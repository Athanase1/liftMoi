package com.example.service_avis;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvisRepository extends JpaRepository<Avis, Long> {

    List<Avis> findByUId(String uId);
    List<Avis> findByVId(String vId);
}
