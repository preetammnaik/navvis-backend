package com.example.navvis.services;

import com.example.navvis.data.Building;
import com.example.navvis.data.Floor;
import com.example.navvis.data.Point3D;
import com.example.navvis.data.dto.LocationResponseDto;
import com.example.navvis.factory.LocationResponseFactory;
import com.example.navvis.utils.GeometryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Slf4j
@Service
public class CoordinationLocalizationServiceImpl implements CoordinationLocalizationService {

    private final List<Building> buildings;
    private final LocationResponseFactory locationResponseFactory;

    @Autowired
    public CoordinationLocalizationServiceImpl(List<Building> buildings, LocationResponseFactory locationResponseFactory) {
        this.buildings = buildings;
        this.locationResponseFactory = locationResponseFactory;
        log.info("Loaded {} buildings.", buildings.size());
    }

    /**
     * Finds if  a given 3D point is within known buildings and their floors.
     *
     * @param point the point to localize
     * @return a {@link LocationResponseDto} describing where the point is located
     */
    @Cacheable(value = "localization", key = "#point.x + '_' + #point.y + '_' + #point.z", unless = "#result.found == false")
    @Override
    public LocationResponseDto localizePoint(Point3D point) {
        if (point == null) {
            log.warn("Received null point for localization");
            return locationResponseFactory.notFound();
        }

        if (CollectionUtils.isEmpty(buildings)) {
            log.debug("No buildings available for point: {}", point);
            return locationResponseFactory.notFound();
        }

        for (Building building : buildings) {
            if (isInsideBuilding(point, building)) {
                for (Floor floor : building.getFloors()) {
                    if (isInsideFloor(point, floor)) {
                        //found
                        return locationResponseFactory.found(building.getName(), floor.getName());
                    }
                }

                // In building, but not on any floor
                return locationResponseFactory.foundInBuildingOnly(building.getName());
            }
        }

        // not found
        return locationResponseFactory.notFound();
    }

    /**
     * Checks whether a point lies inside a building based on its 2D outline and height range.
     *
     * @param point    point to evaluate
     * @param building building definition
     * @return true if the point is within the building
     */
    private boolean isInsideBuilding(Point3D point, Building building) {
        boolean insideXY = GeometryUtils.isPointInPolygon(point.getX(), point.getY(), building.getOutline());
        boolean insideZ = building.getHeight() != null && building.getHeight().contains(point.getZ());
        return insideXY && insideZ;
    }

    /**
     * Checks whether a point lies inside a floor based on its polygon outline and height range.
     *
     * @param point point to evaluate
     * @param floor floor definition
     * @return true if the point is within the floor
     */
    private boolean isInsideFloor(Point3D point, Floor floor) {
        boolean insideXY = GeometryUtils.isPointInPolygon(point.getX(), point.getY(), floor.getOutline());
        boolean insideZ = floor.getHeight() != null && floor.getHeight().contains(point.getZ());
        return insideXY && insideZ;
    }
}
