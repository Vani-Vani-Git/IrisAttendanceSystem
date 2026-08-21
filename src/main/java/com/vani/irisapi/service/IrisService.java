package com.vani.irisapi.service;

import com.vani.irisapi.util.IrisMatcher;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class IrisService {

    private static final String DATASET_PATH = "dataset/";

    public String matchIris(String inputPath) {

        File folder = new File(DATASET_PATH);
        File[] files = folder.listFiles();

        double bestScore = Double.MAX_VALUE;
        String bestMatch = null;

        for (File file : files) {

            double score = IrisMatcher.compare(
                    inputPath,
                    file.getAbsolutePath()
            );

            if (score < bestScore) {
                bestScore = score;
                bestMatch = file.getName().split("\\.")[0];
            }
        }

        System.out.println("Best Match: " + bestMatch);
        System.out.println("Best Score: " + bestScore);

        // Always return the best match
        return bestMatch;
    }
}