package com.example.spring_project.controllers.users;

import com.example.spring_project.services.UserService;
import com.example.spring_project.utils.ResponseTemplate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/edit")
public class EditController {

    private final UserService userService;

    public EditController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<ResponseTemplate<EditResponse>> edit(@RequestBody @Valid EditRequest request){

        EditResponse editResponse = userService.edit(request);

        return new ResponseEntity<>(new ResponseTemplate<>
                ("Edit successful", editResponse), HttpStatus.OK);
    }
}
