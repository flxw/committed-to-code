package de.flxw;

public class ConfigurationBadException extends ConfigurationException {
    private final String badValue;

    public ConfigurationBadException(String bv) {
        badValue = bv;
    }

    @Override
    public String toString() {
        return String.format("This configuration value can not be parsed: '%s'. " +
                             "Please adjust it appropriately for the parameter.", badValue);
    }
}
