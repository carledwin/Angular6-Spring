package com.wordpress.carledwinj.api.dto;

import java.io.Serializable;

public class Summary implements Serializable{

	private static final long serialVersionUID = 7008034734378488859L;

	private Integer amountNew;
	private Integer amountApproved;
	private Integer amountResolved;
	private Integer amountAssigned;
	private Integer amountDisapproved;
	private Integer amountClosed;

	public Integer getAmountNew() {
		return amountNew;
	}
	public void setAmountNew(Integer amountNew) {
		this.amountNew = amountNew;
	}
	public Integer getAmountApproved() {
		return amountApproved;
	}
	public void setAmountApproved(Integer amountApproved) {
		this.amountApproved = amountApproved;
	}
	public Integer getAmountResolved() {
		return amountResolved;
	}
	public void setAmountResolved(Integer amountResolved) {
		this.amountResolved = amountResolved;
	}
	public Integer getAmountAssigned() {
		return amountAssigned;
	}
	public void setAmountAssigned(Integer amountAssigned) {
		this.amountAssigned = amountAssigned;
	}
	public Integer getAmountDisapproved() {
		return amountDisapproved;
	}
	public void setAmountDisapproved(Integer amountDisapproved) {
		this.amountDisapproved = amountDisapproved;
	}
	public Integer getAmountClosed() {
		return amountClosed;
	}
	public void setAmountClosed(Integer amountClosed) {
		this.amountClosed = amountClosed;
	}


}
