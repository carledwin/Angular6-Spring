package com.wordpress.carledwinj.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wordpress.carledwinj.api.entity.ChangeStatus;

public interface ChangeStatusRepository extends MongoRepository<ChangeStatus, String> {

	Iterable<ChangeStatus> findByTicketIdOrderByDateChangeDesc(String ticketId);
}
