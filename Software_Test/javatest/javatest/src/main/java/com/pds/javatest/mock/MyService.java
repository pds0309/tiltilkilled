package com.pds.javatest.mock;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyService {

    private final MyRepository myRepository;

    public MyService(MyRepository myRepository) {
        this.myRepository = myRepository;
    }

    public My getByName(String name) {
        return myRepository.getByName(name)
                .orElseThrow(() -> new RuntimeException(""));
    }

    public void insert(String name, int age) {
        myRepository.getByName(name)
                .ifPresent(my -> {
                    throw new RuntimeException("");
                });
        myRepository.save(new My(name, age));
    }
}
