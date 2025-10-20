package com.nurlan.turboazbot.turbo_az_bot.telegram;

import com.nurlan.turboazbot.turbo_az_bot.dto.CarAdDto;
import com.nurlan.turboazbot.turbo_az_bot.repo.NotificationLogRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.UserDataRepo;
import com.nurlan.turboazbot.turbo_az_bot.repo.UsersessionRepo;
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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TurboAzBot extends TelegramLongPollingBot implements ApplicationListener<ApplicationReadyEvent> {

    private final NotificationLogRepo notificationLogRepo;
    private final UserDataRepo userDataRepo;
    private final UsersessionRepo usersessionRepo;

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

              Optional<UserSession> userSession=  usersessionRepo.findByTelegramChatId(chatId);
                if(userSession.isPresent()) {
                    sendMessage(chatId, "geri geldiyin ucun sevindik "+userSession.get().getName()+" bey 👋,yeni kriterialarinizi,Hansi marka masin isteyirsiz,kecmek ucun skip ede bilersiz");
                    userSession.get().setStep(2);
                    sessions.put(chatId, userSession.get());
                }else {
                    sendMessage(chatId, "Xoş gəldin! 👋\nZəhmət olmasa adını yaz:");
                    sessions.put(chatId, new UserSession());
                    UserSession sessionDB= sessions.get(chatId);
                    sessionDB.setTelegramChatId(chatId);

                }
                return;
            }
            if (text.equalsIgnoreCase("new")) {


                Optional<UserSession> userSession=  usersessionRepo.findByTelegramChatId(chatId);
                if(userSession.isPresent()) {
                    sessions.put(chatId, userSession.get());
                }else {
                    sessions.put(chatId, new UserSession());
                    UserSession sessionDB= sessions.get(chatId);
                    sessionDB.setTelegramChatId(chatId);

                }

                UserSession oldSession = sessions.get(chatId);
                if (oldSession == null || oldSession.getName() == null || oldSession.getEmail() == null) {
                    sendMessage(chatId, "Zəhmət olmasa əvvəlcə /start yaz və ad/email daxil et.");
                    return;
                }
                UserSession newSession = new UserSession();
                newSession.setName(oldSession.getName());
                newSession.setEmail(oldSession.getEmail());
                newSession.setTelegramChatId(chatId);
                newSession.setStep(2);

                sessions.put(chatId, newSession);


                sendMessage(chatId, "Yeni kriteriya daxil etməyə başlaya bilərsən 🔍\nMarkanı yaz (məs: BMW X6).  bos qoymaq isteyirsense skip yaz");
                return;

            }

            UserSession session = sessions.get(chatId);
            if (session == null) {
                sendMessage(chatId, "Zəhmət olmasa əvvəlcə /start yaz.");
                return;
            }

           int step=session.getStep();
            switch (step) {
                case 0 -> {
                    if (text.trim().isEmpty()) {
                        sendMessage(chatId, "Ad boş ola bilməz, zəhmət olmasa adını yaz:");
                        return;
                    }
                    session.setName(text);
                    sendMessage(chatId, "Əla! İndi email ünvanını daxil et:");
                    session.setStep(step + 1);
                }
                case 1 -> {
                    if (!text.contains("@")) {
                        sendMessage(chatId, "Email düzgün formatda deyil. Yenidən cəhd et:");
                        return;
                    }
                    session.setEmail(text);
                    sendMessage(chatId, "Hansı markanı axtarırsan? (məs: BMW X6)  bos qoymaq isteyirsense skip yaz");
                    session.setStep(step + 1);
                }
                case 2 -> {
                    if (!text.equalsIgnoreCase("skip"))
                        session.setTitle(text);
                    sendMessage(chatId, "Minimum və maxsimum ili daxil et (məs: 2005-2006):  bos qoymaq isteyirsense skip yaz");
                    session.setStep(step + 1);
                }
                case 3 -> {
                    if (!text.equalsIgnoreCase("skip")) {
                        try {
                            String[] years = text.split("-");
                            session.setYearFrom((Integer.parseInt(years[0].trim())));
                            session.setYearTo((Integer.parseInt(years[1].trim())));
                        } catch (Exception e) {
                            sendMessage(chatId, "Yanlış format. Məs: 2005-2020 şəklində yaz:");
                            return;
                        }
                    }
                    sendMessage(chatId, "Qiymət aralığını yaz (məs: 20000-30000).   bos qoymaq isteyirsense skip yaz");
                    session.setStep(step + 1);                }
                case 4 -> {
                    if (!text.equalsIgnoreCase("skip")) {
                        try {
                            String[] prices = text.split("-");
                            session.setPriceFrom(Integer.parseInt(prices[0].trim()));
                            session.setPriceTo(Integer.parseInt(prices[1].trim()));
                        } catch (Exception e) {
                            sendMessage(chatId, "Yanlış format. Məs: 20000-30000 şəklində yaz:");
                            return;
                        }
                    }
                    sendMessage(chatId, "Saat aralığını yaz (məs: 09:00:00-18:00:00).  bos qoymaq isteyirsense skip yaz");
                    session.setStep(step + 1);
                }
                case 5 -> {
                    if (!text.equalsIgnoreCase("skip")) {
                        try {
                            String[] times = text.split("-");
                            session.setCreatedAtFrom(LocalTime.parse(times[0].trim()));
                            session.setCreatedAtTo(LocalTime.parse(times[1].trim()));
                        } catch (Exception e) {
                            sendMessage(chatId, "Yanlış format. Məs: 09:00:00-18:00:00 şəklində yaz:");
                            return;
                        }
                    }

                    UserSession sessionDB= sessions.get(chatId);
                    usersessionRepo.save(sessionDB);


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
            user.setName(session.getName());
            user.setEmail(session.getEmail());
            user.setTelegramChatId(chatId);

            SearchCriteria criteria = new SearchCriteria();
            criteria.setTitle(session.getTitle());
            criteria.setYearFrom(session.getYearFrom());
            criteria.setYearTo(session.getYearTo());
            criteria.setPriceFrom(session.getPriceFrom());
            criteria.setPriceTo(session.getPriceTo());
            criteria.setCreatedAtFrom(session.getCreatedAtFrom());
            criteria.setCreatedAtTo(session.getCreatedAtTo());

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

                sendMessage(chatId, "axtaris yekunlasdi! masinlar tapildi");

                int count = 0;
                for (CarAd car : cars) {
                    count++;
                    sendMessage(chatId, count + " ci maşın: " + car.getLink());


                }

             User userDB=userDataRepo.findByEmail(user.getEmail());

                user.setId(userDB.getId());

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