package com.example.practice.service;

import com.example.practice.dto.mapping.CardStatusMapper;
import com.example.practice.repository.CardStatusRepository;
import com.example.practice.repository.FirstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("firstService")
public class FirstServiceImpl {
//public class FirstServiceImpl implements ServiceDto {
    private final FirstRepository firstRepository;

    public FirstServiceImpl(FirstRepository firstRepository) {
        this.firstRepository = firstRepository;
    }


//    @Override
    public String getData() {
        return firstRepository.getData();
    }

}
