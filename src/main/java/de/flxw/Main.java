package de.flxw;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.apache.commons.cli.*;
import java.io.IOException;

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
            System.out.println("Configuration file is not accessible.");
            System.exit(2);
        } catch (ConfigurationException e) {
            System.out.println("Please check the configuration file contents, they are not valid.");
            System.exit(3);
        }

        RandomCommiter rc = new RandomCommiter();
        rc.executeCommits();
    }
}
