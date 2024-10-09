package com.faitoncodes.core_processor_service.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public boolean existsByUserId(Long userId) {
        String sql = "SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM public.usuario u where u.user_id = :userId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);
        return (boolean) query.getSingleResult();
    }

    public String getTeacherName(Long userId){
        String sql = "SELECT u.nome from public.usuario u where u.user_id = :userId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);
        return (String) query.getSingleResult();
    }
}
