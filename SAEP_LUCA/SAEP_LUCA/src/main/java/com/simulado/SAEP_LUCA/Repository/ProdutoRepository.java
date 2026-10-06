package com.simulado.SAEP_LUCA.Repository;



import com.simulado.SAEP_LUCA.Entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(p.categoria) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Produto> buscarPorTermo(@Param("termo") String termo);
}