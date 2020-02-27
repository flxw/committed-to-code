package de.flxw;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import static java.lang.Math.abs;

public class Main {
    public static void main(String[] args) throws GitAPIException {
        final int breakDays = 35;
        final LocalDate startDate = LocalDate.of(2019, 1, 1);
        final LocalDate endDate = LocalDate.of(2019,10,1);

        final int maximumCommitsPerDay;
        final String repoPath = "/Users/f.wolff/testrepo";

        File repoDir = new File(repoPath);

        Git git = Git.init()
                     .setDirectory(repoDir)
                     .call();

        String newRandomFile = UUID.randomUUID().toString();
        String dateString = "";

        for (int i=0; i < breakDays; ++i) {
            LocalDate randomDate = generateRandomDateBetweenTwoDates(startDate, endDate);
            dateString += randomDate.toString() + "\n";
        }

        writeToFile(dateString, new File(repoPath, newRandomFile));

        git.add().addFilepattern(".").call();
        git.commit()
           .setMessage("Commit all changes including additions")
           .call();
    }

    private static LocalDate generateRandomDateBetweenTwoDates (LocalDate begin, LocalDate end) {
        int days = (int) ChronoUnit.DAYS.between(begin, end);
        Random randomIntGenerator = new Random();

        int randomDateOffset = abs(randomIntGenerator.nextInt()) % days;
        LocalDate randomDate = begin.plusDays(randomDateOffset);
        return randomDate;
    }

    private static void writeToFile(String data, File file) {
        FileWriter fr = null;

        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
