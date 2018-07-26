package com.cg.mypaymentapp.beans;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="walletJPA")
public class Wallet {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="mysgen")
	@SequenceGenerator(sequenceName="my_sequence",name="mysgen")
	private int id; 
	private BigDecimal balance;
	public Wallet(BigDecimal amount) {
		this.balance=amount;
	}
	public Wallet() {
		// TODO Auto-generated constructor stub
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return ", balance="+balance;
	}
}
