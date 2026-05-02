package com.crustcloud.production.controller;

import com.crustcloud.production.dto.SettingsDTO;
import com.crustcloud.production.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<SettingsDTO.ProfileResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<SettingsDTO.ProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody SettingsDTO.ProfileUpdate request) {
        try {
            return ResponseEntity.ok(userService.updateProfile(authentication.getName(), request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(
            Authentication authentication,
            @RequestBody SettingsDTO.PasswordUpdate request) {
        try {
            userService.updatePassword(authentication.getName(), request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
