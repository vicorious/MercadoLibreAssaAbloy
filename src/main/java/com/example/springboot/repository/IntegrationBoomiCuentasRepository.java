package com.example.springboot.repository;


import com.example.springboot.entity.IntegracionBoomiCuentas;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface IntegrationBoomiCuentasRepository extends CrudRepository<IntegracionBoomiCuentas, Long>{
}
