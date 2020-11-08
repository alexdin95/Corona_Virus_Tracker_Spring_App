package com.example.coronaVirusTracker.services;

import com.example.coronaVirusTracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service // 1. Ii spun lui Spring ca acesta este un Service
public class CoronaVirusDataService { //0. Trebuie transformat in Spring Service Si sa ii spun sa foloseasca metoda la activare

    private static String  VIRUS_DATA_URL ="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    //4.1 creez o lista pentur obiectele de tip LocationStats
    private List<LocationStats> allStats = new ArrayList<>(); // acum pot popula acest list

    @PostConstruct //2. Ii spun lui Spring cand Construiesti aceasta instanta a Serviciului, dupa ce ai terminat executa aceasta metoda
    @Scheduled(cron ="* * 1 * * *") //!!!How often do i want it to run to refresh the data
    public void fetchVirusData() throws IOException, InterruptedException {
        //4.2 Creez o lista noua din motive de Concurency
        List<LocationStats> newStats = new ArrayList<>(); // ulterior voi popula allStats cu newStats
        // Fac un Http call utilizand Http Client-ul din java
        HttpClient client = HttpClient.newHttpClient(); // Aici Creez un client nou
        HttpRequest request= HttpRequest.newBuilder() // Aici Creez un request care ne lasa sa utilizam metoda newBuilder
                .uri(URI.create(VIRUS_DATA_URL)) // utilizat ca sa transform un String in Url artifact
                .build(); //
        //Voi primi un responde trimitand la Client acest Request
        HttpResponse<String> httpResponse= client.send(request, HttpResponse.BodyHandlers.ofString()); // Trimite Request-ul si dupa virgula ii spune ce sa faca cu raspunsul
        //HttpResponse.BodyHandlers.ofString() --> ia corpul si il returneaza ca un string.
        //La send trebuie sa arunc sau sa prind exceptia "throws IOException, InterruptedException {"
        //System.out.println(httpResponse.body()); // printez raspunsul.


        //3.1 Copiat Direct de pe Site-ul librariei de parsat csv.
        // Detectare automata a header-ului deoarece era prezent in fisierul csv Raw la inceput


        //3.2 Ne trebuie o instanta de Reader, pe site ia din fisier, eu iau din String, utilizand StringReader

        Reader in = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();

            if (record.get("Province/State").isEmpty()) {locationStat.setState("-");} else {locationStat.setState(record.get("Province/State"));}
            locationStat.setCountry(record.get("Country/Region"));

            int latestCases = Integer.parseInt(record.get(record.size()-1));
            int prevDayCases = Integer.parseInt(record.get(record.size()-2));


            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPrevDay(latestCases-prevDayCases);
            //System.out.println(locationStat);
            newStats.add(locationStat);

            //String state = record.get("Province/State");
            //System.out.println(state);
            //String customerNo = record.get("CustomerNo");
            //String name = record.get("Name");
        }
        this.allStats=newStats;

    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }
}
