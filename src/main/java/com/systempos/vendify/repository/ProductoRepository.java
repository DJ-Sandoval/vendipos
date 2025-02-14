package com.systempos.vendify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.systempos.vendify.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
