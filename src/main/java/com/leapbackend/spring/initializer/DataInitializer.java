package com.leapbackend.spring.initializer;

import com.leapbackend.spring.models.ERole;
import com.leapbackend.spring.models.Role;
import com.leapbackend.spring.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if the roles table is empty
        if (roleRepository.count() == 0) {
            // Insert initial roles
            Role ownerRole = new Role(ERole.ROLE_OWNER);
            Role managerRole = new Role(ERole.ROLE_MANAGER);
            Role customerRole = new Role(ERole.ROLE_CUSTOMER);

            roleRepository.save(ownerRole);
            roleRepository.save(managerRole);
            roleRepository.save(customerRole);

            System.out.println("Initial roles inserted into the database.");
        } else {
            System.out.println("Roles table already contains data.");
        }
    }
}