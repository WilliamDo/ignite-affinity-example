package com.ultimaspin.ignite;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class NodeData {

    private final String nodeId;
    private final Map<StudentKey, Student> students = new HashMap<>();
    private final Map<DepartmentKey, Department> departments = new HashMap<>();

    public NodeData(String nodeId, Map<StudentKey, Student> students, Map<DepartmentKey, Department> departments) {
        this.nodeId = nodeId;
        this.students.putAll(students);
        this.departments.putAll(departments);
    }

    public void print(OutputStream outputStream) {
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println("=== Node (" + nodeId + ") ===");
        students.forEach((key, student) -> writer.println(student.getFullName()));
        departments.forEach((key, department) -> writer.println(department));
        writer.flush();
    }


}
