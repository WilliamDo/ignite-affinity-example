package com.ultimaspin.ignite;

public class Department {
    private final String title;

    public Department(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Department of " + title;
    }
}
