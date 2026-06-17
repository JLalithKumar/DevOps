package com.example;

public class App {

    public static void main(String[] args) {

        GradeCalculator calculator = new GradeCalculator();

        int marks = 82;

        String grade = calculator.calculateGrade(marks);

        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
    }
}