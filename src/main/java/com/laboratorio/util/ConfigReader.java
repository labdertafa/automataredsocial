package com.laboratorio.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.util.Properties;

/**
 *
 * author Rafael
 * version 1.0
 * created 04/05/2025
 * updated 04/05/2025
 */
public class ConfigReader {
    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private final Properties properties;

    public ConfigReader(String filePath) {
        this.properties = new Properties();
        this.loadProperties(filePath);
    }

    private void loadProperties(String filePath) {
        try {
            this.properties.load(new FileReader(filePath));
        } catch (Exception e) {
            log.error("Ha ocurrido un error leyendo el fichero de configuración para el autómata: {}. Finaliza la aplicación!", filePath);
            log.error("Error: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("Causa: {}", e.getCause().getMessage());
            }
            System.exit(-1);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}