package com.example.navvis.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Floor {
    private String name;
    private List<Point2D> outline;
    private HeightRange height;
}
