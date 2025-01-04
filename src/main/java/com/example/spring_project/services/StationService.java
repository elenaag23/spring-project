package com.example.spring_project.services;

import com.example.spring_project.controllers.stations.AddRequest;
import com.example.spring_project.controllers.stations.CoordinatesDto;
import com.example.spring_project.models.entities.Station;
import com.example.spring_project.repositories.StationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StationService {
    private static final Logger log = LoggerFactory.getLogger(StationService.class);
    private final StationRepository stationRepository;

    private static final String NOMINATIM_API_URL = "https://nominatim.openstreetmap.org/search?q=";
    private static final String ENDLINE = "&format=json&addressdetails=1";

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station addStation(@Valid AddRequest request){
        log.info("entered add station");
        try{
            log.info("entered try");
            CoordinatesDto coordinatesDto = getCoordinates(request.getAddress());
            log.info("coordinates: " + coordinatesDto);
            Station station = new Station(request.getStationName(), request.getStatus().toString(), request.getAddress());
            station.setLatitude(coordinatesDto.getLatitude());
            station.setLongitude(coordinatesDto.getLongitude());
            return stationRepository.save(station);
        } catch (Exception e){
            throw new IllegalArgumentException("Incorrect station data");
        }
    }

    public CoordinatesDto getCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();
        String url = NOMINATIM_API_URL + address.replace(" ", "+") + ENDLINE;

        try {
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);

            if (jsonNode.isArray() && jsonNode.size() > 0) {
                JsonNode firstResult = jsonNode.get(0);
                double lat = firstResult.get("lat").asDouble();
                double lon = firstResult.get("lon").asDouble();
                return new CoordinatesDto(lat, lon);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("No coordinates found for this address");
        }

        return null;
    }

}
