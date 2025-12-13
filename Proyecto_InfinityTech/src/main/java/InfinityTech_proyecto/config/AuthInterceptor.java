/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.config;

import InfinityTech_proyecto.domain.Rol;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Permitir login y recursos estáticos
        if (uri.startsWith("/login") || uri.startsWith("/error")
                || uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/img")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("rol") == null) {
            response.sendRedirect("/login");
            return false;
        }

        Rol rol = (Rol) session.getAttribute("rol");

        // Admin entra a todo
        if (rol == Rol.ADMIN) return true;

        // Restricciones por prefijo de ruta
        if (uri.startsWith("/admin")) {
            response.sendRedirect("/login");
            return false;
        }
        if (uri.startsWith("/tecnico") && rol != Rol.TECNICO) {
            response.sendRedirect("/login");
            return false;
        }
        if (uri.startsWith("/cliente") && rol != Rol.CLIENTE) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
