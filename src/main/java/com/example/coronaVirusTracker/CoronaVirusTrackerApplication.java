package com.example.coronaVirusTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Ca spring sa stie sa utilizeze @Scheduled din Serviciul CoronaVirusDataService
public class CoronaVirusTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoronaVirusTrackerApplication.class, args);
	}

}

// Cream un model punem chestii pe el si in html putem accesa lucruri din model si sa construim html-ul cu modelul