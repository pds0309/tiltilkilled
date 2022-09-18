package com.pds.javatest.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Slf4j
class DockerComposeMemberRepoTest {

    @Autowired
    private MemberRepository memberRepository;

    @Container
    static DockerComposeContainer<?> dockerComposeContainer =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"));

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
    }

    @Test
    void insertTest() {
        Member member = Member.builder().name("김갑환").build();
        Member savedMember = memberRepository.save(member);
        assertEquals(member, savedMember);
    }

    @Test
    void findByIdTest() {
        assertNull(memberRepository.findById(1L).orElse(null));
    }
}
