package de.flxw;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.apache.commons.cli.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args)  {
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
            System.out.println("Configuration file is not accessible.");
            System.exit(2);
        } catch (ConfigurationException e) {
            System.out.println(e);
            System.exit(3);
        }

        try {
            RandomCommiter rc = new RandomCommiter();
            rc.executeCommits();
        } catch (IOException e) {
            System.out.println("Could not perform IO correctly, check permissions and storage");
            System.exit(4);
        } catch (GitAPIException e) {
            System.out.println("Something failed with JGIT...");
            e.printStackTrace();
            System.exit(5);
        }
    }
}
