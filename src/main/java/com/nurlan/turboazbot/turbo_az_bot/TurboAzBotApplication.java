package com.nurlan.turboazbot.turbo_az_bot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling //avtomatik islerin icrasi ucun
public class TurboAzBotApplication implements CommandLineRunner {



    public static void main(String[] args) {
		SpringApplication.run(TurboAzBotApplication.class, args);

	}

	@Override
	public void run(String... args) throws IOException {

		}
	}




