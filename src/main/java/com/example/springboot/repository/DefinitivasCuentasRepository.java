package com.example.springboot.repository;

import com.example.springboot.entity.DefinitivasCuentas;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface DefinitivasCuentasRepository extends CrudRepository<DefinitivasCuentas, Long>{
}
