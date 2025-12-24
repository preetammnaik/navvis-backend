package com.example.navvis.utils;

import com.example.navvis.data.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeometryUtilsTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void isPointInPolygon_PointInsideSquare_ShouldReturnTrue() {
        // arrange
        List<Point2D> outline = List.of(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        // act + assert
        assertTrue(GeometryUtils.isPointInPolygon(5.0, 5.0, outline));
    }

    @Test
    void isPointInPolygon_PointOutsideSquare_ShouldReturnFalse() {
        // arrange
        List<Point2D> outline = List.of(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        // act + assert
        assertFalse(GeometryUtils.isPointInPolygon(20.0, 20.0, outline));
    }

    @Test
    void isPointInPolygon_PointOnBoundary_ShouldReturnTrue() {
        List<Point2D> outline = List.of(new Point2D(0.0, 0.0), new Point2D(10.0, 0.0), new Point2D(10.0, 10.0), new Point2D(0.0, 10.0), new Point2D(0.0, 0.0));

        // act + assert
        assertTrue(GeometryUtils.isPointInPolygon(0.0, 0.0, outline)); // vertex
        assertTrue(GeometryUtils.isPointInPolygon(5.0, 0.0, outline)); // edge
    }

    @Test
    void isPointInPolygon_NullOrTooSmallPolygon_ShouldReturnFalse() {
        // arrange

        // act + assert
        assertFalse(GeometryUtils.isPointInPolygon(1.0, 1.0, null));
        assertFalse(GeometryUtils.isPointInPolygon(1.0, 1.0, List.of()));
        assertFalse(GeometryUtils.isPointInPolygon(1.0, 1.0, List.of(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0))));
    }
}