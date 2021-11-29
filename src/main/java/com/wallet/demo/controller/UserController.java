package com.wallet.demo.controller;

import com.wallet.demo.model.JwtResponse;
import com.wallet.demo.model.UserModel;
import com.wallet.demo.security.JwtTokenUtil;
import com.wallet.demo.service.UserService;
import com.wallet.demo.serviceImpl.UserCredentialServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserCredentialServiceImpl userDetailsService;

    @PostMapping("/addUser")
    public ResponseEntity addUser(@RequestBody UserModel user){
        userService.addUser(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserModel> getUser(@PathVariable("id") String userId, Authentication authentication){
        try {
            UserModel userModel = userService.getUser(Integer.parseInt(userId));
            return new ResponseEntity<>(userModel, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity("Id Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserModel> viewProfile(Authentication authentication){
        try {
            User user= (User) authentication.getPrincipal();
            UserModel userModel=userService.viewProfile(user.getUsername());
            return new ResponseEntity(userModel,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserModel authenticationRequest) throws Exception {
        try {
            authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUserName());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
