package com.wallet.demo.Entity;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer accountId;
    @OneToOne(mappedBy = "accountEntity", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private UserEntity userEntity;
    private Integer amount;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
