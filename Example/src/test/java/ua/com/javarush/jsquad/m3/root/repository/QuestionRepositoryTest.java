package ua.com.javarush.jsquad.m3.root.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionRepositoryTest {

  @Test
  void getAll() {
    assertEquals(1, QuestionRepository.getInstance().getAll().size());
  }
}