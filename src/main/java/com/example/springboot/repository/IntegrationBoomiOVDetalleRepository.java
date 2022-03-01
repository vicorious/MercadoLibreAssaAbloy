package com.example.springboot.repository;

import com.example.springboot.entity.IntegrationBoomiOVDetalle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface IntegrationBoomiOVDetalleRepository extends CrudRepository<IntegrationBoomiOVDetalle, Long>
{

}
