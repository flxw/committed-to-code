package de.flxw;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.Random;
import org.apache.commons.cli.*;
import static java.lang.Math.abs;

public class Main {
    public static void main(String[] args) throws GitAPIException {
        Options options = new Options();

        Option input = new Option("c", "configurationfile", true, "configuration file path");
        input.setRequired(true);
        options.addOption(input);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        Configuration config = null;
        try {
            String configurationFile = cmd.getOptionValue("configurationfile");
            config = Configuration.setupConfiguration(configurationFile);
        } catch (IOException e) {
            System.out.println("Configuration file is not accessible");
            System.exit(2);
        }

        System.out.println(config);

        /*final int breakDays = 35;
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
           .call();*/
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
