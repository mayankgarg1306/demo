package com.wallet.demo.serviceImpl;

import com.wallet.demo.model.Account;
import com.wallet.demo.model.Address;
import com.wallet.demo.model.UserModel;
import com.wallet.demo.Entity.AccountEntity;
import com.wallet.demo.Entity.AddressEntity;
import com.wallet.demo.Entity.UserCredentialEntity;
import com.wallet.demo.Entity.UserEntity;
import com.wallet.demo.repository.UserCredentialRepository;
import com.wallet.demo.repository.UserRepository;
import com.wallet.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    public void addUser(UserModel userModel) {
        UserEntity userEntity=populateUserEntity(userModel);
        userModel.setPassword(new BCryptPasswordEncoder().encode(userModel.getPassword()));
        UserCredentialEntity userCredentialEntity=populateUserCredentialEntity(userModel,userEntity);
        userCredentialRepository.save(userCredentialEntity);
    }

    @Override
    public UserModel getUser(int userId) {
        Optional<UserEntity> userEntity=userRepository.findById(userId);
        return populateUserDTO(userEntity.get());
    }

    @Override
    public UserModel viewProfile(String username)throws Exception{
        try {
            Optional<UserCredentialEntity> userCredentialEntityOptional = userCredentialRepository.findByUsername(username);
            if (userCredentialEntityOptional.isPresent()) {
                UserCredentialEntity userCredentialEntity = userCredentialEntityOptional.get();
                UserEntity userEntity = userCredentialEntity.getUserEntity();
                return populateUserDTO(userEntity);
            } else {
                throw new Exception("User not present with Username:" + username);
            }
        }catch (Exception e){
            throw new Exception("Not Able to fetch Profile");
        }
    }

    private UserCredentialEntity populateUserCredentialEntity(UserModel userModel, UserEntity userEntity){
        UserCredentialEntity userCredentialEntity=new UserCredentialEntity();
        userCredentialEntity.setUserEntity(userEntity);
        userCredentialEntity.setUsername(userModel.getUserName());
        userCredentialEntity.setPassword(userModel.getPassword());
        return userCredentialEntity;
    }

    private UserEntity populateUserEntity(UserModel userModel){

        UserEntity userEntity=new UserEntity();

        AccountEntity accountEntity=new AccountEntity();
        accountEntity.setAmount(userModel.getAccount().getAmount());

        AddressEntity addressEntity=new AddressEntity();
        addressEntity.setCity(userModel.getAddress().getCity());
        addressEntity.setHouseno(userModel.getAddress().getHouseNo());
        addressEntity.setPincode(userModel.getAddress().getPinCode());
        addressEntity.setState(userModel.getAddress().getState());

        userEntity.setAccountEntity(accountEntity);
        userEntity.setAddressEntity(addressEntity);
        userEntity.setGender(userModel.getGender());
        userEntity.setMobileNo(userModel.getMobileNo());
        userEntity.setName(userModel.getName());
        return userEntity;
    }

    private UserModel populateUserDTO(UserEntity userEntity){
        UserModel userModel =new UserModel();
        userModel.setUserId(userEntity.getUserId());
        userModel.setName(userEntity.getName());
        userModel.setGender(userEntity.getGender());
        userModel.setMobileNo(userEntity.getMobileNo());
        Account account=new Account();
        account.setAmount(userEntity.getAccountEntity().getAmount());
        account.setAccountId(userEntity.getAccountEntity().getAccountId());
        userModel.setAccount(account);
        Address address=new Address();
        address.setId(userEntity.getAddressEntity().getAddressId());
        address.setCity(userEntity.getAddressEntity().getCity());
        address.setState(userEntity.getAddressEntity().getState());
        address.setHouseNo(userEntity.getAddressEntity().getHouseno());
        address.setPinCode(userEntity.getAddressEntity().getPincode());
        userModel.setAddress(address);
        return userModel;
    }


}
