package com.example.demo.repository;

import com.example.demo.model.Player;
import com.example.demo.model.Signing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SigningsRepository extends JpaRepository<Signing, Long> {

    List<Signing> findByPlayer(Player player);
}
