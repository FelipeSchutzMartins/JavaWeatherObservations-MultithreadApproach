package com.weatherObservation.threads;

import lombok.var;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class GenerateFileThread extends Thread{

    private FileWriter fileToBeWritten;
    private Integer range;
    private AtomicLong lastId;

    public GenerateFileThread(FileWriter fileToBeWritten, Integer range, AtomicLong lastId) {
        this.fileToBeWritten = fileToBeWritten;
        this.range = range;
        this.lastId = lastId;
    }


    @Override
    public void run() {
        try {
            StringBuilder content = new StringBuilder();
            StringBuilder temperature = new StringBuilder();
            StringBuilder location = new StringBuilder();

            for (int i = 0; i < range; i++) {
                content.append(buildObservationContent(temperature, location));
                fileToBeWritten.write(content.toString());
                content.setLength(0);
                temperature.setLength(0);
                location.setLength(0);
            }
        } catch (ParseException exception) {
            exception.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildObservationContent(StringBuilder temperature, StringBuilder location) throws ParseException {
        var randomObservatory = generateRandomObservatory();
        switch (randomObservatory) {
            case "AU":
                generateObservatoryData(
                        temperature,
                        -88,
                        58,
                        location,
                        -38.168786,
                        145.366598,
                        false
                );
                break;
            case "US":
                generateObservatoryData(
                        temperature,
                        -460,
                        212,
                        location,
                        26.007706,
                        -80.388898,
                        false
                );
                break;
            case "FR":
                generateObservatoryData(
                        temperature,
                        0,
                        373,
                        location,
                        45.599727,
                        2.609266,
                        false
                );
                break;
            case "All Others":
                generateObservatoryData(
                        temperature,
                        0,
                        373,
                        location,
                        0.0,
                        0.0,
                        true
                );
                break;
        }
        return lastId.addAndGet(1L) + "|" + buildTimeStamp() + "|" + location + "|" + temperature + "|" + randomObservatory + "\n";
    }

    private String buildTimeStamp() throws ParseException {
        Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse("30/12/1999");
        Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse("30/12/2020");
        Date randomDate = new Date(ThreadLocalRandom.current()
                .nextLong(d1.getTime(), d2.getTime()));
        SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return isoFormatter.format(randomDate);
    }

    private String generateRandomObservatory() {
        int observatoryChance = buildRandom().nextInt(100);
        if (observatoryChance <= 25)
            return "AU";
        if (observatoryChance<=50)
            return "US";
        if (observatoryChance<=75)
            return "FR";
        return "All Others";
    }

    private void generateObservatoryData(
            StringBuilder temperature,
            Integer minTemperature,
            Integer maxTemperature,
            StringBuilder location,
            Double pointX,
            Double pointY,
            Boolean generateRandomLocation
    ) {
        temperature.append(buildRandom().nextInt(maxTemperature - minTemperature) + minTemperature);
        location.append(generateRandomLocation ? generateRandomLocation() : pointX + "," + pointY);
    }

    private String generateRandomLocation() {
        Double pointX = ((-100) + (100 - (-100)) * buildRandom().nextDouble());
        Double pointY = ((-100) + (100 - (-100)) * buildRandom().nextDouble());
        return pointX + "," + pointY;
    }

    private Random buildRandom() {
        return new Random();
    }


}
