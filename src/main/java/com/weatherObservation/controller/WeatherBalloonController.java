package com.weatherObservation.controller;

import com.weatherObservation.service.GenerateDataFileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.text.ParseException;

@RequestMapping("/weatherBalloon")
@AllArgsConstructor
@Controller
public class WeatherBalloonController {
    private GenerateDataFileService generateDataFileService;

    @PostMapping("/multiThreadGenerateFile")
    public ResponseEntity<?> generateFile() throws IOException, ParseException, InterruptedException {
        generateDataFileService.generateFile();
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
