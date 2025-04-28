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

    @GetMapping("")
    public List<ClientDto> getAll() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable int id) {
        return clientService.getClientById(id);
    }

    @PostMapping("/store")
    public ResponseEntity<Map<String, Object>> store(@Valid @RequestBody ClientDto clientDto, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            response.put("status", "error");
            response.put("message", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ClientDto savedClientDto = clientService.storeClient(clientDto);
        response.put("status", "success");
        response.put("message", savedClientDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody ClientDto clientDto, BindingResult bindingResult,
                                                      @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            response.put("status", "error");
            response.put("message", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        ClientDto updatedClientDto = clientService.updateClient(id, clientDto);
        response.put("status", "success");
        response.put("message", updatedClientDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        clientService.removeClient(id);
    }
}
