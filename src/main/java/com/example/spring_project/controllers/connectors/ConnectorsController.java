package com.example.spring_project.controllers.connectors;

import com.example.spring_project.controllers.connectors.AddRequest;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.services.ConnectorService;
import com.example.spring_project.utils.ResponseTemplate;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/connector")
public class ConnectorsController {

    private static final Logger log = LoggerFactory.getLogger(ConnectorsController.class);
    private final ConnectorService connectorService;

    public ConnectorsController(ConnectorService connectorService) {
        this.connectorService = connectorService;
    }


    @PostMapping("/add")
    public ResponseEntity<ResponseTemplate<Connector>> addConnector(@RequestBody @Valid AddRequest request){

        Connector added = connectorService.addConnector(request);

        return new ResponseEntity<>(new ResponseTemplate<>
                ("Connector added successfully", added), HttpStatus.OK);
    }

    @PutMapping("/reserve")
    public ResponseEntity<ResponseTemplate<Connector>> reserveConnector(@RequestBody @Valid ReserveRequest request){

        log.info("entered reserve");
        Connector reserved = connectorService.reserveConnector(request);

        return new ResponseEntity<>(new ResponseTemplate<>
                ("Connector reserved successfully", reserved), HttpStatus.OK);
    }

}
