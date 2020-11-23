package config;

import java.io.*;
import java.util.Properties;

public class Config {

    private File configFile;
    private Properties configProps;

    public Config() {
        configFile = new File("resources/config.txt");

        try {
            FileReader reader = new FileReader(configFile);
            configProps = new Properties();
            configProps.load(reader);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getLanguageProperties() {
        File languageFile = new File("resources/language/" + configProps.get("language") + ".txt");

        try {
            FileReader reader = new FileReader(languageFile);
            Properties languageProps = new Properties();
            languageProps.load(reader);

            return languageProps;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getLanguageWord(String key) {
        return getLanguageProperties().getProperty(key);
    }

    public void setProp(String key, String value) {
        configProps.setProperty(key, value);
        try {
            FileWriter writer = new FileWriter(configFile);
            configProps.store(writer, "config file");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProp(String key) {
        return configProps.getProperty(key);
    }

}
