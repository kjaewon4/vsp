package com.bu.startup;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA Auditing 활성화
//@EnableScheduling
public class VirtualStartUpApplication {

	public static List<String> PhotonAppID = List.of(
			"8bc28e47-8596-451a-87d2-f8c61d889d7b",
			"36e0ded9-872f-453d-ace0-3e5c0cdbb42c",
			"f71c0612-43e4-4b69-8bb3-200deb1d4098",
			"f521d45b-68df-4333-9c96-371894739ed8");
	
	public static void main(String[] args) {
		SpringApplication.run(VirtualStartUpApplication.class, args);
	}

}
