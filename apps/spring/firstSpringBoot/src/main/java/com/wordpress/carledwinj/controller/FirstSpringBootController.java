package com.wordpress.carledwinj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstSpringBootController {

	@GetMapping("/")
	public String index() {
		return "index page";
	}
	
	@GetMapping("showText")
	public String showText() {
		return "First Spirng Boot project";
	}
}
