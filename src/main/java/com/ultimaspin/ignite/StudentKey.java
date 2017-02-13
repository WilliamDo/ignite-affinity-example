package com.ultimaspin.ignite;

public class StudentKey {

    private final int studentId;

    public StudentKey(int studentId) {
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentKey that = (StudentKey) o;

        return studentId == that.studentId;
    }

    @Override
    public int hashCode() {
        return studentId;
    }
}
