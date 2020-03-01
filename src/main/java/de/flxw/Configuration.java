package de.flxw;

import lombok.Getter;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.stream.Stream;

public class Configuration {
    private static Configuration instance;

    @Getter private String repoDir;
    @Getter private LocalDate startDate;
    @Getter private LocalDate endDate;
    @Getter private Integer breakdays;
    @Getter private String name;
    @Getter private String email;
    @Getter private Integer lowerFrequencyBound;
    @Getter private Integer upperFrequencyBound;

    private Configuration(){}

    public static synchronized Configuration getInstance(){
        return instance;
    }

    public static Configuration setupConfiguration(String configPath) throws IOException, ConfigurationException {
        if (instance != null){
            return instance;
        } else {
            instance = new Configuration();
        }

        Ini configFile = new Ini(new File(configPath));

        String repoDir   = configFile.get("GENERAL", "REPODIRECTORY");
        String startDate = configFile.get("GENERAL", "STARTDATE");
        String endDate   = configFile.get("GENERAL", "ENDDATE");
        String breakdays = configFile.get("GENERAL", "BREAK_DAYS");
        String name      = configFile.get("USER", "NAME");
        String email     = configFile.get("USER", "EMAIL");
        String lower     = configFile.get("FREQUENCY", "LOWER");
        String upper     = configFile.get("FREQUENCY", "UPPER");

        boolean anyNotSet = Stream.of(repoDir,
                                        startDate,
                                        endDate,
                                        name,
                                        breakdays,
                                        email,
                                        lower,
                                        upper).anyMatch(x -> x == null || x.isEmpty());

        if (anyNotSet) {
            throw new ConfigurationNullException();
        } else {
            try {
                instance.repoDir = repoDir;
                instance.startDate = LocalDate.parse(startDate);
                instance.endDate = LocalDate.parse(endDate);
                instance.breakdays = Integer.parseInt(breakdays);
                instance.name = name;
                instance.email = email;
                instance.lowerFrequencyBound = Integer.parseInt(lower);
                instance.upperFrequencyBound = Integer.parseInt(upper);
            } catch (DateTimeParseException ex) {
                throw new ConfigurationBadException(ex.getParsedString());
            } catch (NumberFormatException ex) {
                throw new ConfigurationBadException(ex.getMessage());
            }
        }

        return instance;
    }


}