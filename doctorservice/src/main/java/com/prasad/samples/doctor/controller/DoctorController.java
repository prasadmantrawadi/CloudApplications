package com.prasad.samples.doctor.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DoctorController {

	@Autowired
	RestTemplate rest;

	@Value("${patient.restURL}")
	String patientServiceURL;


	private final static Logger logger = LoggerFactory.getLogger(DoctorController.class);


	@GetMapping(value = "/hello/{id}")
	public String getData(@AuthenticationPrincipal Jwt jwt, @PathVariable("id") String abc) {
		logger.info("Inside Doctor Service getData method " + abc);
		logger.info("***** JWT Headers: {}", jwt.getHeaders());
		logger.info("***** JWT Claims: {}", jwt.getClaims().toString());
		logger.info("***** JWT Token: {}", jwt.getTokenValue());
		return String.format("doctorservice Resource accessed by: %s (with subjectId: %s)",
				jwt.getClaims().get("username"), jwt.getSubject());
	}

	@GetMapping("/getPatients")
	String getPatients(@AuthenticationPrincipal Jwt jwt) {
		logger.info("Calling Patients service to get details" + patientServiceURL);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", "Bearer "+jwt.getTokenValue());
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		return rest.exchange(patientServiceURL, HttpMethod.GET, entity, String.class).getBody();
	}
	

}
