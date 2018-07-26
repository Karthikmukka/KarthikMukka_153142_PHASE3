package com.cg.mypaymentapp.repo;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.cg.mypaymentapp.beans.Customer;
import com.cg.mypaymentapp.beans.Transactions;
import com.cg.mypaymentapp.beans.Wallet;
import com.cg.mypaymentapp.util.JPAUtil;
public class WalletRepoImpl implements WalletRepo{
	private List<Transactions> transactionsList=new ArrayList<Transactions>(); 
	/*public WalletRepoImpl(Map<String, Customer> data) {
		super();
		this.data = data;
	}*/
	private EntityManager entityManager;
	public WalletRepoImpl(){
		entityManager = JPAUtil.getEntityManager();
	}
	public boolean save(Customer customer) {		
		entityManager.persist(customer);
		return true;
	}
	public Customer findOne(String mobileNo) {
		Customer customer=entityManager.find(Customer.class, mobileNo);
		return customer;
	}
	public void update(Wallet wallet,Transactions transaction){
		entityManager.merge(wallet);
		entityManager.persist(transaction);
	}
	@Override
	public List<Transactions> transactions(String mobileNo) {
		String qstr="select trans from Transactions trans where trans.mobileNo=:m_no";
		TypedQuery<Transactions> query=entityManager.createQuery(qstr, Transactions.class);
		query.setParameter("m_no", mobileNo);
		transactionsList=query.getResultList();
		return transactionsList;
	}
	@Override
	public void startTransaction() {

		entityManager.getTransaction().begin();
	}
	@Override
	public void commitTransaction() {
		try {
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}