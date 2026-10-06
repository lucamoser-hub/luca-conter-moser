package com.simulado.SAEP_LUCA.Util;


import com.simulado.SAEP_LUCA.Entity.Produto;

import java.util.List;

public class OrdenacaoUtil {

    /**
     * Algoritmo QuickSort para ordenação alfabética por Nome de Produto.
     * Atende estritamente ao Requisito 7.1.1 do SAEP.
     */
    public static void ordenarProdutosPorNome(List<Produto> produtos) {
        if (produtos == null || produtos.size() <= 1) {
            return;
        }
        quickSort(produtos, 0, produtos.size() - 1);
    }

    private static void quickSort(List<Produto> lista, int inicio, int fim) {
        if (inicio < fim) {
            int pivoIndex = particionar(lista, inicio, fim);
            quickSort(lista, inicio, pivoIndex - 1);
            quickSort(lista, pivoIndex + 1, fim);
        }
    }

    private static int particionar(List<Produto> lista, int inicio, int fim) {
        String pivo = lista.get(fim).getNome().toLowerCase();
        int i = inicio - 1;

        for (int j = inicio; j < fim; j++) {
            if (lista.get(j).getNome().toLowerCase().compareTo(pivo) <= 0) {
                i++;
                trocar(lista, i, j);
            }
        }
        trocar(lista, i + 1, fim);
        return i + 1;
    }

    private static void trocar(List<Produto> lista, int i, int j) {
        Produto temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }
}