package com.leapbackend.spring.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/customer")
  @PreAuthorize("hasRole('CUSTOMER')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/manager")
  @PreAuthorize("hasRole('MANAGER')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/owner")
  @PreAuthorize("hasRole('OWNER')")
  public String adminAccess() {
    return "Admin Board.";
  }

  @PostMapping("/owner")
  @PreAuthorize("hasRole('OWNER')")
  public String printName(@RequestParam String name)
  {
    return name;
  }
}
