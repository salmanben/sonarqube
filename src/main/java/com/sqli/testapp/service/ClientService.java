package com.sqli.testapp.service;

import com.sqli.testapp.dto.ClientDto;

import java.util.List;

public interface ClientService {
    List<ClientDto> getAllClients();
    ClientDto getClientById(int id);
    ClientDto storeClient(ClientDto clientDto);
    ClientDto updateClient(int id, ClientDto clientDto);
    void removeClient(int id);

}
