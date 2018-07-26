
package com.cg.mypaymentapp.service;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Transactions;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.exception.InsufficientBalanceException;
import com.cg.mypaymentapp.exception.InvalidInputException;
import com.cg.mypaymentapp.repo.WalletRepo;
import com.cg.mypaymentapp.repo.WalletRepoImpl;


public class WalletServiceImpl implements WalletService{
	static Logger myLogger=Logger.getLogger(WalletServiceImpl.class);
	private WalletRepo repo= new WalletRepoImpl();
	Customer customer;
	Transactions transaction;
	Map<String,Customer> data= new HashMap<String, Customer>();
	long millis=System.currentTimeMillis();
	/*List<Transactions> data;*/
		public WalletServiceImpl(Map<String, Customer> data)
	{
		this.data=data;
	}
	public WalletServiceImpl(WalletRepo repo) {
		super();
		this.repo = repo;
	}

	public WalletServiceImpl() {

	}

	public Customer createAccount(String name, String mobileNo, BigDecimal amount)  
	{
		myLogger.info("In createAccount method");
		if(!isValidName(name)||!isValidMobile(mobileNo)||!isValidAmount(amount))
		{
			System.out.println("qqqqqqqqqqqqq");
			myLogger.error("invalid inputs while creating");
			throw new InvalidInputException("Invalid Inputs");
		}
		else
		{

			Wallet wallet=new Wallet(amount);
			customer=new Customer();
			customer.setName(name);
			customer.setMobileNo(mobileNo);
			customer.setWallet(wallet);

			repo.startTransaction();

			repo.save(customer);

			repo.commitTransaction();

			myLogger.info("Account Successfully created");
			return customer;

		}

	}



	public Customer showBalance(String mobileNo) 
	{
		myLogger.info("In showBalance()");
		if(!isValidMobile(mobileNo))
		{

			throw new InvalidInputException("Incorrect mobile number");
		}
		else
		{
			Customer customer=repo.findOne(mobileNo);
			if(customer!=null)
				return customer;
			else
			{
				myLogger.error("No account found");
				throw new InvalidInputException("Account with this mobile number not found");
			}
		}
	}

	public Customer fundTransfer(String sourceMobileNo, String targetMobileNo, BigDecimal amount) 
	{
		myLogger.info("In fundTransfer()");
		if(!isValidMobile(sourceMobileNo)&&!isValidMobile(targetMobileNo)&&!isValidAmount(amount))
		{
			myLogger.error("Invalid inputs");
			throw new InvalidInputException("Invalid Inputs");
		}
		else
		{
			Customer customer1=repo.findOne(sourceMobileNo);
			Customer customer2=repo.findOne(targetMobileNo);
			if(customer1!=null&&customer2!=null)
			{
				Wallet wal1=customer1.getWallet();
				Wallet wal2=customer2.getWallet();
				BigDecimal balance1=wal1.getBalance();
				BigDecimal balance2=wal2.getBalance();
				if(balance1.compareTo(amount)>0)
				{
					transaction=new Transactions();

					repo.startTransaction();


					Date date=new java.sql.Date(millis);

					wal1.setBalance(balance1.subtract(amount));
					transaction.setMobileNo(sourceMobileNo);
					transaction.setTransactionDate(date);
					transaction.setTransactionType("fundTransfer");
					transaction.setBalance(wal1.getBalance());
					repo.update(wal1, transaction);


					wal2.setBalance(balance2.add(amount));
					transaction.setMobileNo(targetMobileNo);
					transaction.setTransactionDate(date);
					transaction.setTransactionType("fund received");
					transaction.setBalance(wal2.getBalance());

					repo.update(wal1, transaction);

					repo.commitTransaction();
					return customer1;
				}
				else
				{
					throw new InsufficientBalanceException("Insufficient balance");
				}
			}
			else
			{
				myLogger.error("No account found");
				throw new InvalidInputException("Account with this mobile number not found");
			}

		}

	}

	public Customer depositAmount(String mobileNo, BigDecimal amount) 
	{
		myLogger.info("In depositAmount()");
		if(!isValidMobile(mobileNo)&&!isValidAmount(amount))
		{
			myLogger.error("Invalid inputs");
			throw new InvalidInputException("Invalid Inputs");
		}
		else
		{
			Customer customer=repo.findOne(mobileNo);
			if(customer!=null)
			{
				Wallet wal=customer.getWallet();
				wal.setBalance(wal.getBalance().add(amount));

				repo.startTransaction();
				transaction=new Transactions();
				transaction.setMobileNo(mobileNo);
				//java.sql.Date date=getCurrentJavaSqlDate();

				Date date=new java.sql.Date(millis);
				transaction.setTransactionDate(date);
				transaction.setTransactionType("deposit");
				transaction.setBalance(wal.getBalance());
				try {
					repo.update(wal,transaction);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				repo.commitTransaction();
				return customer;
			}
			else
			{
				myLogger.error("No account found");
				throw new InvalidInputException("Account with this mobile number not found");
			}
		}


	}

	public Customer withdrawAmount(String mobileNo, BigDecimal amount) 
	{
		myLogger.info("In withdraw()");
		if(!isValidMobile(mobileNo)&&!isValidAmount(amount))
		{
			myLogger.error("Invalid inputs");

			throw new InvalidInputException("invalid inputs");
		}
		else
		{
			Customer customer=repo.findOne(mobileNo);
			if(customer!=null)
			{
				Wallet wal=customer.getWallet();
				if(wal.getBalance().compareTo(amount)>0)
				{
					wal.setBalance(wal.getBalance().subtract(amount));
					repo.startTransaction();
					transaction=new Transactions();
					transaction.setMobileNo(mobileNo);
					Date date=new java.sql.Date(millis);
					transaction.setTransactionDate(date);
					transaction.setTransactionType("withdraw");
					transaction.setBalance(wal.getBalance());
					repo.update(wal, transaction);
					repo.commitTransaction();
					return customer;
				}
				else 
				{
					throw new InsufficientBalanceException("Insufficient balance");
				}
			}
			else
			{
				myLogger.error("No account found");
				throw new InvalidInputException("Account with this mobile number not found");
			}
		}
	}

	@Override
	public List<Transactions> viewTransactions(String mobileNo) 
	{
		List<Transactions> transactionList;
		Customer customer=repo.findOne(mobileNo);

		if(customer!=null)
		{
			repo.startTransaction();
			transactionList=repo.transactions(mobileNo);
			repo.commitTransaction();

			return transactionList;
		}
		else
		{
			myLogger.error("No account found");
			throw new InvalidInputException("Account with this mobile number not found");
		}
	}
	//validation
	private boolean isValidName(String name) {
		if(String.valueOf(name).matches("[A-Za-z]+")) 
			return true;		
		else 
			return false;
	}
	private boolean isValidAmount(BigDecimal amount)
	{
		BigDecimal num=new BigDecimal("0");
		if(amount.compareTo(num)>0)
		{	return true;
		}
		else 
		{	return false;}
	}
	private boolean isValidMobile(String mobileNo) 
	{
		if(String.valueOf(mobileNo).matches("[1-9][0-9]{9}")) 
			return true;		
		else 
			return false;
	}
	public static java.sql.Date getCurrentJavaSqlDate()
	{
		java.util.Date today=new java.util.Date();
		return new java.sql.Date(today.getTime());
	}
}
