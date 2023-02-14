package com.weatherObservation.service;

import com.weatherObservation.threads.GenerateFileThread;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
@Service
public class GenerateDataFileService {

    private FileUtilsService fileUtilsService;

    public void generateFile() throws IOException, InterruptedException {
        fileUtilsService.deleteExitingFile("flyingData");
        var file = new File(fileUtilsService.getDefaultFolderPath() + "/flyingData.txt");
        file.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        buildHeader(bw);
        var lastId = new AtomicLong(1L);

        List<Thread> threads = new ArrayList<>();
        threads.add(new GenerateFileThread(bw, 250_000, lastId));
        threads.add(new GenerateFileThread(bw, 250_000, lastId));
        threads.add(new GenerateFileThread(bw, 250_000, lastId));
        threads.add(new GenerateFileThread(bw, 250_000, lastId));

        for (Thread thread : threads)
            thread.start();

        for(Thread thread : threads)
            thread.join();

        bw.close();
    }
    private void buildHeader(BufferedWriter file) throws IOException {
        file.write("id|timestamp|location|temperature|observatory\n");
    }
}
