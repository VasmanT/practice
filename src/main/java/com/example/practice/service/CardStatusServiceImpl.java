package com.example.practice.service;

import com.example.practice.dto.mapping.CardStatusMapper;
import com.example.practice.repository.CardStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cardStatusService")
public class CardStatusServiceImpl implements ServiceDto {

    private final CardStatusRepository cardStatusRepository;
    private final CardStatusMapper cardStatusMapper;

    @Autowired
    public CardStatusServiceImpl(CardStatusRepository cardStatusRepository, CardStatusMapper cardStatusMapper) {
        this.cardStatusRepository = cardStatusRepository;
        this.cardStatusMapper = cardStatusMapper;
    }


    @Override
    public String getData() {
        return cardStatusRepository.getData();
    }

}