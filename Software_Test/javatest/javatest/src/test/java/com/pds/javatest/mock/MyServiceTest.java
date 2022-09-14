package com.pds.javatest.mock;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyServiceTest {

    private final MyRepository myRepository = mock(MyRepository.class);

    private final MyService myService = new MyService(myRepository);

    @Test
    void findByNameWithExistsNameShouldReturnMy() {
        My mockMy = new My("킴갑환", 22);
        when(myRepository.getByName("킴갑환")).thenReturn(Optional.of(mockMy));

        My my = myService.getByName("킴갑환");
        assertEquals(mockMy, my);
    }

    @Test
    void findByNameWithNotExistsNameShouldThrowRuntimeException() {
        when(myRepository.getByName("킴갑환")).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> myService.getByName("킴갑환"));
    }

    @Test
    void insertNewMyShouldSuccess() {
        My my = new My("킴갑환", 25);
        when(myRepository.getByName("킴갑환"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(my));

        myService.insert("킴갑환", 25);
        verify(myRepository).save(any(My.class));
        assertThrows(RuntimeException.class, () -> myService.insert("킴갑환", 25));
    }

    @Test
    void insertNewMyShouldSuccessWithBDDStyle() {
        My my = new My("킴갑환", 25);
        BDDMockito.given(myRepository.getByName("킴갑환"))
                .willReturn(Optional.empty())
                .willReturn(Optional.of(my));

        myService.insert("킴갑환", 25);
        BDDMockito.then(myRepository).should(times(1)).save(any(My.class));
        assertThrows(RuntimeException.class, () -> myService.insert("킴갑환", 25));
    }
}