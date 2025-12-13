/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.service;

import InfinityTech_proyecto.domain.Rol;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Rol login(String usuario, String password) {
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el usuario.");
        }
        if (!"123".equals(password)) {
            throw new IllegalArgumentException("Contraseña inválida. Use 123.");
        }

        String u = usuario.trim().toLowerCase();

        if (u.equals("admin")) return Rol.ADMIN;
        if (u.equals("tecnico")) return Rol.TECNICO;

        // Todo lo demás = cliente (simple)
        return Rol.CLIENTE;
    }
}
