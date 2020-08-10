package com.bruno.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bruno.cursomc.dominio.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    @Transactional(readOnly=true)
    public List<Estado> findAllByOrderByNome(); // retornar todos os estados retornando por nome
}
