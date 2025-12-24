package com.example.navvis.config;

import com.example.navvis.data.Building;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class DataLoader {

    /**
     * Loads building data from the {@code example_data.json} file.
     *
     * @param objectMapper the Jackson {@link ObjectMapper} used to parse the JSON data
     * @return a list of loaded {@link Building} objects, or an empty list if loading fails
     */
    @Bean
    public List<Building> loadBuildings(ObjectMapper objectMapper) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("example_data.json");

            if (inputStream == null) {
                log.error("Cannot find example_data.json in classpath");
                return null;
            }

            List<Building> loadedBuildings = objectMapper.readValue(inputStream, new TypeReference<>() {});

            log.info("Successfully loaded {} buildings", loadedBuildings.size());

            return loadedBuildings;

        } catch (Exception e) {
            log.error("Failed to load building data", e);
        }
        return List.of();
    }
}
