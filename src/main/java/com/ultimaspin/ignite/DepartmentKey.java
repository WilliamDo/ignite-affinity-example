package com.ultimaspin.ignite;

public class DepartmentKey {
    private final String faculty;

    private final String department;

    public DepartmentKey(String faculty, String department) {
        this.faculty = faculty;
        this.department = department;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepartmentKey that = (DepartmentKey) o;

        if (faculty != null ? !faculty.equals(that.faculty) : that.faculty != null) return false;
        return department != null ? department.equals(that.department) : that.department == null;
    }

    @Override
    public int hashCode() {
        int result = faculty != null ? faculty.hashCode() : 0;
        result = 31 * result + (department != null ? department.hashCode() : 0);
        return result;
    }

}
