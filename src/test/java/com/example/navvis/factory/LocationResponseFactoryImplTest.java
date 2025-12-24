package com.example.navvis.factory;

import com.example.navvis.data.dto.LocationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationResponseFactoryImplTest {

    private LocationResponseFactoryImpl locationResponseFactory;

    @BeforeEach
    void setUp() {
        locationResponseFactory = new LocationResponseFactoryImpl();
    }

    @Test
    void found_ShouldCreateResponseWithBuildingAndFloor() {
        // arrange
        String buildingName = "Office Tower";
        String floorName = "Floor 3";

        // act
        LocationResponseDto result = locationResponseFactory.found(buildingName, floorName);

        // assert
        assertNotNull(result);
        assertEquals(buildingName, result.getBuilding());
        assertEquals(floorName, result.getFloor());
        assertTrue(result.isFound());
    }

    @Test
    void found_ShouldHandleEmptyStrings() {
        // arrange
        String buildingName = "";
        String floorName = "";

        // act
        LocationResponseDto result = locationResponseFactory.found(buildingName, floorName);

        // assert
        assertNotNull(result);
        assertEquals(buildingName, result.getBuilding());
        assertEquals(floorName, result.getFloor());
        assertTrue(result.isFound());
    }

    @Test
    void foundInBuildingOnly_ShouldCreateResponseWithBuildingOnly() {
        // arrange
        String buildingName = "Shopping Mall";

        // act
        LocationResponseDto result = locationResponseFactory.foundInBuildingOnly(buildingName);

        // assert
        assertNotNull(result);
        assertEquals(buildingName, result.getBuilding());
        assertNull(result.getFloor());
        assertTrue(result.isFound());
    }

    @Test
    void foundInBuildingOnly_ShouldHandleNullBuilding() {
        // arrange
        String buildingName = null;

        // act
        LocationResponseDto result = locationResponseFactory.foundInBuildingOnly(buildingName);

        // assert
        assertNotNull(result);
        assertNull(result.getBuilding());
        assertNull(result.getFloor());
        assertTrue(result.isFound());
    }

    @Test
    void notFound_ShouldCreateResponseWithNotFoundStatus() {
        // act
        LocationResponseDto result = locationResponseFactory.notFound();

        // assert
        assertNotNull(result);
        assertNull(result.getBuilding());
        assertNull(result.getFloor());
        assertFalse(result.isFound());
    }

    @Test
    void notFound_ShouldReturnDifferentInstanceEachTime() {
        // act
        LocationResponseDto result1 = locationResponseFactory.notFound();
        LocationResponseDto result2 = locationResponseFactory.notFound();

        // assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2); // Should be different instances
        assertEquals(result1.getBuilding(), result2.getBuilding());
        assertEquals(result1.getFloor(), result2.getFloor());
        assertEquals(result1.isFound(), result2.isFound());
    }

    @Test
    void found_ShouldReturnDifferentInstanceEachTime() {
        // arrange
        String buildingName = "Test Building";
        String floorName = "Test Floor";

        // act
        LocationResponseDto result1 = locationResponseFactory.found(buildingName, floorName);
        LocationResponseDto result2 = locationResponseFactory.found(buildingName, floorName);

        // assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2); // Should be different instances
        assertEquals(result1.getBuilding(), result2.getBuilding());
        assertEquals(result1.getFloor(), result2.getFloor());
        assertEquals(result1.isFound(), result2.isFound());
    }

    @Test
    void found_ShouldWorkWithSpecialCharacters() {
        // arrange
        String buildingName = "Building-123_Test";
        String floorName = "Floor 2.5 (Mezzanine)";

        // act
        LocationResponseDto result = locationResponseFactory.found(buildingName, floorName);

        // assert
        assertNotNull(result);
        assertEquals(buildingName, result.getBuilding());
        assertEquals(floorName, result.getFloor());
        assertTrue(result.isFound());
    }

    @Test
    void foundInBuildingOnly_ShouldWorkWithSpecialCharacters() {
        // arrange
        String buildingName = "Building @#$%^&*()_+Test";

        // act
        LocationResponseDto result = locationResponseFactory.foundInBuildingOnly(buildingName);

        // assert
        assertNotNull(result);
        assertEquals(buildingName, result.getBuilding());
        assertNull(result.getFloor());
        assertTrue(result.isFound());
    }
}