package de.marx_labs.ads.server.test;

import org.springframework.stereotype.Component;

@Component
public class TestService {

	public void print (String message) {
		System.out.println(message);
	}
}
