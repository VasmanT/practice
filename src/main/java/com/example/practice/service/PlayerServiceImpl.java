package com.example.practice.service;

import com.example.practice.model.Player;
import com.example.practice.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("playerService")
public class PlayerServiceImpl implements ServiceInterface<Long, Player> {

    private PlayerRepository repository;

    public PlayerServiceImpl(PlayerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Player updateById(Player entity) {
        if(entity == null || entity.getId() == null){
            throw new EntityNotFoundException("Передаваемые параметры не должны быть нулевыми");
        }
        if(!repository.existsById(entity.getId())) {
            throw new EntityNotFoundException("Игрок с id " + entity.getId() + " not found");
        }
        return repository.save(entity);
    }

    @Override
    public Player getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player with ID " + id + " not found"));}

    @Override
    public List<Player> getAll() {
        return repository.findAll();
    }

    @Override
    public void deleteByID(Long id) {
        if(id == null){
            throw new EntityNotFoundException("Передаваемые параметры не должны быть нулевыми");
        }
        if(!repository.existsById(id)) {
            throw new EntityNotFoundException("Игрок с id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public Player addNew(Player entity) {
        if(entity == null){
            throw new EntityNotFoundException("Передаваемые параметры не должны быть нулевыми");
        }

        // Убедитесь, что ID null для новой сущности -- вот тут непонятно
        entity.setId(null);
//        ----

        return repository.save(entity);
    }
}
