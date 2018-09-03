package com.wordpress.carledwinj.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wordpress.carledwinj.api.entity.User;
import com.wordpress.carledwinj.api.enums.ProfileEnum;
import com.wordpress.carledwinj.api.repository.UserRepository;

@SpringBootApplication
public class HelpDeskApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepository, passwordEncoder);
		};
	}

	//este metodo insere um usuario ao inicializar a aplicacao
	private void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		
		User admin = new User();
		admin.setEmail("pegatoken@helpdesk.com");
		admin.setPassword(passwordEncoder.encode("senha"));
		admin.setProfile(ProfileEnum.ROLE_ADMIN);
		
		User find = userRepository.findByEmail(admin.getEmail());
		
		if(find == null) {
			userRepository.save(admin);
		}
	}
	
}
