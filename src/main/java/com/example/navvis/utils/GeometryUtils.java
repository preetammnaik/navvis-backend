package com.example.navvis.utils;

import com.example.navvis.data.Point2D;

import java.util.List;

public final class GeometryUtils {

    private static final double EPS = 1e-9;

    /**
     * Determines whether a point (x, y) lies inside a polygon using the
     * ray-casting algorithm.
     * <p>
     * The polygon must contain at least three points and must be properly ordered
     * (clockwise or counter-clockwise). The last vertex implicitly connects
     * back to the first.
     *
     * @param x       the x-coordinate of the test point
     * @param y       the y-coordinate of the test point
     * @param polygon the list of polygon vertices, in order
     * @return {@code true} if the point lies inside the polygon, otherwise {@code false}
     */
    public static boolean isPointInPolygon(double x, double y, List<Point2D> polygon) {
        if (polygon == null || polygon.size() < 3) return false;

        boolean inside = false;
        Point2D prev = polygon.getLast();

        for (Point2D current : polygon) {
            double ax = prev.getX();
            double ay = prev.getY();
            double bx = current.getX();
            double by = current.getY();

            boolean rayBetweenYs = (ay > y && by <= y) || (by > y && ay <= y);
            if (rayBetweenYs) {
                double slope = by - ay;
                if (Math.abs(slope) > EPS) {
                    double intersectX = ax + (y - ay) * (bx - ax) / slope;
                    if (x < intersectX) inside = !inside;
                }
            }

            prev = current;
        }

        return inside;
    }
}
