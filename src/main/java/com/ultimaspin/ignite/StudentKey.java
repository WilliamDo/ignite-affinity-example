package com.ultimaspin.ignite;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

public class StudentKey {

    private final int studentId;

    @AffinityKeyMapped
    private final DepartmentKey departmentKey;

    public StudentKey(int studentId, DepartmentKey departmentKey) {
        this.studentId = studentId;
        this.departmentKey = departmentKey;
    }

    public int getStudentId() {
        return studentId;
    }

    public DepartmentKey getDepartmentKey() {
        return this.departmentKey;
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
