package com.sqli.testapp.repository;

import com.sqli.testapp.model.Client;
import com.sqli.testapp.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    @Rollback(value = true)
    public void testFindByEmail() {
        Client client = new Client();
        client.setName("Salman Ben Omar");
        client.setEmail("salman1112@example.com");
        client.setPassword("securepassword");
        client.setAge(27);
        client.setRole(Role.CLIENT);

        clientRepository.save(client);

        Client foundClient = clientRepository.findByEmail("salman1112@example.com");

        Assertions.assertNotNull(foundClient);
        Assertions.assertEquals("Salman Ben Omar", foundClient.getName());
    }
}
