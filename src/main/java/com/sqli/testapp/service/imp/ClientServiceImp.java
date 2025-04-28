package com.sqli.testapp.service.imp;

import com.sqli.testapp.dto.ClientDto;
import com.sqli.testapp.mapper.ClientMapper;
import com.sqli.testapp.model.Client;
import com.sqli.testapp.repository.ClientRepository;
import com.sqli.testapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ClientServiceImp implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable(value="allProducts")
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream().map(clientMapper::toDto).collect(Collectors.toList());
    }

    @Cacheable(value="products", key="#id")
    @Transactional(readOnly = true)
    @Override
    public ClientDto getClientById(int id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (!clientOptional.isPresent()) {
            return null;
        }
        return clientMapper.toDto(clientOptional.get());
    }

    @CacheEvict(value="allProducts", allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED, timeout = 30, isolation = Isolation.READ_COMMITTED)
    @Override
    public ClientDto storeClient(ClientDto clientDto) {
        Client client = clientMapper.dtoRequestToEntity(clientDto);
        if (doesEmailExist(clientDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }
        client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @CacheEvict(value="allProducts", allEntries = true)
    @CachePut(value="products", key="#id")
    @Override
    public ClientDto updateClient(int id, ClientDto clientDto) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if(!clientOptional.isPresent()){
            throw new IllegalArgumentException("Client not found with id: " + id);
        }
        Client client = clientOptional.get();
        if (doesEmailExist(clientDto.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists.");
        }
        client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
        client.setEmail(clientDto.getEmail());
        client.setRole(clientDto.getRole());
        client.setAge(clientDto.getAge());
        client.setName(clientDto.getName());
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Override

    @Caching(evict={@CacheEvict(value="allProducts", allEntries = true),
            @CacheEvict(value="products", key="#id")})

    public void removeClient(int id) {
        clientRepository.deleteById(id);
    }

    private boolean doesEmailExist(String email) {
        Client existingClient = clientRepository.findByEmail(email);
        return existingClient != null;
    }

    private boolean doesEmailExist(String email, int id) {
        Client existingClient = clientRepository.findByEmail(email);
        return existingClient != null && existingClient.getId() != id;
    }


}
