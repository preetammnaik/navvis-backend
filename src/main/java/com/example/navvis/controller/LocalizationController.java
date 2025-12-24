package com.example.navvis.controller;


import com.example.navvis.data.Point3D;
import com.example.navvis.data.dto.LocationResponseDto;
import com.example.navvis.exceptions.PointLocalizationException;
import com.example.navvis.services.CoordinationLocalizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/v1/localize")
public class LocalizationController {
    private final CoordinationLocalizationService coordinationLocalizationService;

    @Autowired
    public LocalizationController(CoordinationLocalizationService coordinationLocalizationService) {
        this.coordinationLocalizationService = coordinationLocalizationService;
    }

    @PostMapping(path = "/point", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationResponseDto> localizePoint(@RequestBody Point3D point) {
        try {
            log.info("Received localization coordinates: x={}, y={}, z={}", point.getX(), point.getY(), point.getZ());

            LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

            log.info("Location: building={}, floor={}, found={}", result.getBuilding(), result.getFloor(), result.isFound());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error during localization for point x={}, y={}, z={}", point.getX(), point.getY(), point.getZ(), e);
            throw new PointLocalizationException("Failed to determine location for the point", e);
        }
    }
}
