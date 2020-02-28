package de.flxw;

import lombok.Getter;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.Date;

public class Configuration {
    private static Configuration instance;

    @Getter
    private String repoDir;

    @Getter
    private Date startDate;

    @Getter
    private Date endDate;

    @Getter
    private int breakdays;


    private Configuration(){}

    public static synchronized Configuration getInstance(){
        return instance;
    }

    public static Configuration setupConfiguration(String configPath) throws IOException {
        if (instance != null){
            return instance;
        } else {
            instance = new Configuration();
        }

        Ini configFile = new Ini(new File(configPath));

        instance.repoDir = configFile.get("GENERAL", "REPODIRECTORY");
        instance.startDate = configFile.get("GENERAL", "STARTDATE", Date.class);
        instance.endDate  = configFile.get("GENERAL", "ENDDATE", Date.class);
        instance.breakdays = configFile.get("GENERAL", "BREAK_DAYS", int.class);

        return instance;
    }

    private boolean isAnyParameterBad() {
        boolean returnValue;

        returnValue  = repoDir == null;
        returnValue |= startDate == null;
        returnValue |= endDate   == null;
        //TODO returnValue |= breakdays == null;

        return returnValue;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "repoDir='" + repoDir + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", breakdays=" + breakdays +
                '}';
    }
}