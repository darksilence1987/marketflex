package org.xhite.marketflex.config;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.xhite.marketflex.model.AppUser;
import org.xhite.marketflex.model.Category;
import org.xhite.marketflex.model.Product;
import org.xhite.marketflex.model.enums.Role;
import org.xhite.marketflex.repository.CategoryRepository;
import org.xhite.marketflex.repository.ProductRepository;
import org.xhite.marketflex.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!prod")
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            createAdminUser();
            createTestManager();
            createTestCustomer();
        }

         // Create categories
        Category electronics = new Category();
        electronics.setName("Electronics");
        electronics.setDescription("Electronic gadgets and devices");
        electronics.setImageUrl("/images/categories/electronics.jpg");

        Category fashion = new Category();
        fashion.setName("Fashion");
        fashion.setDescription("Latest fashion trends");
        fashion.setImageUrl("/images/categories/fashion.jpg");

        categoryRepository.saveAll(Arrays.asList(electronics, fashion));

        // Create products
        Product smartphone = new Product();
        smartphone.setName("Smartphone");
        smartphone.setDescription("Latest model smartphone with advanced features");
        smartphone.setPrice(new BigDecimal("699.99"));
        smartphone.setStockQuantity(100);
        smartphone.setCategory(electronics);
        smartphone.setImageUrl("/images/products/smartphone.jpg");

        Product tshirt = new Product();
        tshirt.setName("T-Shirt");
        tshirt.setDescription("Comfortable cotton t-shirt");
        tshirt.setPrice(new BigDecimal("19.99"));
        tshirt.setStockQuantity(200);
        tshirt.setCategory(fashion);
        tshirt.setImageUrl("/images/products/tshirt.jpg");

        productRepository.saveAll(Arrays.asList(smartphone, tshirt));

    }

    private void createAdminUser() {
        AppUser admin = AppUser.builder()
                .email("admin@marketflex.com")
                .password(passwordEncoder.encode("Admin123!"))
                .firstName("Admin")
                .lastName("User")
                .roles(Set.of(Role.ADMIN))
                .enabled(true)
                .accountNonLocked(true)
                .build();
        
        userRepository.save(admin);
        log.info("Admin user created");
    }

    private void createTestManager() {
        AppUser manager = AppUser.builder()
                .email("manager@marketflex.com")
                .password(passwordEncoder.encode("Manager123!"))
                .firstName("Manager")
                .lastName("User")
                .roles(Set.of(Role.MANAGER))
                .enabled(true)
                .accountNonLocked(true)
                .build();
        
        userRepository.save(manager);
        log.info("Manager user created");
    }

    private void createTestCustomer() {
        AppUser customer = AppUser.builder()
                .email("customer@marketflex.com")
                .password(passwordEncoder.encode("Customer123!"))
                .firstName("Customer")
                .lastName("User")
                .roles(Set.of(Role.CUSTOMER))
                .enabled(true)
                .accountNonLocked(true)
                .build();
        
        userRepository.save(customer);
        log.info("Customer user created");
    }
}