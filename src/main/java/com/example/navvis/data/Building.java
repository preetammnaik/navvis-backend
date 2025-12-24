package com.example.navvis.data;

import lombok.Data;

import java.util.List;

@Data
public class Building {
    private String name;
    private List<Point2D> outline;
    private HeightRange height;
    private List<Floor> floors;
}
