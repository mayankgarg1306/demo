package com.wallet.demo.Entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer userId;
    private String name;
    private String gender;
    private String mobileNo;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="addressId")
    AddressEntity addressEntity;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="accountId")
    AccountEntity accountEntity;
    @OneToMany(mappedBy = "fromUser")
    Set<TransactionEntity> transactionEntitySet;
    @OneToMany(mappedBy = "toUser")
    Set<TransactionEntity> toUserTransactionEntitySet;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public AddressEntity getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public AccountEntity getAccountEntity() {
        return accountEntity;
    }

    public void setAccountEntity(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }

    public Set<TransactionEntity> getTransactionEntitySet() {
        return transactionEntitySet;
    }

    public void setTransactionEntitySet(Set<TransactionEntity> transactionEntitySet) {
        this.transactionEntitySet = transactionEntitySet;
    }

    public Set<TransactionEntity> getToUserTransactionEntitySet() {
        return toUserTransactionEntitySet;
    }

    public void setToUserTransactionEntitySet(Set<TransactionEntity> toUserTransactionEntitySet) {
        this.toUserTransactionEntitySet = toUserTransactionEntitySet;
    }
}
