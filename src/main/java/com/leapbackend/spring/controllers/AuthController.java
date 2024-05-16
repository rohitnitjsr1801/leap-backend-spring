package com.leapbackend.spring.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.leapbackend.spring.models.*;
import com.leapbackend.spring.repository.CustomerDetailRepository;
import com.leapbackend.spring.repository.ManagerDetailRepository;
import com.leapbackend.spring.repository.RoleRepository;
import com.leapbackend.spring.repository.UserRepository;
import com.leapbackend.spring.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leapbackend.spring.payload.request.LoginRequest;
import com.leapbackend.spring.payload.request.SignupRequest;
import com.leapbackend.spring.payload.response.JwtResponse;
import com.leapbackend.spring.payload.response.MessageResponse;
import com.leapbackend.spring.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  CustomerDetailRepository customerDetailRepository;

  @Autowired
  ManagerDetailRepository managerDetailRepository;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles,
            userDetails.getGender(),
            userDetails.getAge()
    ));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()),signUpRequest.getAge(),signUpRequest.getGender());


    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "owner":
            Role adminRole = roleRepository.findByName(ERole.ROLE_OWNER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            user.setRoles(roles);
            User response = userRepository.save(user);
            ManagerDetail managerDetail = new ManagerDetail();
            managerDetail.setUser(user);
            managerDetail.setOrganization(signUpRequest.getOrganization());
            managerDetailRepository.save(managerDetail);
            break;
          case "manager":
            Role modRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);
            user.setRoles(roles);
            User response2 = userRepository.save(user);
            ManagerDetail managerDetail2 = new ManagerDetail();
            managerDetail2.setUser(user);
            managerDetail2.setOrganization(signUpRequest.getOrganization());
            managerDetailRepository.save(managerDetail2);
            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            user.setRoles(roles);
            User response3 = userRepository.save(user);
            CustomerDetail customerDetail = new CustomerDetail();
            customerDetail.setUser(user);
            customerDetail.setAge(signUpRequest.getAge());
            customerDetail.setGender(signUpRequest.getGender());
            customerDetailRepository.save(customerDetail);
        }
      });
    }

//    user.setRoles(roles);
//    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
