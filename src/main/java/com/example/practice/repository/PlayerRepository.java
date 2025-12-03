package com.example.practice.repository;

import com.example.practice.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("playerRepository")
public interface PlayerRepository extends JpaRepository<Player, Long> {
    default String getData() {
        return "крутые данные, - version2(03.12.2025) "+ count();
    }
}
