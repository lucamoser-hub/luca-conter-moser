package com.simulado.SAEP_LUCA.Controller;

import com.simulado.SAEP_LUCA.Entity.Usuario;
import com.simulado.SAEP_LUCA.Enums.TipoMovimentacao;
import com.simulado.SAEP_LUCA.Service.EstoqueService;
import com.simulado.SAEP_LUCA.Service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueController {

    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    @GetMapping
    public String telaEstoque(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        // Ordenação alfabética exigida no requisito 7.1.1
        model.addAttribute("produtos", produtoService.listarOrdenadoAlfabeticamente());
        model.addAttribute("historico", estoqueService.listarHistorico());
        model.addAttribute("usuario", usuario);
        return "estoque";
    }

    @PostMapping("/movimentar")
    public String movimentarEstoque(@RequestParam("produtoId") Long produtoId,
                                    @RequestParam("tipo") TipoMovimentacao tipo,
                                    @RequestParam("quantidade") Integer quantidade,
                                    @RequestParam(value = "dataMovimentacao", required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataMovimentacao,
                                    HttpSession session,
                                    RedirectAttributes redirect) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        try {
            if (dataMovimentacao == null) {
                dataMovimentacao = LocalDateTime.now();
            }

            // ==========================================
            // VALIDAÇÃO EXIGIDA PELA PROFESSORA AQUI:
            // ==========================================
            if (dataMovimentacao.getYear() < 2026) {
                redirect.addFlashAttribute("erro", "Data inválida! Não é permitido registrar movimentações em anos anteriores a 2026.");
                return "redirect:/estoque";
            }
            // ==========================================

            String alerta = estoqueService.registrarMovimentacao(produtoId, tipo, quantidade, dataMovimentacao, usuario);

            if (alerta != null) {
                redirect.addFlashAttribute("alertaMinimo", alerta);
            } else {
                redirect.addFlashAttribute("sucesso", "Movimentação registrada com sucesso!");
            }
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }

        return "redirect:/estoque";
    }
}