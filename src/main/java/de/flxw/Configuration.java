package de.flxw;

import lombok.Getter;
import org.ini4j.Ini;
import de.flxw.ConfigurationException;
import java.io.File;
import java.io.IOException;
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

    public static Configuration setupConfiguration(String configPath) throws IOException, ConfigurationException {
        if (instance != null){
            return instance;
        } else {
            instance = new Configuration();
        }

        Ini configFile = new Ini(new File(configPath));

        instance.repoDir = configFile.get("GENERAL", "REPODIRECTORY");
        instance.startDate = configFile.get("GENERAL", "STARTDATE", Date.class);
        instance.endDate  = configFile.get("GENERAL", "ENDDATE", Date.class);
        String tempInt = configFile.get("GENERAL", "BREAK_DAYS");
        instance.breakdays = (tempInt == null) ? -1 : Integer.parseInt(tempInt);

        if (instance.isAnyParameterBad()) {
            //TODO list the bad parameters
            throw new ConfigurationException();
        }

        return instance;
    }

    private boolean isAnyParameterBad() {
        boolean returnValue = false;

        returnValue  = repoDir == null;
        returnValue |= startDate == null;
        returnValue |= endDate   == null;
        returnValue |= breakdays == -1;

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