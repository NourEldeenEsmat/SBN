package com.nouresmat.book;

import com.nouresmat.book.auth.AuthServices;
import com.nouresmat.book.auth.RegisterRequest;
import com.nouresmat.book.role.Role;
import com.nouresmat.book.role.RoleRepository;
import com.nouresmat.book.user.User;
import com.nouresmat.book.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApplication.class, args);
	}
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository,
									AuthServices authServices,
									UserRepository userRepository
									){
		return args ->
		{
			if (roleRepository.findByName("USER").isEmpty()){
				roleRepository.save(Role.builder().name("USER").build());
			}
//			for (int i=1;i<10;i++){
//				RegisterRequest registerRequest = new RegisterRequest();
//				registerRequest.setEmail("nouresamt." + i+ "@gmail.com");
//				registerRequest.setFirstName("Nour" + " " +i);
//				registerRequest.setLastName("Esmat");
//				registerRequest.setPassword("20181056");
//				authServices.register(registerRequest);
//			}
		};
	}
}
