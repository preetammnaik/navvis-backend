package com.example.navvis.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point2D implements Serializable {
    @Serial
    private static final long serialVersionUID = 6727173706059176815L;
    private double x;
    private double y;

    @JsonCreator
    public Point2D(double[] array) {
        if (array.length != 2) {
            throw new IllegalArgumentException("Point2D array must have exactly 2 elements");
        }
        this.x = array[0];
        this.y = array[1];
    }
}
