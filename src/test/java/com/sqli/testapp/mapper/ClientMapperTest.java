package com.sqli.testapp.mapper;

import com.sqli.testapp.dto.ClientDto;
import com.sqli.testapp.model.Client;
import com.sqli.testapp.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class ClientMapperTest {
    private final ClientMapper mapper = new ClientMapper();

    @Test
    void testToDto() {
        Client client = new Client();
        client.setId(1);
        client.setName("John Doe");
        client.setAge(30);
        client.setEmail("john.doe@example.com");
        client.setCreatedAt(LocalDate.now());
        client.setUpdatedAt(LocalDate.now());
        client.setRole(Role.CLIENT);

        ClientDto dto = mapper.toDto(client);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(client.getId(), dto.getId());
        Assertions.assertEquals(client.getName(), dto.getName());
        Assertions.assertEquals(client.getAge(), dto.getAge());
        Assertions.assertEquals(client.getEmail(), dto.getEmail());
        Assertions.assertEquals(client.getCreatedAt(), dto.getCreatedAt());
        Assertions.assertEquals(client.getUpdatedAt(), dto.getUpdatedAt());
        Assertions.assertEquals(client.getRole(), dto.getRole());
    }

    @Test
    void testDtoRequestToEntity() {
        ClientDto dto = new ClientDto();
        dto.setName("Jane Smith");
        dto.setAge(25);
        dto.setEmail("jane.smith@example.com");
        dto.setRole(Role.ADMIN);

        Client client = mapper.dtoRequestToEntity(dto);

        Assertions.assertNotNull(client);
        Assertions.assertEquals(dto.getName(), client.getName());
        Assertions.assertEquals(dto.getAge(), client.getAge());
        Assertions.assertEquals(dto.getEmail(), client.getEmail());
        Assertions.assertEquals(dto.getRole(), client.getRole());

        Assertions.assertEquals(client.getId(), 0);
        Assertions.assertNull(client.getCreatedAt());
        Assertions.assertNull(client.getUpdatedAt());
    }
}
