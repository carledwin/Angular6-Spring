package com.wordpress.carledwinj.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordpress.carledwinj.api.dto.Summary;
import com.wordpress.carledwinj.api.entity.ChangeStatus;
import com.wordpress.carledwinj.api.entity.Ticket;
import com.wordpress.carledwinj.api.entity.User;
import com.wordpress.carledwinj.api.enums.ProfileEnum;
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

	@GetMapping("/{page}/{count}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Page<Ticket>>> findAll(HttpServletRequest httpServeletRequest, @PathVariable Integer page, @PathVariable Integer count , BindingResult bindingResult){
		
		Response<Page<Ticket>> responsePageTicket = new Response<Page<Ticket>>();
		
		Page<Ticket> pageTickets = null;
		
		try {
			
			User userRequest = userFromRequest(httpServeletRequest);
		
			validateUserRequest(userRequest, bindingResult);
			if(bindingResult.hasErrors()){
				bindingResult.getAllErrors().forEach( error -> responsePageTicket.getErrors().add( error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(responsePageTicket);
			}
			
			validatePageCount(page, count, bindingResult);
			if(bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(
						error -> responsePageTicket.getErrors().add(error.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(responsePageTicket);
			}
			
			if(validateProfileUser(userRequest, ProfileEnum.ROLE_TECHNICIAN)){
				pageTickets = ticketService.listTicket(page, count);
			} else if(validateProfileUser(userRequest, ProfileEnum.ROLE_CUSTOMER)){
				pageTickets = ticketService.findByCurrentUser(page.intValue(), count.intValue(), userRequest.getId());
			}
			
			if(pageTickets == null) {
				responsePageTicket.getErrors().add("Ticket not found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePageTicket);
			}
			
			responsePageTicket.setData(pageTickets);
			return ResponseEntity.ok(responsePageTicket);
		} catch (Exception e) {
			responsePageTicket.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responsePageTicket);
		}
	}

	@GetMapping("/{page}/{count}/{number}/{title}/{status}/{priority}/{assigned}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Page<Ticket>>> findByParameters(HttpServletRequest httpServeletRequest,
			@PathVariable Integer page, @PathVariable Integer count, @PathVariable Integer number,
			@PathVariable String title, @PathVariable String status, @PathVariable String priority,
			@PathVariable boolean assigned, BindingResult bindingResult) {
		
		Response<Page<Ticket>> responsePageTicket = new Response<Page<Ticket>>();
		
		Page<Ticket> pageTickets = null;
		
		try {

			if(number > 0) {
				pageTickets = ticketService.findByNumber(page, count, number);
			} else {
				
				User userRequest = userFromRequest(httpServeletRequest);
				
				validateUserRequest(userRequest, bindingResult);
				if(bindingResult.hasErrors()){
					bindingResult.getAllErrors().forEach( error -> responsePageTicket.getErrors().add( error.getDefaultMessage()));
					return ResponseEntity.badRequest().body(responsePageTicket);
				}
				
				validatePageCount(page, count, bindingResult);
				if(bindingResult.hasErrors()) {
					bindingResult.getAllErrors().forEach(
							error -> responsePageTicket.getErrors().add(error.getDefaultMessage()));
					
					return ResponseEntity.badRequest().body(responsePageTicket);
				}
				
				if(validateProfileUser(userRequest, ProfileEnum.ROLE_TECHNICIAN)){
					
					title = title.equalsIgnoreCase("uninformed") ? "" : title;
					status = status.equalsIgnoreCase("uninformed") ? "" : status;
					priority = priority.equalsIgnoreCase("uninformed") ? "" : priority;
					
					if(assigned) {
						pageTickets = ticketService.findByParametersAndAssignedUser(page, count, title, status, priority, userRequest.getId());
					}
					
					pageTickets = ticketService.findByParameters(page, count, title, status, priority);
					
				}else if(validateProfileUser(userRequest, ProfileEnum.ROLE_CUSTOMER)) {
					
					pageTickets = ticketService.findByParametersAndCurrentUser(page, count, title, status, priority, userRequest.getId());
				}
			}	
			
			if(pageTickets == null) {
				responsePageTicket.getErrors().add("Ticket not found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responsePageTicket);
			}
			
			responsePageTicket.setData(pageTickets);
			return ResponseEntity.ok(responsePageTicket);
		} catch (Exception e) {
			responsePageTicket.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responsePageTicket);
		}
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Ticket>> findById(HttpServletRequest httpServeletRequest, @PathVariable Integer id, BindingResult bindingResult){
		
		Response<Ticket> responseTicket = new Response<Ticket>();
		
		try {
			
			validateFindById(id, bindingResult);
			if(bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(
						error -> responseTicket.getErrors().add(error.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(responseTicket);
			}
			
			Ticket ticket = ticketService.findById(id.toString());
			
			if(ticket == null) {
				responseTicket.getErrors().add("Ticket not found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseTicket);
			}
			
			Iterable<ChangeStatus> listChangeStatus = ticketService.listChangeStatus(id.toString());
			
			if(listChangeStatus != null) {
				
				ticket.setChanges(new ArrayList<>());
				
				for (ChangeStatus changeStatus : listChangeStatus) {
					changeStatus.setTicket(null);
					ticket.getChanges().add(changeStatus);
				}
			}
			
			responseTicket.setData(ticket);
			return ResponseEntity.ok(responseTicket);
		} catch (Exception e) {
			responseTicket.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseTicket);
		}
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Void>> delete(HttpServletRequest httpServeletRequest, @PathVariable Integer id, BindingResult bindingResult){
		
		Response<Void> response = new Response<Void>();
		
		try {
			
			validateFindById(id, bindingResult);
			if(bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(
						error -> response.getErrors().add(error.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(response);
			}
			
			Ticket ticket = ticketService.findById(id.toString());
			
			if(ticket == null) {
				response.getErrors().add("Ticket not found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			
			ticketService.delete(id.toString());
			
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
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
			
			ticket = ticketService.createOrUpdate(ticket);
			
			responseTicket.setData(ticket);
			return ResponseEntity.ok(responseTicket);
		} catch (Exception e) {
			responseTicket.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseTicket);
		}
	}
	
	@PutMapping
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> update(HttpServletRequest httpServeletRequest, @RequestBody Ticket ticket, BindingResult bindingResult){
		
		Response<Ticket> responseTicket = new Response<Ticket>();
		
		try {
			
			validateUpdateTicket(ticket, bindingResult);
			if(bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(
						error -> responseTicket.getErrors().add(error.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(responseTicket);
			}
			
			
			Ticket ticketCurrent = ticketService.findById(ticket.getId());
			
			if(ticketCurrent == null) {
				responseTicket.getErrors().add("Ticket not found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseTicket);
			}
			
			ticketCurrent.setTitle(ticket.getTitle());
			ticketCurrent.setPriority(ticket.getPriority());
			ticketCurrent.setDescription(ticket.getDescription());
			ticketCurrent.setImage(ticket.getImage());
			
			ticketCurrent = ticketService.createOrUpdate(ticketCurrent);
			
			responseTicket.setData(ticketCurrent);
			return ResponseEntity.accepted().body(responseTicket);
		} catch (Exception e) {
			responseTicket.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseTicket);
		}
	}
	
	@PutMapping("/{id}/{status}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Ticket>> changeStatus(HttpServletRequest httpServeletRequest, @RequestBody Ticket ticket,
			@PathVariable Integer id, @PathVariable String status, BindingResult bindingResult) {
		
		Response<Ticket> responseTicket = new Response<Ticket>();
		
		try {
			
			validateChangeStatus(id, status, bindingResult);
			if(bindingResult.hasErrors()) {
				bindingResult.getAllErrors().forEach(
						error -> responseTicket.getErrors().add(error.getDefaultMessage()));
				
				return ResponseEntity.badRequest().body(responseTicket);
			}
			
			Ticket ticketCurrent = ticketService.findById(id.toString());
			
			if(ticketCurrent == null) {
				responseTicket.getErrors().add("Ticket not found!");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseTicket);
			}
			
			ticketCurrent.setStatus(StatusEnum.getStatus(status));
			
			if(status.equalsIgnoreCase("Assigned")) {
				ticketCurrent.setAssignedUser(userFromRequest(httpServeletRequest));
			}
			
			ticketCurrent = ticketService.createOrUpdate(ticketCurrent);
			
			ChangeStatus changeStatus = new ChangeStatus();
			changeStatus.setDateChange(new Date());
			changeStatus.setStatus(ticketCurrent.getStatus());
			changeStatus.setTicket(ticketCurrent);
			changeStatus.setUserChange(userFromRequest(httpServeletRequest));
			
			ticketService.createChangeStatus(changeStatus);
			
			responseTicket.setData(ticketCurrent);
			return ResponseEntity.accepted().body(responseTicket);
		} catch (Exception e) {
			responseTicket.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseTicket);
		}
	}
	
	@GetMapping("/summary")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Summary>> findSummary(){
		
		Response<Summary> responseSummary= new Response<Summary>();
		
		try {
			
			Summary summary = new Summary();
			
			Integer amountNew = 0;
			Integer amountApproved = 0;
			Integer amountResolved = 0;
			Integer amountAssigned = 0;
			Integer amountDisapproved = 0;
			Integer amountClosed = 0;
			
			
			Iterable<Ticket> tickets = ticketService.findAll();
			
			for (Ticket ticket : tickets) {
				
				if(StatusEnum.New.equals(ticket.getStatus())){
					amountNew++;
				}
				
				if(StatusEnum.Assigned.equals(ticket.getStatus())){
					amountAssigned++;
				}
				
				if(StatusEnum.Disapproved.equals(ticket.getStatus())){
					amountDisapproved++;
				}
				
				if(StatusEnum.Approved.equals(ticket.getStatus())){
					amountApproved++;
				}
				
				if(StatusEnum.Resolved.equals(ticket.getStatus())){
					amountResolved++;
				}
				
				if(StatusEnum.Close.equals(ticket.getStatus())){
					amountClosed++;
				}
				
			}
			
			summary.setAmountApproved(amountApproved);
			summary.setAmountAssigned(amountAssigned);
			summary.setAmountClosed(amountClosed);
			summary.setAmountDisapproved(amountDisapproved);
			summary.setAmountNew(amountNew);
			summary.setAmountResolved(amountResolved);
			
			responseSummary.setData(summary);
			
			return ResponseEntity.ok(responseSummary);
		} catch (Exception e) {
			responseSummary.getErrors().add(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseSummary);
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

	private void validateChangeStatus(Integer id, String status, BindingResult bindingResult){
		
		if(id == null) {
			bindingResult.addError(new ObjectError("Ticket", "Id no information!"));
			return;
		}
		
		if(status == null || status.equals("")) {
			bindingResult.addError(new ObjectError("Ticket", "Status no information!"));
			return;
		}
	}
	
	private boolean validateProfileUser(User user, ProfileEnum profileEnum) {
		
		if(user == null ||user.getProfile() == null || profileEnum == null) {
			return false;
		}
		
		if(user.getProfile().equals(profileEnum)) {
			return true;
		}
		
		return false;
	}
	
	private void validateUserRequest(User userRequest, BindingResult bindingResult) {
		
		if(userRequest == null) {
			bindingResult.addError(new ObjectError("User", "Invalid user!"));
		}
		
	}
	
	private void validatePageCount(Integer page, Integer count, BindingResult bindingResult){
		
		if(count < 0) {
			bindingResult.addError(new ObjectError("Ticket", "Invalid page!"));
			return;
		}
		
		if(count <= 0) {
			bindingResult.addError(new ObjectError("Ticket", "Invalid count!"));
			return;
		}
	}
	
	private void validateFindById(Integer id, BindingResult bindingResult){
		
		if(id <= 0) {
			bindingResult.addError(new ObjectError("Ticket", "Invalid Id!"));
			return;
		}
	}
	
	private void validateCreateTicket(Ticket ticket, BindingResult bindingResult){
		
		if(ticket.getTitle() == null) {
			bindingResult.addError(new ObjectError("Ticket", "Title no information!"));
			return;
		}
	}
	
	private void validateUpdateTicket(Ticket ticket, BindingResult bindingResult){
		
		if(ticket.getId() == null) {
			bindingResult.addError(new ObjectError("Ticket", "Id no information!"));
			return;
		}
		
		if(ticket.getTitle() == null) {
			bindingResult.addError(new ObjectError("Ticket", "Title no information!"));
			return;
		}
	}
}
