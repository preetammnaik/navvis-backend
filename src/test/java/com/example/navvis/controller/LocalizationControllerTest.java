package com.example.navvis.controller;

import com.example.navvis.data.Point3D;
import com.example.navvis.data.dto.LocationResponseDto;
import com.example.navvis.exceptions.PointLocalizationException;
import com.example.navvis.services.CoordinationLocalizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LocalizationControllerTest {

    LocalizationController localizationController;
    CoordinationLocalizationService mockCoordinationLocalizationService;
    Point3D testPoint;
    LocationResponseDto testResponse;


    @BeforeEach
    void setUp() {
        mockCoordinationLocalizationService = mock(CoordinationLocalizationService.class);
        localizationController = new LocalizationController(mockCoordinationLocalizationService);

        testPoint = new Point3D(10.0, 20.0, 5.0);
        testResponse = new LocationResponseDto("BuildingA", "Floor1", true);
    }

    @Test
    void localizePoint_Success() {
        // arrange
        when(mockCoordinationLocalizationService.localizePoint(any())).thenReturn(testResponse);

        // act
        ResponseEntity<LocationResponseDto> response = localizationController.localizePoint(testPoint);

        // assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testResponse, response.getBody());
        verify(mockCoordinationLocalizationService, times(1)).localizePoint(any());
    }

    @Test
    void localizePoint_ThrowsException() {
        // arrange
        when(mockCoordinationLocalizationService.localizePoint(any())).thenThrow(new RuntimeException("Error!!!!"));

        // act + assert
        assertThrows(PointLocalizationException.class, () -> localizationController.localizePoint(testPoint));
        verify(mockCoordinationLocalizationService, times(1)).localizePoint(any());
    }

}