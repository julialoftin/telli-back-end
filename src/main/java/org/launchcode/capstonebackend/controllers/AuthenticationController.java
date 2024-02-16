package org.launchcode.capstonebackend.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.launchcode.capstonebackend.models.User;
import org.launchcode.capstonebackend.models.data.UserRepository;
import org.launchcode.capstonebackend.models.dto.LoginFormDTO;
import org.launchcode.capstonebackend.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/authentication")
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;

    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid RegisterFormDTO registerFormDTO,
                                                            Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Validation failed. Please check your inputs."));
        }

        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "A user with that username already exists."));
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Passwords do not match"));
        }

        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User created!"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody @Valid LoginFormDTO loginFormDTO,
                                                         Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Validation failed. Please check your inputs."));
        }

        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());
        if (theUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Username does not exist."));
        }

        String password = loginFormDTO.getPassword();
        if (!theUser.isMatchingPassword(password)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Password is incorrect."));
        }

        setUserInSession(request.getSession(), theUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("message", "Log in successful!"));
    }

    @GetMapping("/check-login")
    public ResponseEntity<Map<String, Boolean>> checkLogin(HttpServletRequest request) {
        boolean isLoggedIn = getUserFromSession(request.getSession()) != null;
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("isLoggedIn", isLoggedIn));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logoutUser(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "User is not logged in"));
        }
        request.getSession().invalidate();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Logout successful."));
    }

}
