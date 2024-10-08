package com.faitoncodes.core_processor_service.repository;

import com.faitoncodes.core_processor_service.dao.AgrupamentoUserClass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgrupamentoUserClassRepository extends CrudRepository<AgrupamentoUserClass, Long> {
}
