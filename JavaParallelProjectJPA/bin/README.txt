create table customerJPA (mobileNo varchar2(10) primary key,name varchar2(15),wallet_id number(10));


create table transactionsJPA (balance number(20,2),transactiondate date,mobileNo varchar2(10),transactionType varchar2(15),id number(10));
