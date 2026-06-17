package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GradeCalculatorTest {

    private final GradeCalculator calculator =
            new GradeCalculator();

    @Test
    void shouldReturnGradeA() {
        assertEquals("A", calculator.calculateGrade(95));
    }

    @Test
    void shouldReturnGradeB() {
        assertEquals("B", calculator.calculateGrade(80));
    }

    @Test
    void shouldReturnGradeC() {
        assertEquals("C", calculator.calculateGrade(65));
    }

    @Test
    void shouldReturnGradeD() {
        assertEquals("D", calculator.calculateGrade(45));
    }

    @Test
    void shouldReturnGradeF() {
        assertEquals("F", calculator.calculateGrade(20));
    }

    @Test
    void shouldThrowExceptionForNegativeMarks() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateGrade(-5)
        );
    }

    @Test
    void shouldThrowExceptionForMarksAbove100() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateGrade(110)
        );
    }
}