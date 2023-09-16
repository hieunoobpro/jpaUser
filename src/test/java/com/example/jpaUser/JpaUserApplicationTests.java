package com.example.jpaUser;

import com.example.jpaUser.model.User;
import com.example.jpaUser.repository.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class JpaUserApplicationTests {
@Autowired
	private UserRepository userRepository;
	@Test
	void contextLoads() {
	}
	@Test
	void save_all_product(){
		Faker faker = new Faker();
		List<User> products = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			User user = new User();
			user.setName(faker.name().fullName());
			user.setEmail(faker.internet().emailAddress());
			user.setPhone(faker.phoneNumber().phoneNumber());
			user.setAddress(faker.address().fullAddress());
			user.setAvatar(faker.internet().avatar());
			user.setPassword(faker.internet().password());
			products.add(user);
		}
		userRepository.saveAll(products);
	}
}
