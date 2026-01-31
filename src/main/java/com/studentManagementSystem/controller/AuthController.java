package com.studentManagementSystem.controller;


import com.studentManagementSystem.dto.UserDTO;
import com.studentManagementSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.getUserDTOByUsername(username);
        model.addAttribute("user", userDTO);
        return "profile";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.getUserDTOByUsername(username);
        model.addAttribute("user", userDTO);
        return "settings";
    }

    @PostMapping("/settings/change-password")
    public String changePassword(@ModelAttribute UserDTO userDTO, 
                                  RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // Validate new password and confirmation match
            if (!userDTO.getNewPassword().equals(userDTO.getConfirmPassword())) {
                redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
                return "redirect:/settings";
            }

            // Validate password length
            if (userDTO.getNewPassword().length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters!");
                return "redirect:/settings";
            }

            // Update password
            userService.updatePassword(username, userDTO.getNewPassword());
            redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
            return "redirect:/settings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error changing password: " + e.getMessage());
            return "redirect:/settings";
        }
    }
}
