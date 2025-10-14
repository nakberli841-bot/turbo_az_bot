package com.nurlan.turboazbot.turbo_az_bot.telegram;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TurboAzBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "Turboaz_Helper1_bot";
    }

    @Override
    public String getBotToken() {
        return "8259579588:AAG5d2z3ICh78o22Ie_3FYb9l7GO63zE0xQ";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("GELEN MESAJ VAR!");
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            System.out.println("MESAJ: " + message);

            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            response.setText("Sən yazdın: " + message);

            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
