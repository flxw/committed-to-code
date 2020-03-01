package de.flxw;

public class ConfigurationNullException extends ConfigurationException {
    @Override
    public String toString() {
        return "A configuration parameter was not set. Please revisit the configuration and set it.";
    }
}
