package com.cg.mypaymentapp.beans;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TransactionsJPA")
public class Transactions implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="mysgen")
	@SequenceGenerator(sequenceName="my_sequence",name="mysgen")
	private int id;
	private String mobileNo;
	private Date transactionDate;
	private String transactionType;
	private BigDecimal balance;
	public Transactions(String mobileNo, Date transactiondate, String transactionType, BigDecimal balance) {
		super();
		this.mobileNo = mobileNo;
		this.transactionDate = transactiondate;
		this.transactionType = transactionType;
		this.balance = balance;
	}
	public Transactions() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactiondate) {
		this.transactionDate = transactiondate;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Transactions [id=" + id + ", mobileNo=" + mobileNo + ", transactiondate=" + transactionDate
				+ ", transactionType=" + transactionType + ", balance=" + balance + "]";
	}
}