package com.example.navvis.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDto {
    private String building;
    private String floor;
    private boolean found;
}
