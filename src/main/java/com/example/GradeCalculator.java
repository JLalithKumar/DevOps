package com.example;

public class GradeCalculator {

    public String calculateGrade(int marks) {

        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks must be between 0 and 100");
        }

        if (marks >= 90) {
            return "A";
        } else if (marks >= 75) {
            return "B";
        } else if (marks >= 60) {
            return "C";
        } else if (marks >= 40) {
            return "D";
        } else {
            return "F";
        }
    }
}