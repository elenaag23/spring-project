package com.example.spring_project.controllers.connectors;

import com.example.spring_project.controllers.connectors.AddRequest;
import com.example.spring_project.models.entities.Connector;
import com.example.spring_project.services.ConnectorService;
import com.example.spring_project.utils.ResponseTemplate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/connector")
public class ConnectorsController {

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
}
