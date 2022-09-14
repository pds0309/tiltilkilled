package com.pds.javatest.mock;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class MyRepository {
    private List<My> myList = new ArrayList<>(
            Arrays.stream((new My[]{new My("김갑환", 25), new My("최번개", 35)}))
                    .toList());

    public Optional<My> getByName(String name) {
        return myList.stream()
                .filter(my -> my.getName().equals(name))
                .findFirst();
    }

    public My save(My my) {
        myList.add(my);
        return my;
    }
}
