package com.god.MediaManager.controller.user;

import com.god.MediaManager.DTO.AuthenticationRequest;
import com.god.MediaManager.DTO.JwtResponse;
import com.god.MediaManager.util.JwtUtil;
import com.god.MediaManager.model.auth.User;
import com.god.MediaManager.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) {
        try {
            if (!authService.registerUser(user)) {
                return ResponseEntity.status(403).body("Username exits, please chose different username");
            }
            final String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Some thing wrong, please try again later");
        }
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody(required = false) AuthenticationRequest authenticationRequest) {
        try {
            User user = authService.loadUserByUsername(authenticationRequest.getUsername());
            boolean isCorrectPassword = passwordEncoder.matches(authenticationRequest.getPassword(),
                    user.getPassword());

            if (isCorrectPassword) {
                final String token = jwtUtil.generateToken(user);
                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                return ResponseEntity.status(403).body("Wrong password");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/info")
    public ResponseEntity getCurrentUserInfo() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    //NOTE: the password of all the users is raw, we have to decode it in userService
    @PutMapping("/info")
    public ResponseEntity updateCurrentUserInfo(@RequestBody User user) {
        User currentUser = authService.getCurrentUser();

        if (authService.updateCurrentUser(currentUser)) {
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(403).body("You cannot modify info of another user");
        }
    }
}
