package com.sqli.testapp.controller;

import com.sqli.testapp.dto.ClientDto;
import com.sqli.testapp.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    private final ClientService clientService;

    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";
    private static final String SUCCESS = "success";

    @GetMapping
    public List<ClientDto> getAll() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getById(@PathVariable int id) {
        ClientDto clientDto =  clientService.getClientById(id);
        if(clientDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(clientDto);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> store(@Valid @RequestBody ClientDto clientDto, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            response.put(STATUS, ERROR);
            response.put(MESSAGE, errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ClientDto savedClientDto = clientService.storeClient(clientDto);
        response.put(STATUS, SUCCESS);
        response.put(MESSAGE, savedClientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody ClientDto clientDto, BindingResult bindingResult,
                                                      @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            response.put(STATUS, ERROR);
            response.put(MESSAGE, errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ClientDto updatedClientDto = clientService.updateClient(id, clientDto);
        response.put(STATUS, SUCCESS);
        response.put(MESSAGE, updatedClientDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean clientRemoved = clientService.removeClient(id);
        if (clientRemoved) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
