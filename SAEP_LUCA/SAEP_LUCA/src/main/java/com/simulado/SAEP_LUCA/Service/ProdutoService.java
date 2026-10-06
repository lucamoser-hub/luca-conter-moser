package com.simulado.SAEP_LUCA.Service;



import com.simulado.SAEP_LUCA.Entity.Produto;
import com.simulado.SAEP_LUCA.Repository.ProdutoRepository;
import com.simulado.SAEP_LUCA.Util.OrdenacaoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public List<Produto> listarOrdenadoAlfabeticamente() {
        List<Produto> produtos = produtoRepository.findAll();
        // Aplicação do Algoritmo de Ordenação QuickSort (Requisito 7.1.1)
        OrdenacaoUtil.ordenarProdutosPorNome(produtos);
        return produtos;
    }

    public List<Produto> buscarPorTermo(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodos();
        }
        return produtoRepository.buscarPorTermo(termo);
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }
}