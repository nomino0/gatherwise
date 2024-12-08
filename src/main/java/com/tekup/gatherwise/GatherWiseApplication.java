package com.tekup.gatherwise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tekup.gatherwise.business.services.UserService;

@SpringBootApplication
public class GatherWiseApplication {

	@Autowired
	 UserService userService;
	public static void main(String[] args) {
		SpringApplication.run(GatherWiseApplication.class, args);
	}

// 	 @PostConstruct
//     public void init() {
//         List<String> roles = new ArrayList<>();
//         roles.add("admin");
		
//         userService.saveUser(new User(null, "adminAmine", "password", "amine.mayoufi11@gmail.com", roles));
//     }  
}
