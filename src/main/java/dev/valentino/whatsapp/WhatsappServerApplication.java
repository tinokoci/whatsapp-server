package dev.valentino.whatsapp;

import dev.valentino.whatsapp.user.UserRepository;
import dev.valentino.whatsapp.user.WapUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class WhatsappServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsappServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.count() < 1) {
                userRepository.save(WapUser.builder()
                        .username("tinokoci")
                        .fullName("Valentino K.")
                        .email("valentino@whatsapp.com")
                        .password(encoder.encode("whatsapp"))
                        .build());
                userRepository.save(WapUser.builder()
                        .username("tinodi")
                        .fullName("Valentino K.")
                        .email("tinodi@whatsapp.com")
                        .password(encoder.encode("whatsapp"))
                        .build());
                userRepository.save(WapUser.builder()
                        .username("nadrlja")
                        .fullName("Valentino K.")
                        .email("nadrlja@whatsapp.com")
                        .password(encoder.encode("whatsapp"))
                        .build());
            }
        };
    }
}

