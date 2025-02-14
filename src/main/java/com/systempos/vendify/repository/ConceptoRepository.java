package com.systempos.vendify.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systempos.vendify.model.Concepto;

@Repository
public interface ConceptoRepository extends JpaRepository<Concepto, Long> {

}
