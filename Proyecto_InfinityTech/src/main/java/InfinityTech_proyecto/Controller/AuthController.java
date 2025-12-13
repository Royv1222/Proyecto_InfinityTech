/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.controller;

import InfinityTech_proyecto.domain.LoginForm;
import InfinityTech_proyecto.domain.Rol;
import InfinityTech_proyecto.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping({"/", "/login"})
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm loginForm, HttpSession session, Model model) {
        try {
            Rol rol = authService.login(loginForm.getUsuario(), loginForm.getPassword());

            session.setAttribute("rol", rol);
            session.setAttribute("usuario", loginForm.getUsuario().trim());

            if (rol == Rol.ADMIN) return "redirect:/admin/tickets";
            if (rol == Rol.TECNICO) return "redirect:/tecnico/tickets";
            return "redirect:/cliente";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}