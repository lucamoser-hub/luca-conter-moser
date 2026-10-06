package com.simulado.SAEP_LUCA.Controller;


import com.simulado.SAEP_LUCA.Entity.Usuario;
import com.simulado.SAEP_LUCA.Service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("erroLogin", "Falha de autenticação: Usuário ou senha inválidos!");
        }
        if (logout != null) {
            model.addAttribute("mensagemSucesso", "Sessão encerrada com sucesso.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String realizarLogin(@RequestParam("login") String login,
                                @RequestParam("senha") String senha,
                                HttpSession session,
                                Model model) {
        Optional<Usuario> usuarioOpt = usuarioService.autenticar(login, senha);

        if (usuarioOpt.isPresent()) {
            session.setAttribute("usuarioLogado", usuarioOpt.get());
            return "redirect:/principal";
        } else {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}