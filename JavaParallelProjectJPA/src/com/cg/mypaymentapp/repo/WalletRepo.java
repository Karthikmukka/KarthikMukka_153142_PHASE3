package com.cg.mypaymentapp.repo;
import java.util.List;
import java.util.Map;
import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Transactions;
import com.cg.mypaymentapp.beans.Wallet;
public interface WalletRepo {
	public boolean save(Customer customer);
	public Customer findOne(String mobileNo);
	public void update(Wallet wallet,Transactions transaction);
	public List<Transactions> transactions(String mobileNo);
	public void startTransaction();
	public void commitTransaction();
}