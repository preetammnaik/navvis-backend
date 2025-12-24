package com.example.navvis.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeightRange {

    private double min;
    private double max;

    public boolean contains(double z) {
        return z >= min && z <= max;
    }

    @JsonCreator
    public static HeightRange fromArray(double[] array) {
        if (array.length != 2) {
            throw new IllegalArgumentException("HeightRange array must have exactly 2 elements");
        }
        return new HeightRange(array[0], array[1]);
    }
}

