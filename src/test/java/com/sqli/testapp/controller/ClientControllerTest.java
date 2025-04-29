package com.sqli.testapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqli.testapp.dto.ClientDto;
import com.sqli.testapp.model.Role;
import com.sqli.testapp.service.imp.ClientServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ClientServiceImp clientService;

    @Test
    public void testGetAll() throws Exception {
        ClientDto clientDto1 = new ClientDto(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT);
        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(clientDto1);

        when(clientService.getAllClients()).thenReturn(clientDtos);

        ResultActions response = mockMvc.perform(get("/api/v1/clients"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(clientDtos.size())))
                .andExpect(jsonPath("$[0].name", is(clientDtos.get(0).getName())))
                .andExpect(jsonPath("$[0].email", is(clientDtos.get(0).getEmail())));
    }

    @Test
    public void testGetById() throws Exception {
        ClientDto clientDto = new ClientDto(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT);

        when(clientService.getClientById(clientDto.getId())).thenReturn(clientDto);

        ResultActions response = mockMvc.perform(get("/api/v1/clients/" + clientDto.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(clientDto.getId())))
                .andExpect(jsonPath("$.name", is(clientDto.getName())))
                .andExpect(jsonPath("$.email", is(clientDto.getEmail())));

    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        int notExistingId = 100000000;

        when(clientService.getClientById(notExistingId)).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/api/v1/clients/" + notExistingId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void testStore() throws Exception {
        ClientDto clientRequestDto = new ClientDto();
        clientRequestDto.setName("Salman");
        clientRequestDto.setEmail("salman@gmail.com");
        clientRequestDto.setPassword("12345");
        clientRequestDto.setAge(28);
        clientRequestDto.setRole(Role.CLIENT);

        ClientDto savedClientDto = new ClientDto(1, "Salman", "salman@example.com", "12345", 28, Role.CLIENT, LocalDate.now(), LocalDate.now());

        when(clientService.storeClient(clientRequestDto)).thenReturn(savedClientDto);

        ResultActions response = mockMvc.perform(post("/api/v1/clients").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientRequestDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message.id", is(savedClientDto.getId())))
                .andExpect(jsonPath("$.message.name", is(savedClientDto.getName())))
                .andExpect(jsonPath("$.message.email", is(savedClientDto.getEmail())))
                .andExpect(jsonPath("$.message.age", is(savedClientDto.getAge())))
                .andExpect(jsonPath("$.message.role", is(savedClientDto.getRole().name())))
                .andExpect(jsonPath("$.message.createdAt", is(savedClientDto.getCreatedAt().toString())))
                .andExpect(jsonPath("$.message.updatedAt", is(savedClientDto.getUpdatedAt().toString())));
    }

    @Test
    public void testUpdate() throws Exception {
        int id = 1;
        ClientDto clientRequestDto = new ClientDto();
        clientRequestDto.setName("Salman Ben omar");
        clientRequestDto.setEmail("salman.new@gmail.com");
        clientRequestDto.setPassword("1234567");
        clientRequestDto.setAge(28);
        clientRequestDto.setRole(Role.CLIENT);

        ClientDto updatedClientDto = new ClientDto(1, "Salman Ben omar", "salman.new@gmail.com", "1234567", 28, Role.CLIENT, LocalDate.of(2025, 04, 28), LocalDate.now());

        when(clientService.updateClient(id, clientRequestDto)).thenReturn(updatedClientDto);

        ResultActions response = mockMvc.perform(put("/api/v1/clients/" + id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientRequestDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message.id", is(updatedClientDto.getId())))
                .andExpect(jsonPath("$.message.name", is(updatedClientDto.getName())))
                .andExpect(jsonPath("$.message.email", is(updatedClientDto.getEmail())))
                .andExpect(jsonPath("$.message.age", is(updatedClientDto.getAge())))
                .andExpect(jsonPath("$.message.role", is(updatedClientDto.getRole().name())))
                .andExpect(jsonPath("$.message.createdAt", is(updatedClientDto.getCreatedAt().toString())))
                .andExpect(jsonPath("$.message.updatedAt", is(updatedClientDto.getUpdatedAt().toString())));

    }

    @Test
    public void testUpdateNotFound() throws Exception {
        int notExistingId = 1000000000;
        ClientDto clientRequestDto = new ClientDto();
        clientRequestDto.setName("Salman Ben omar");
        clientRequestDto.setEmail("salman.new@gmail.com");
        clientRequestDto.setPassword("1234567");
        clientRequestDto.setAge(28);
        clientRequestDto.setRole(Role.CLIENT);


        when(clientService.updateClient(notExistingId, clientRequestDto)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(put("/api/v1/clients/" + notExistingId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientRequestDto)));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("error")));
    }

    @Test
    public void testDelete() throws Exception {
        int id = 1;

        when(clientService.removeClient(id)).thenReturn(true);

        ResultActions response = mockMvc.perform(delete("/api/v1/clients/" + id));

        response.andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        int notExistingId = 100000000;

        when(clientService.removeClient(notExistingId)).thenReturn(false);

        ResultActions response = mockMvc.perform(delete("/api/v1/clients/" + notExistingId));

        response.andExpect(status().isNotFound());
    }
}
