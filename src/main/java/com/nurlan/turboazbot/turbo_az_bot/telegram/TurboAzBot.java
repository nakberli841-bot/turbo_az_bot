package com.nurlan.turboazbot.turbo_az_bot.telegram;

import com.nurlan.turboazbot.turbo_az_bot.dto.CarAdDto;
import com.nurlan.turboazbot.turbo_az_bot.repo.NotificationLogRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.nurlan.turboazbot.turbo_az_bot.entity.*;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class TurboAzBot extends TelegramLongPollingBot implements ApplicationListener<ApplicationReadyEvent> {

    private final NotificationLogRepo notificationLogRepo;

    private final Map<Long, UserSession> sessions = new HashMap<>();



    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("Bot başladı və Telegram-a qoşuldu!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();


            if (text.equals("/start")) {
                sendMessage(chatId, "Xoş gəldin! 👋\nZəhmət olmasa adını yaz:");
                sessions.put(chatId, new UserSession());
                return;
            }
            if (text.equalsIgnoreCase("new")) {

                UserSession oldSession = sessions.get(chatId);
                if (oldSession == null || oldSession.name == null || oldSession.email == null) {
                    sendMessage(chatId, "Zəhmət olmasa əvvəlcə /start yaz və ad/email daxil et.");
                    return;
                }
                UserSession newSession = new UserSession();
                newSession.name = oldSession.name;
                newSession.email = oldSession.email;
                newSession.step = 2; // Yəni title-dan başlayacaq

                sessions.put(chatId, newSession);


                sendMessage(chatId, "Yeni kriteriya daxil etməyə başlaya bilərsən 🔍\nMarkanı yaz (məs: BMW X6).  bos qoymaq isteyirsense skip yaz");
                return;

            }

            UserSession session = sessions.get(chatId);
            if (session == null) {
                sendMessage(chatId, "Zəhmət olmasa əvvəlcə /start yaz.");
                return;
            }

            switch (session.step) {
                case 0 -> {
                    if (text.trim().isEmpty()) {
                        sendMessage(chatId, "Ad boş ola bilməz, zəhmət olmasa adını yaz:");
                        return;
                    }
                    session.name = text;
                    sendMessage(chatId, "Əla! İndi email ünvanını daxil et:");
                    session.step++;
                }
                case 1 -> {
                    if (!text.contains("@")) {
                        sendMessage(chatId, "Email düzgün formatda deyil. Yenidən cəhd et:");
                        return;
                    }
                    session.email = text;
                    sendMessage(chatId, "Hansı markanı axtarırsan? (məs: BMW X6)  bos qoymaq isteyirsense skip yaz");
                    session.step++;
                }
                case 2 -> {
                    if (!text.equalsIgnoreCase("skip"))
                        session.title = text;
                    sendMessage(chatId, "Minimum və maxsimum ili daxil et (məs: 2005-2006):  bos qoymaq isteyirsense skip yaz");
                    session.step++;
                }
                case 3 -> {
                    if (!text.equalsIgnoreCase("skip")) {
                        try {
                            String[] years = text.split("-");
                            session.yearFrom = Integer.parseInt(years[0].trim());
                            session.yearTo = Integer.parseInt(years[1].trim());
                        } catch (Exception e) {
                            sendMessage(chatId, "Yanlış format. Məs: 2005-2020 şəklində yaz:");
                            return;
                        }
                    }
                    sendMessage(chatId, "Qiymət aralığını yaz (məs: 20000-30000).   bos qoymaq isteyirsense skip yaz");
                    session.step++;
                }
                case 4 -> {
                    if (!text.equalsIgnoreCase("skip")) {
                        try {
                            String[] prices = text.split("-");
                            session.priceFrom = Integer.parseInt(prices[0].trim());
                            session.priceTo = Integer.parseInt(prices[1].trim());
                        } catch (Exception e) {
                            sendMessage(chatId, "Yanlış format. Məs: 20000-30000 şəklində yaz:");
                            return;
                        }
                    }
                    sendMessage(chatId, "Saat aralığını yaz (məs: 09:00:00-18:00:00).  bos qoymaq isteyirsense skip yaz");
                    session.step++;
                }
                case 5 -> {
                    if (!text.equalsIgnoreCase("skip")) {
                        try {
                            String[] times = text.split("-");
                            session.createdAtFrom = LocalTime.parse(times[0].trim());
                            session.createdAtTo = LocalTime.parse(times[1].trim());
                        } catch (Exception e) {
                            sendMessage(chatId, "Yanlış format. Məs: 09:00:00-18:00:00 şəklində yaz:");
                            return;
                        }
                    }


                    sendMessage(chatId, "Məlumatlar uğurla qəbul edildi ✅ Axtarış başlayır...");
                    sendToBackend(session, chatId);


                    sendMessage(chatId, "daxil etmek istediyiniz novbeti kriteriyalar varsa   new promptunu daxil edin!...");

                }

            }
        }
    }


    private void sendToBackend(UserSession session, Long chatId) {
        try {
            final String apiUrl = "http://localhost:8080/api/user/creatUserAndCriteria";

            User user = new User();
            user.setName(session.name);
            user.setEmail(session.email);
            user.setTelegramChatId(chatId);

            SearchCriteria criteria = new SearchCriteria();
            criteria.setTitle(session.title);
            criteria.setYearFrom(session.yearFrom);
            criteria.setYearTo(session.yearTo);
            criteria.setPriceFrom(session.priceFrom);
            criteria.setPriceTo(session.priceTo);
            criteria.setCreatedAtFrom(session.createdAtFrom);
            criteria.setCreatedAtTo(session.createdAtTo);

            List<SearchCriteria> list = List.of(criteria);

            CarAdDto dto = new CarAdDto();
            dto.setUser(user);
            dto.setSearchCriteria(list);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CarAdDto> request = new HttpEntity<>(dto, headers);

            ResponseEntity<List<CarAd>> response = restTemplate.exchange(
                    apiUrl,
                    org.springframework.http.HttpMethod.POST,
                    request,
                    new org.springframework.core.ParameterizedTypeReference<>() {
                    }
            );

            List<CarAd> cars = response.getBody();
            if (cars != null && !cars.isEmpty()) {


                int count = 0;
                for (CarAd car : cars) {
                    count++;
                    sendMessage(chatId, "axtaris yekunlasdi! masin tapildi");
                    sendMessage(chatId, count + " ci maşın: " + car.getLink());


                }

                NotificationLog notificationLog = new NotificationLog();


                notificationLog.setUser(user);
                notificationLog.setCars(cars);
                notificationLog.setSentAt(LocalDateTime.now());
                notificationLogRepo.save(notificationLog);


            } else {
                sendMessage(chatId, "Kriteriyanıza uyğun maşın tapılmadı, yeni elan çıxanda xəbər veriləcək 🚗");
            }

        } catch (Exception e) {
            System.out.println("xeta bas verdi: "+e.getClass().getName()+": "+e.getMessage());

            Throwable cause = e.getCause();
            while (cause != null) {
                System.err.println("CAUSE: " + cause.getClass().getName() + " - " + cause.getMessage());
                cause = cause.getCause();
            }
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "Turboaz3_bot";
    }

    @Override
    public String getBotToken() {
        return "8454907934:AAElOAf1o6Cagk745u2saG1wtI5wV6QopOI";
    }


    public void sendMessage(Long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(text);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}