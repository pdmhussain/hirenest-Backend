package com.hirenest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hirenest.entity.Domain;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {

    Optional<Domain> findByName(String name);

    boolean existsByName(String name);
}