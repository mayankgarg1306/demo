package com.wallet.demo.serviceImpl;

import com.wallet.demo.Entity.UserCredentialEntity;
import com.wallet.demo.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserCredentialServiceImpl implements UserDetailsService{

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserCredentialEntity> userCredentialEntity=userCredentialRepository.findByUsername(username);

        if (userCredentialEntity.isPresent()) {
            return new User(userCredentialEntity.get().getUsername(),userCredentialEntity.get().getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}

