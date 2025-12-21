package com.example.practice.service;

import com.example.practice.repository.PlayerRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service("playerService")
@Profile("prod")
public class PlayerServiceProdImpl extends PlayerServiceImpl{

    public PlayerServiceProdImpl(PlayerRepository repository) {
        super(repository);
    }

    @Override
    public String getData() {
        String original = super.getData();
        return "[PRODUCTION] " + original + " (Optimized for performance)";
    }
}