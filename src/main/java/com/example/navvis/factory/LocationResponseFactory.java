package com.example.navvis.factory;

import com.example.navvis.data.dto.LocationResponseDto;

public interface LocationResponseFactory {
    LocationResponseDto found(String buildingName, String floorName);

    LocationResponseDto foundInBuildingOnly(String buildingName);

    LocationResponseDto notFound();
}
