package com.example.coronaVirusTracker.controllers;

import com.example.coronaVirusTracker.models.LocationStats;
import com.example.coronaVirusTracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    CoronaVirusDataService coronaVirusDataService; //5.1 fac autowire ca sa stie ca are nevoie de instanta asta pt model

    //5. De aici putem sa accesam un URL, asfel incat sa putem plasa datele despre virus intr-un UI
    // Pentru a accesa un URL ne trebuie un controller
    @GetMapping("/") //"/" = root URL
    public String home(Model model){ //5.0 Pun ce vreau in model si il pot accesa cand fac render la html
        //Nu E Controler REST
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();


        int totalReportedCases = allStats.stream().mapToInt((stat -> stat.getLatestTotalCases())).sum();
        // iau lista de obiecte --> o convertesc in stream --> mapez fiecare obiect la integer total cases si dupa le insumez
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);


        return "home"; // il vom mapa la un fisier HTML. home template va fi fisierul de HTML
    }
// Cream un model punem chestii pe el si in html putem accesa lucruri din model si sa construim html-ul cu modelul
}
