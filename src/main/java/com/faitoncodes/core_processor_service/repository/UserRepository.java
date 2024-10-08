package com.faitoncodes.core_processor_service.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    @Query("SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM public.user u where u.user_id = ?1")
    boolean existsUserId(Long userId);
}
