package com.weatherObservation.service;

import com.weatherObservation.threads.GenerateFileThread;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
@Service
public class GenerateDataFileService {

    private FileUtilsService fileUtilsService;

    public void generateFile() throws IOException, ParseException, InterruptedException {
        fileUtilsService.deleteExitingFile("flyingData");
        var file = new File(fileUtilsService.getDefaultFolderPath() + "/flyingData.txt");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        buildHeader(fw);
        var lastId = new AtomicLong(0L);

        List<Thread> threads = new ArrayList<>();
        threads.add(new GenerateFileThread(fw, 250_000, lastId));
        threads.add(new GenerateFileThread(fw, 250_000, lastId));
        threads.add(new GenerateFileThread(fw, 250_000, lastId));
        threads.add(new GenerateFileThread(fw, 250_000, lastId));

        for (Thread thread : threads)
            thread.start();

        for(Thread thread : threads)
            thread.join();

        fw.close();
    }
    private void buildHeader(FileWriter file) throws IOException {
        file.write("id|timestamp|location|temperature|observatory\n");
    }
}
