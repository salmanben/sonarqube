package com.sqli.testapp.service;

import com.sqli.testapp.dto.ClientDto;
import com.sqli.testapp.exception.EmailAlreadyExistsException;
import com.sqli.testapp.mapper.ClientMapper;
import com.sqli.testapp.model.Client;
import com.sqli.testapp.model.Role;
import com.sqli.testapp.repository.ClientRepository;
import com.sqli.testapp.service.imp.ClientServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImp clientService;

    @Test
    public void testGetAllClients() {
        List<Client> clients = List.of(new Client(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT));
        List<ClientDto> clientDtos = List.of(new ClientDto(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT));

        when(clientRepository.findAll()).thenReturn(clients);
        when(clientMapper.toDto(any(Client.class))).thenReturn(clientDtos.get(0));

        List<ClientDto> result = clientService.getAllClients();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Salman", result.get(0).getName());
    }

    @Test
    public void testGetClientById() {
        Client client = new Client(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT);
        ClientDto clientDto = new ClientDto(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT);

        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.getClientById(1);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Salman", result.getName());
    }

    @Test
    public void testGetClientByIdNotFound() {
        when(clientRepository.findById(2)).thenReturn(Optional.empty());
        ClientDto result = clientService.getClientById(2);
        Assertions.assertNull(result);
    }

    @Test
    public void testStoreClient() {
        ClientDto clientDto = new ClientDto("Salman", "salman@example.com", "password", 27, Role.CLIENT);
        Client client = new Client(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT);
        ClientDto expectedDto = new ClientDto(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT);

        when(clientRepository.findByEmail(clientDto.getEmail())).thenReturn(null);
        when(clientMapper.dtoRequestToEntity(clientDto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(expectedDto);

        ClientDto result = clientService.storeClient(clientDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Salman", result.getName());
    }

    @Test
    public void testStoreClientWithExistingEmail() {
        ClientDto clientDto = new ClientDto("Salman", "salman@example.com", "password", 27, Role.CLIENT);
        Client existingClient = new Client(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT);

        when(clientRepository.findByEmail(clientDto.getEmail())).thenReturn(existingClient);

        EmailAlreadyExistsException exception = Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
            clientService.storeClient(clientDto);
        });
        Assertions.assertEquals("Email already exists.", exception.getMessage());
    }

    @Test
    public void testUpdateClient() {
        int id = 1;
        ClientDto clientDto = new ClientDto(1, "Salman Updated", "salman.updated@example.com", "newpassword", 28, Role.CLIENT);
        Client existingClient = new Client(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT);
        ClientDto expectedDto = new ClientDto(1, "Salman Updated", "salman.updated@example.com", "newpassword", 28, Role.CLIENT);

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.findByEmail(clientDto.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(clientDto.getPassword())).thenReturn("newpassword");

        existingClient.setName(clientDto.getName());
        existingClient.setEmail(clientDto.getEmail());
        existingClient.setPassword("newpassword");
        existingClient.setAge(clientDto.getAge());
        existingClient.setRole(clientDto.getRole());

        when(clientRepository.save(existingClient)).thenReturn(existingClient);
        when(clientMapper.toDto(existingClient)).thenReturn(expectedDto);

        ClientDto result = clientService.updateClient(id, clientDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Salman Updated", result.getName());
        Assertions.assertEquals("salman.updated@example.com", result.getEmail());
    }


    @Test
    public void testUpdateClientWithExistingEmail() {
        int id = 1;
        ClientDto clientDto = new ClientDto(1, "Salman Updated", "salman.updated@example.com", "newpassword", 28, Role.CLIENT);
        Client existingClient = new Client(1, "Salman", "salman@example.com", "password", 27, Role.CLIENT);
        Client anotherClient = new Client(2, "Ahmed", "salman.updated@example.com", "password", 30, Role.CLIENT);

        when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        when(clientRepository.findByEmail(clientDto.getEmail())).thenReturn(anotherClient);

        EmailAlreadyExistsException exception = Assertions.assertThrows(EmailAlreadyExistsException.class, () -> {
            clientService.updateClient(id, clientDto);
        });
        Assertions.assertEquals("Email already exists.", exception.getMessage());
    }

    @Test
    public void testUpdateClientNotFound() {
        int id = 1;
        ClientDto clientDto = new ClientDto(1, "Salman Updated", "salman.updated@example.com", "newpassword", 28, Role.CLIENT);

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            clientService.updateClient(id, clientDto);
        });
        Assertions.assertEquals("Client not found with id: " + id, exception.getMessage());
    }

    @Test
    public void testRemoveClient() {
        int id = 1;
        Client client = new Client();
        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).deleteById(id);

        boolean result = clientService.removeClient(id);

        verify(clientRepository, times(1)).deleteById(id);
        Assertions.assertTrue(result);
    }

    @Test
    public void testRemoveClientNotFound() {
        int id = 100;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = clientService.removeClient(id);

        verify(clientRepository, times(0)).deleteById(id);
        Assertions.assertFalse(result);
    }





}
