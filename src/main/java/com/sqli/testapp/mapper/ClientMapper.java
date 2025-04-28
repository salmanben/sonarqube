package com.sqli.testapp.mapper;

import com.sqli.testapp.dto.ClientDto;
import com.sqli.testapp.model.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDto toDto(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setAge(client.getAge());
        clientDto.setEmail(client.getEmail());
        clientDto.setCreatedAt(client.getCreatedAt());
        clientDto.setUpdatedAt(client.getUpdatedAt());
        clientDto.setRole(client.getRole());
        return  clientDto;
    }

    public Client dtoRequestToEntity(ClientDto clientDto){
        Client client = new Client();
        client.setName(clientDto.getName());
        client.setAge(clientDto.getAge());
        client.setEmail(clientDto.getEmail());
        client.setRole(clientDto.getRole());
        return client;
    }
}
