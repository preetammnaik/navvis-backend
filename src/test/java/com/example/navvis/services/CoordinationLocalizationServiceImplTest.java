package com.example.navvis.services;

import com.example.navvis.data.*;
import com.example.navvis.data.dto.LocationResponseDto;
import com.example.navvis.factory.LocationResponseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoordinationLocalizationServiceImplTest {

    private CoordinationLocalizationService coordinationLocalizationService;
    private LocationResponseFactory mockLocationResponseFactory;

    @BeforeEach
    void setUp() {
        mockLocationResponseFactory = mock(LocationResponseFactory.class);
    }

    @Test
    void localizePoint_PointInsideBuildingAndFloor_ShouldReturnFound() {
        // arrange
        List<Point2D> buildingOutline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Floor floor = new Floor();
        floor.setName("Floor 1");
        floor.setOutline(buildingOutline);
        floor.setHeight(new HeightRange(0.0, 3.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(buildingOutline);
        building.setHeight(new HeightRange(0.0, 15.0));
        building.setFloors(List.of(floor));

        List<Building> buildings = List.of(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        Point3D point = new Point3D(5.0, 5.0, 2.0);
        LocationResponseDto expectedResponse = new LocationResponseDto("Test Building", "Floor 1", true);

        when(mockLocationResponseFactory.found("Test Building", "Floor 1")).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).found("Test Building", "Floor 1");
    }

    @Test
    void localizePoint_PointInsideBuildingButOutsideFloorZ_ShouldReturnFoundInBuildingOnly() {
        // arrange
        List<Point2D> buildingOutline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Floor floor = new Floor();
        floor.setName("Floor 1");
        floor.setOutline(buildingOutline);
        floor.setHeight(new HeightRange(0.0, 3.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(buildingOutline);
        building.setHeight(new HeightRange(0.0, 15.0));
        building.setFloors(Arrays.asList(floor));

        List<Building> buildings = Arrays.asList(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        // Point is in building XY but Z=4 is outside floor height
        Point3D point = new Point3D(5.0, 5.0, 4.0);
        LocationResponseDto expectedResponse = new LocationResponseDto("Test Building", null, true);

        when(mockLocationResponseFactory.foundInBuildingOnly("Test Building")).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).foundInBuildingOnly("Test Building");
    }

    @Test
    void localizePoint_PointOutsideBuildingXY_ShouldReturnNotFound() {
        // arrange
        List<Point2D> buildingOutline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(buildingOutline);
        building.setHeight(new HeightRange(0.0, 15.0));
        building.setFloors(Collections.emptyList());

        List<Building> buildings = Arrays.asList(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        // Point far outside building
        Point3D point = new Point3D(50.0, 50.0, 2.0);
        LocationResponseDto expectedResponse = new LocationResponseDto(null, null, false);

        when(mockLocationResponseFactory.notFound()).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).notFound();
    }

    @Test
    void localizePoint_PointInsideBuildingXYButOutsideBuildingZ_ShouldReturnNotFound() {
        // arrange
        List<Point2D> buildingOutline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(buildingOutline);
        building.setHeight(new HeightRange(0.0, 15.0));

        List<Building> buildings = Arrays.asList(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        // Point is in building XY but Z=20 is outside building height
        Point3D point = new Point3D(5.0, 5.0, 20.0);
        LocationResponseDto expectedResponse = new LocationResponseDto(null, null, false);

        when(mockLocationResponseFactory.notFound()).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).notFound();
    }

    @Test
    void localizePoint_PointInBuildingButNotInFloorOutline_ShouldReturnFoundInBuildingOnly() {
        // arrange
        List<Point2D> buildingOutline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));


        List<Point2D> floorOutline = Arrays.asList(new Point2D(2.0, 2.0), new Point2D(8.0, 2.0), new Point2D(8.0, 8.0), new Point2D(2.0, 8.0), new Point2D(2.0, 2.0));

        Floor floor = new Floor();
        floor.setName("Floor 1");
        floor.setOutline(floorOutline);
        floor.setHeight(new HeightRange(0.0, 3.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(buildingOutline);
        building.setHeight(new HeightRange(0.0, 15.0));
        building.setFloors(List.of(floor));

        List<Building> buildings = List.of(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        // Point at (1,1) is in building but outside floor outline
        Point3D point = new Point3D(1.0, 1.0, 2.0);
        LocationResponseDto expectedResponse = new LocationResponseDto("Test Building", null, true);

        when(mockLocationResponseFactory.foundInBuildingOnly("Test Building")).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).foundInBuildingOnly("Test Building");
    }

    @Test
    void localizePoint_MultipleBuildings_PointFoundInSecondBuilding() {
        // arrange
        List<Point2D> building1Outline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        List<Point2D> building2Outline = Arrays.asList(new Point2D(20.0, 20.0), new Point2D(30.0, 20.0), new Point2D(30.0, 30.0), new Point2D(20.0, 30.0), new Point2D(20.0, 20.0));

        Building building1 = new Building();
        building1.setName("Building 1");
        building1.setOutline(building1Outline);
        building1.setHeight(new HeightRange(0.0, 10.0));
        building1.setFloors(Collections.emptyList());

        Floor floor2 = new Floor();
        floor2.setName("Floor 1");
        floor2.setOutline(building2Outline);
        floor2.setHeight(new HeightRange(0.0, 3.0));

        Building building2 = new Building();
        building2.setName("Building 2");
        building2.setOutline(building2Outline);
        building2.setHeight(new HeightRange(0.0, 10.0));
        building2.setFloors(List.of(floor2));

        List<Building> buildings = Arrays.asList(building1, building2);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        // Point is in second building
        Point3D point = new Point3D(25.0, 25.0, 2.0);
        LocationResponseDto expectedResponse = new LocationResponseDto("Building 2", "Floor 1", true);

        when(mockLocationResponseFactory.found("Building 2", "Floor 1")).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).found("Building 2", "Floor 1");
    }

    @Test
    void localizePoint_EmptyBuildingsList_ShouldReturnNotFound() {
        // arrange
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(Collections.emptyList(), mockLocationResponseFactory);
        Point3D point = new Point3D(5.0, 5.0, 2.0);
        LocationResponseDto expectedResponse = new LocationResponseDto(null, null, false);

        when(mockLocationResponseFactory.notFound()).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).notFound();
    }

    @Test
    void localizePoint_BuildingWithNoFloors_PointInBuildingShouldReturnFoundInBuildingOnly() {
        // arrange
        List<Point2D> buildingOutline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(buildingOutline);
        building.setHeight(new HeightRange(0.0, 15.0));
        building.setFloors(Collections.emptyList());

        List<Building> buildings = List.of(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        Point3D point = new Point3D(5.0, 5.0, 2.0);
        LocationResponseDto expectedResponse = new LocationResponseDto("Test Building", null, true);

        when(mockLocationResponseFactory.foundInBuildingOnly("Test Building")).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).foundInBuildingOnly("Test Building");
    }

    @Test
    void localizePoint_BuildingWithMultipleFloors_PointFoundInSecondFloor() {
        // arrange
        List<Point2D> outline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Floor floor1 = new Floor();
        floor1.setName("Floor 1");
        floor1.setOutline(outline);
        floor1.setHeight(new HeightRange(0.0, 3.0));

        Floor floor2 = new Floor();
        floor2.setName("Floor 2");
        floor2.setOutline(outline);
        floor2.setHeight(new HeightRange(3.0, 6.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(outline);
        building.setHeight(new HeightRange(0.0, 15.0));
        building.setFloors(Arrays.asList(floor1, floor2));

        List<Building> buildings = Arrays.asList(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        // Point with Z=4 is in floor 2
        Point3D point = new Point3D(5.0, 5.0, 4.0);
        LocationResponseDto expectedResponse = new LocationResponseDto("Test Building", "Floor 2", true);

        when(mockLocationResponseFactory.found("Test Building", "Floor 2")).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).found("Test Building", "Floor 2");
    }

    @Test
    void localizePoint_PointExactlyOnBoundary_ShouldBeConsideredInside() {
        // arrange - Point exactly on the boundary of the polygon
        List<Point2D> outline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Floor floor = new Floor();
        floor.setName("Floor 1");
        floor.setOutline(outline);
        floor.setHeight(new HeightRange(0.0, 3.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(outline);
        building.setHeight(new HeightRange(0.0, 15.0));
        building.setFloors(List.of(floor));

        List<Building> buildings = List.of(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        // Point exactly on the boundary at (0,0)
        Point3D point = new Point3D(0.0, 0.0, 2.0);
        LocationResponseDto expectedResponse = new LocationResponseDto("Test Building", "Floor 1", true);

        when(mockLocationResponseFactory.found("Test Building", "Floor 1")).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).found("Test Building", "Floor 1");
    }

    @Test
    void localizePoint_BuildingWithNullHeight_ShouldNotCrash() {
        // arrange - Building with null height
        List<Point2D> outline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(outline);
        building.setHeight(null);
        building.setFloors(Collections.emptyList());

        List<Building> buildings = List.of(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        Point3D point = new Point3D(5.0, 5.0, 2.0);
        LocationResponseDto expectedResponse = new LocationResponseDto(null, null, false);

        when(mockLocationResponseFactory.notFound()).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).notFound();
    }

    @Test
    void localizePoint_FloorWithNullHeight_ShouldNotCrash() {
        // arrange
        List<Point2D> outline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Floor floor = new Floor();
        floor.setName("Floor 1");
        floor.setOutline(outline);
        floor.setHeight(null);

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(outline);
        building.setHeight(new HeightRange(0.0, 15.0));
        building.setFloors(List.of(floor));

        List<Building> buildings = List.of(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        Point3D point = new Point3D(5.0, 5.0, 2.0);
        // Point is in building but floor has null height, so should return foundInBuildingOnly
        LocationResponseDto expectedResponse = new LocationResponseDto("Test Building", null, true);

        when(mockLocationResponseFactory.foundInBuildingOnly("Test Building")).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).foundInBuildingOnly("Test Building");
    }

    @Test
    void localizePoint_PointOnZBoundary_ShouldBeConsideredInside() {
        // arrange
        List<Point2D> outline = Arrays.asList(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        Floor floor = new Floor();
        floor.setName("Floor 1");
        floor.setOutline(outline);
        floor.setHeight(new HeightRange(0.0, 3.0));

        Building building = new Building();
        building.setName("Test Building");
        building.setOutline(outline);
        building.setHeight(new HeightRange(0.0, 15.0));
        building.setFloors(List.of(floor));

        List<Building> buildings = List.of(building);
        coordinationLocalizationService = new CoordinationLocalizationServiceImpl(buildings, mockLocationResponseFactory);

        // Point exactly at Z=3
        Point3D point = new Point3D(5.0, 5.0, 3.0);
        LocationResponseDto expectedResponse = new LocationResponseDto("Test Building", "Floor 1", true);

        when(mockLocationResponseFactory.found("Test Building", "Floor 1")).thenReturn(expectedResponse);

        // act
        LocationResponseDto result = coordinationLocalizationService.localizePoint(point);

        // assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(mockLocationResponseFactory, times(1)).found("Test Building", "Floor 1");
    }
}