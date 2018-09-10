package com.wordpress.carledwinj.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wordpress.carledwinj.api.entity.ChangeStatus;
import com.wordpress.carledwinj.api.entity.Ticket;
import com.wordpress.carledwinj.api.repository.ChangeStatusRepository;
import com.wordpress.carledwinj.api.repository.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private ChangeStatusRepository changeStatusRepository;
	
	public Ticket createOrUpdate(Ticket ticket) {
		return this.ticketRepository.save(ticket);
	}

	public Ticket findById(String id) {
		return this.ticketRepository.findOne(id);
	}
	
	public void delete(String id) {
		this.ticketRepository.delete(id);
	}

	public Page<Ticket> listTicket(int page, int count) {
		Pageable pageable = new PageRequest(page, count);
		return this.ticketRepository.findAll(pageable);
	}

	public ChangeStatus createChangeStatus(ChangeStatus changeStatus) {
		return this.changeStatusRepository.save(changeStatus);
	}

	public Iterable<ChangeStatus> listChangeStatus(String ticketId) {
		return this.changeStatusRepository.findByTicketIdOrderByDateChangeDesc(ticketId);
	}

	public Page<Ticket> findByCurrentUser(int page, int count, String userId) {
		Pageable pageable = new PageRequest(page, count);
		return this.ticketRepository.findByUserIdOrderByDateDesc(pageable, userId);
	}

	public Page<Ticket> findByParameters(int page, int count, String title, String status, String priority) {
		Pageable pageable = new PageRequest(page, count);
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusContainingAndPriorityContainingOrderByDateDesc(title, status, priority, pageable);
	}

	public Page<Ticket> findByParametersAndCurrentUser(int page, int count, String title, String status,
			String priority, String userId) {
		Pageable pageable = new PageRequest(page, count);
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusContainingAndPriorityContainingAndUserIdOrderByDateDesc(title, status, priority, userId, pageable);
	}

	public Page<Ticket> findByNumber(int page, int count, Integer number) {
		Pageable pageable = new PageRequest(page, count);
		return this.ticketRepository.findByNumber(number, pageable);
	}

	public Iterable<Ticket> findAll() {
		return this.ticketRepository.findAll();
	}

	public Page<Ticket> findByParametersAndAssignedUser(int page, int count, String title, String status,
			String priority, String assignedUser) {
		Pageable pageable = new PageRequest(page, count);
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusContainingAndPriorityContainingAndAssignedUserIdOrderByDateDesc(title, status, priority, assignedUser, pageable);
	}

}
