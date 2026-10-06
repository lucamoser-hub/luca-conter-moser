package com.simulado.SAEP_LUCA.Controller;


import com.simulado.SAEP_LUCA.Entity.Produto;
import com.simulado.SAEP_LUCA.Entity.Usuario;
import com.simulado.SAEP_LUCA.Service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public String listarProdutos(@RequestParam(value = "termo", required = false) String termo,
                                 HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        List<Produto> produtos = produtoService.buscarPorTermo(termo);
        model.addAttribute("produtos", produtos);
        model.addAttribute("termo", termo);
        model.addAttribute("usuario", usuario);
        return "produtos";
    }

    @GetMapping("/novo")
    public String novoForm(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";
        model.addAttribute("produto", new Produto());
        return "produto-form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable("id") Long id, HttpSession session, Model model, RedirectAttributes redirect) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        return produtoService.buscarPorId(id)
                .map(p -> {
                    model.addAttribute("produto", p);
                    return "produto-form";
                })
                .orElseGet(() -> {
                    redirect.addFlashAttribute("erro", "Produto não encontrado.");
                    return "redirect:/produtos";
                });
    }

    @PostMapping("/salvar")
    public String salvarProduto(@Valid @ModelAttribute("produto") Produto produto,
                                BindingResult result,
                                HttpSession session,
                                RedirectAttributes redirect) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        if (result.hasErrors()) {
            return "produto-form";
        }

        produtoService.salvar(produto);
        redirect.addFlashAttribute("sucesso", "Produto salvo com sucesso!");
        return "redirect:/produtos";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirect) {
        if (session.getAttribute("usuarioLogado") == null) return "redirect:/login";

        try {
            produtoService.excluir(id);
            redirect.addFlashAttribute("sucesso", "Produto excluído com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", "Erro ao excluir produto. Verifique se existem movimentações associadas.");
        }
        return "redirect:/produtos";
    }
}