package com.vani.irisapi.service;
import com.vani.irisapi.util.IrisMatcher;
import org.springframework.stereotype.Service;
import java.io.File;
import org.opencv.core.*;
@Service
public class IrisService {
    private static final String DATASET_PATH = System.getProperty("java.io.tmpdir");
    public String matchIris(String inputPath) {

        File folder = new File(DATASET_PATH);
        File[] files = folder.listFiles();
        double bestScore = Double.MAX_VALUE;
        double secondBest = Double.MAX_VALUE;
        String bestMatch = null;

        for (File file : files) {

            double score = IrisMatcher.compare(inputPath, file.getAbsolutePath());

            if (score < bestScore) {
                secondBest = bestScore;
                bestScore = score;
                bestMatch = file.getName().split("\\.")[0];
            } else if (score < secondBest) {
                secondBest = score;
            }
        }
        System.out.println("Best Match: " + bestMatch);
        System.out.println("Best Score: " + bestScore);
        System.out.println("Second Best: " + secondBest);
        if (bestScore < 12 && (secondBest - bestScore) > 1.0) {
            return bestMatch;
        }
        return null;
    }
}