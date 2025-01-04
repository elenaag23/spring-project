package com.example.spring_project.controllers.stations;

import com.example.spring_project.models.entities.Station;
import com.example.spring_project.services.StationService;
import com.example.spring_project.utils.ResponseTemplate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/station")
public class StationsController {

    private final StationService stationService;

    public StationsController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseTemplate<Station>> addStation(@RequestBody @Valid AddRequest request){

        Station added = stationService.addStation(request);

        return new ResponseEntity<>(new ResponseTemplate<>
                ("Station added successfuly", added), HttpStatus.OK);
    }
}
