package com.simulado.SAEP_LUCA.Service;



import com.simulado.SAEP_LUCA.Entity.MovimentacaoEstoque;
import com.simulado.SAEP_LUCA.Entity.Produto;
import com.simulado.SAEP_LUCA.Entity.Usuario;
import com.simulado.SAEP_LUCA.Enums.TipoMovimentacao;
import com.simulado.SAEP_LUCA.Repository.MovimentacaoEstoqueRepository;
import com.simulado.SAEP_LUCA.Repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    @Transactional
    public String registrarMovimentacao(Long produtoId, TipoMovimentacao tipo, Integer quantidade, LocalDateTime data, Usuario usuario) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade da movimentação deve ser maior que zero.");
        }

        String alerta = null;

        if (tipo == TipoMovimentacao.ENTRADA) {
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);
        } else if (tipo == TipoMovimentacao.SAIDA) {
            if (produto.getQuantidadeEstoque() < quantidade) {
                throw new IllegalArgumentException("Quantidade insuficiente em estoque! Saldo atual: " + produto.getQuantidadeEstoque());
            }
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        }

        produtoRepository.save(produto);

        // Verificação automática de estoque mínimo (Requisito 7.1.4)
        if (produto.getQuantidadeEstoque() < produto.getEstoqueMinimo()) {
            alerta = "ALERTA DE ESTOQUE MÍNIMO: O produto '" + produto.getNome() +
                    "' está com apenas " + produto.getQuantidadeEstoque() +
                    " unidade(s) em estoque (Mínimo exigido: " + produto.getEstoqueMinimo() + ").";
        }

        // Registro para auditoria e rastreabilidade (Requisito 7.1.3)
        MovimentacaoEstoque mov = MovimentacaoEstoque.builder()
                .produto(produto)
                .tipo(tipo)
                .quantidade(quantidade)
                .dataMovimentacao(data != null ? data : LocalDateTime.now())
                .usuario(usuario)
                .build();

        movimentacaoRepository.save(mov);

        return alerta;
    }

    public List<MovimentacaoEstoque> listarHistorico() {
        return movimentacaoRepository.findAllByOrderByDataMovimentacaoDesc();
    }
}