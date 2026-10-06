package com.simulado.SAEP_LUCA.Repository;



import com.simulado.SAEP_LUCA.Entity.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    List<MovimentacaoEstoque> findAllByOrderByDataMovimentacaoDesc();
}