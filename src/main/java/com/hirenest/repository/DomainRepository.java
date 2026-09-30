package com.hirenest.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hirenest.entity.Domain;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    Optional<Domain> findByName(String name);
}
