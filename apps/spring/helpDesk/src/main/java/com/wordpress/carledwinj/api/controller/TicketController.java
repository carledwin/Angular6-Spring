package com.wordpress.carledwinj.api.controller;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordpress.carledwinj.api.entity.Ticket;
import com.wordpress.carledwinj.api.entity.User;
import com.wordpress.carledwinj.api.enums.StatusEnum;
import com.wordpress.carledwinj.api.response.Response;
import com.wordpress.carledwinj.api.security.jwt.JwtTokenUtil;
import com.wordpress.carledwinj.api.service.TicketService;
import com.wordpress.carledwinj.api.service.UserService;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin("*")
public class TicketController {

	//@Autowired
	private TicketService ticketService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> create(HttpServletRequest httpServeletRequest, @RequestBody Ticket ticket, BindingResult bindingResult){
		
		Response<Ticket> responseTicket = new Response<Ticket>();
		
		try {
			
			validateCreateTicket(ticket, bindingResult);
			if(bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(
						error -> responseTicket.getErrors().add(error.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(responseTicket);
			}
			
			ticket.setStatus(StatusEnum.getStatus("New"));
			ticket.setUser(userFromRequest(httpServeletRequest));
			ticket.setDate(new Date());
			ticket.setNumber(generateNumber());
			
			responseTicket.setData(ticket);
			return ResponseEntity.ok(responseTicket);
		} catch (Exception e) {
			responseTicket.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseTicket);
		}
	}
	
	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt(9999);
	}

	private User userFromRequest(HttpServletRequest httpServeletRequest) {
		
		String token = httpServeletRequest.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}

	private void validateCreateTicket(Ticket ticket, BindingResult bindingResult){
		
		if(ticket.getTitle() == null) {
			bindingResult.addError(new ObjectError("Ticket", "Title no information"));
			return;
		}
	}
}
