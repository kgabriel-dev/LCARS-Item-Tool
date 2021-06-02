package main.java.config;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Properties;

public class Config {

    private Properties configProps;
    private File jarFile;
    private String configPath;
    private Properties languageProps;

    public Config() {
        try {
            jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            configPath = jarFile.getParent() + File.separator + "config.properties";

            configProps = new Properties();
            InputStream configFileStream = new FileInputStream(configPath);
            configProps.load(new InputStreamReader(configFileStream, Charset.forName("UTF-8")));
            configFileStream.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Properties getLanguageProperties() {
        if(languageProps == null) {
            InputStream languageFileStream = getClass().getResourceAsStream("/language/" + configProps.getProperty("language") + ".properties");

            try {
                languageProps = new Properties();
                languageProps.load(new InputStreamReader(languageFileStream, Charset.forName("UTF-8")));
                languageFileStream.close();
                return languageProps;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return languageProps;
        }

    }

    public String[] getCSV(String name) {
        String csv = configProps.getProperty(name);
        return csv.split(",");
    }

    public String getLanguageWord(String key) {
        return getLanguageProperties().getProperty(key);
    }

    public void setProp(String key, String value) {
        try {
            configProps.setProperty(key, value);

            FileOutputStream configStream = new FileOutputStream(configPath);
            configProps.store(configStream, "last edited: " + key + "->" + value);
            configStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProp(String key) {
        return configProps.getProperty(key);
    }

}
