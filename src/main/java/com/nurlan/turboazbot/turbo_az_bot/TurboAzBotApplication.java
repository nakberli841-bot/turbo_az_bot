package com.nurlan.turboazbot.turbo_az_bot;

import com.nurlan.turboazbot.turbo_az_bot.telegram.TurboAzBot;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling //avtomatik islerin icrasi ucun
public class TurboAzBotApplication  {



    public static void main(String[] args) {
		SpringApplication.run(TurboAzBotApplication.class, args);

	}






	}




