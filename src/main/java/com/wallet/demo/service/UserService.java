package com.wallet.demo.service;

import com.wallet.demo.model.UserModel;

public interface UserService {
    public void addUser(UserModel userModel);
    public UserModel getUser(int userId);
    public UserModel viewProfile(String username) throws Exception;
}
