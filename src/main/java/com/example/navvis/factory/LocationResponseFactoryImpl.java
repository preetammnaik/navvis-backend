package com.example.navvis.factory;

import com.example.navvis.data.dto.LocationResponseDto;
import org.springframework.stereotype.Component;

@Component
public class LocationResponseFactoryImpl implements LocationResponseFactory {

    /**
     * Creates a response indicating that the point was found in a specific building and floor.
     *
     * @param buildingName the name of the building where the point was found
     * @param floorName    the name of the floor where the point was found
     * @return a populated {@link LocationResponseDto}
     */
    public LocationResponseDto found(String buildingName, String floorName) {
        return new LocationResponseDto(buildingName, floorName, true);
    }

    /**
     * Creates a response indicating that the point is inside a building
     * but not inside any of its floors.
     *
     * @param buildingName the name of the building where the point was detected
     * @return a {@link LocationResponseDto} with floor information omitted
     */
    public LocationResponseDto foundInBuildingOnly(String buildingName) {
        return new LocationResponseDto(buildingName, null, true);
    }

    /**
     * Creates a response indicating that the point could not be localized
     * in any known building or floor.
     *
     * @return an empty {@link LocationResponseDto} with {@code found = false}
     */
    public LocationResponseDto notFound() {
        return new LocationResponseDto(null, null, false);
    }
}
