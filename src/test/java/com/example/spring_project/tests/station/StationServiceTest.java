package com.example.spring_project.tests.station;

import com.example.spring_project.controllers.stations.AddRequest;
import com.example.spring_project.controllers.stations.CoordinatesDto;
import com.example.spring_project.controllers.stations.GetByLocationRequest;
import com.example.spring_project.enums.StationStatus;
import com.example.spring_project.models.entities.Station;
import com.example.spring_project.repositories.StationRepository;
import com.example.spring_project.services.StationService;
import com.example.spring_project.utils.HaversineUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Spy
    @InjectMocks
    private StationService stationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addStation_Success() {
        // Arrange
        AddRequest request = new AddRequest();
        request.setStationName("Test Station");
        request.setAddress("123 Test St");
        request.setStatus(StationStatus.valueOf("AVAILABLE"));

        CoordinatesDto coordinatesDto = new CoordinatesDto(40.7128, -74.0060);

        Station station = new Station("Test Station", "AVAILABLE", "123 Test St");
        station.setLatitude(coordinatesDto.getLatitude());
        station.setLongitude(coordinatesDto.getLongitude());

        when(stationRepository.save(any(Station.class))).thenReturn(station);
        doReturn(coordinatesDto).when(stationService).getCoordinates(request.getAddress());

        // Act
        Station result = stationService.addStation(request);

        // Assert
        assertNotNull(result);
        assertEquals("Test Station", result.getStationName());
        assertEquals("123 Test St", result.getAddress());
        assertEquals(40.7128, result.getLatitude());
        assertEquals(-74.0060, result.getLongitude());
        verify(stationRepository, times(1)).save(any(Station.class));
    }

    @Test
    void addStation_InvalidAddress() {
        // Arrange
        AddRequest request = new AddRequest();
        request.setStationName("Test Station");
        request.setAddress("Invalid Address");
        request.setStatus(StationStatus.valueOf("AVAILABLE"));

        doThrow(new IllegalArgumentException("No coordinates found for this address"))
                .when(stationService).getCoordinates(request.getAddress());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> stationService.addStation(request));
        verify(stationRepository, never()).save(any(Station.class));
    }

    @Test
    void getStations_ByLocation_Success() {
        // Arrange
        GetByLocationRequest request = new GetByLocationRequest();
        request.setLatitude(40.7128); // New York City
        request.setLongitude(-74.0060);
        request.setRadius(10.0); // 10 km radius

        List<Station> stations = new ArrayList<>();
        Station station1 = new Station("Station 1", "ACTIVE", "123 Test St");
        station1.setLatitude(40.7128); // Same as request location
        station1.setLongitude(-74.0060);
        Station station2 = new Station("Station 2", "ACTIVE", "456 Test Ave");
        station2.setLatitude(40.7306); // Within 10 km
        station2.setLongitude(-73.9352);
        Station station3 = new Station("Station 3", "ACTIVE", "789 Test Blvd");
        station3.setLatitude(41.7128); // Far away (outside 10 km)
        station3.setLongitude(-74.0060);

        stations.add(station1);
        stations.add(station2);
        stations.add(station3);

        when(stationRepository.findAll()).thenReturn(stations);

        // Act
        List<Station> result = stationService.getStations(request);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // Only station1 and station2 should be in the result
        assertTrue(result.contains(station1));
        assertTrue(result.contains(station2));
        assertFalse(result.contains(station3));

        verify(stationRepository, times(1)).findAll();
    }


    @Test
    void getStations_NoLocationRequest() {
        // Arrange
        List<Station> stations = new ArrayList<>();
        Station station1 = new Station("Station 1", "AVAILABLE", "123 Test St");
        stations.add(station1);

        when(stationRepository.findAll()).thenReturn(stations);

        // Act
        List<Station> result = stationService.getStations(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stationRepository, times(1)).findAll();
    }
}
