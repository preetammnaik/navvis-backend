package com.example.navvis.services;

import com.example.navvis.data.Point3D;
import com.example.navvis.data.dto.LocationResponseDto;

public interface CoordinationLocalizationService {

    LocationResponseDto localizePoint(Point3D point);
}
