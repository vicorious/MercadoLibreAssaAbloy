package com.example.springboot.repository;

import com.example.springboot.entity.IntegracionBoomiOVEncabezado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface IntegrationBoomiOVEncabezadoRepository extends CrudRepository<IntegracionBoomiOVEncabezado, Long>{
}
