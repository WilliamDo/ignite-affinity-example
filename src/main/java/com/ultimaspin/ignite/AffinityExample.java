package com.ultimaspin.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AffinityExample {

    private static class DepartmentKeys {
        static final DepartmentKey COMPUTING = new DepartmentKey("Engineering", "Computing");
        static final DepartmentKey AERONAUTICS = new DepartmentKey("Engineering", "Aeronautics");
        static final DepartmentKey MATHEMATICS = new DepartmentKey("Natural Sciences", "Mathematics");
        static final DepartmentKey MATERIALS = new DepartmentKey("Natural Sciences", "Materials");
        static final DepartmentKey MEDICINE = new DepartmentKey("Medicine", "Medicine");
    }

    private static final Map<DepartmentKey, Department> DEPARTMENTS = new HashMap<DepartmentKey, Department>() {{
        put(DepartmentKeys.COMPUTING, new Department("Computing"));
        put(DepartmentKeys.AERONAUTICS, new Department("Aeronautics"));
        put(DepartmentKeys.MATHEMATICS, new Department("Mathematics"));
        put(DepartmentKeys.MATERIALS, new Department("Materials"));
        put(DepartmentKeys.MEDICINE, new Department("Medicine"));
    }};

    static class StudentKeys {
        static final StudentKey STUDENT_1 = new StudentKey(1, DepartmentKeys.MEDICINE);
        static final StudentKey STUDENT_2 = new StudentKey(2, DepartmentKeys.MATERIALS);
        static final StudentKey STUDENT_3 = new StudentKey(3, DepartmentKeys.MATHEMATICS);
        static final StudentKey STUDENT_4 = new StudentKey(4, DepartmentKeys.AERONAUTICS);
        static final StudentKey STUDENT_5 = new StudentKey(5, DepartmentKeys.COMPUTING);
    }

    static final Map<StudentKey, Student> STUDENTS = new HashMap<StudentKey, Student>() {{
       put(StudentKeys.STUDENT_1, new Student("Charlie", "Brown"));
       put(StudentKeys.STUDENT_2, new Student("SpongeBob", "SquarePants"));
       put(StudentKeys.STUDENT_3, new Student("Top", "Cat"));
       put(StudentKeys.STUDENT_4, new Student("Johnny", "Bravo"));
       put(StudentKeys.STUDENT_5, new Student("Yogi", "Bear"));
    }};

    public static void main(String[] args) {
        Ignition.setClientMode(true);
        Ignite ignite = Ignition.start("ignite-config.xml");

        CacheConfiguration<StudentKey, Student> studentCacheCfg = new CacheConfiguration<>();
        studentCacheCfg.setName("studentCache");
        studentCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        studentCacheCfg.setBackups(0);
        IgniteCache<StudentKey, Student> studentCache = ignite.getOrCreateCache(studentCacheCfg);

        CacheConfiguration<DepartmentKey, Department> departmentCacheCfg = new CacheConfiguration<>();
        departmentCacheCfg.setName("departmentCache");
        departmentCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        departmentCacheCfg.setBackups(0);
        IgniteCache<DepartmentKey, Department> departmentCache = ignite.getOrCreateCache(departmentCacheCfg);

        studentCache.putAll(STUDENTS);
        departmentCache.putAll(DEPARTMENTS);

        Affinity<StudentKey> studentAffinity = ignite.affinity("studentCache");
        Map<StudentKey, ClusterNode> studentLocations = STUDENTS.keySet()
                .stream()
                .collect(Collectors.toMap(k -> k, studentAffinity::mapKeyToNode));

        Affinity<DepartmentKey> departmentAffinity = ignite.affinity("departmentCache");
        Map<DepartmentKey, ClusterNode> departmentLocations = DEPARTMENTS.keySet()
                .stream()
                .collect(Collectors.toMap(k -> k, departmentAffinity::mapKeyToNode));

        Map<ClusterNode, List<StudentKey>> nodeToStudents = new HashMap<>();
        studentLocations.forEach((studentKey, node) -> {
            if (!nodeToStudents.containsKey(node)) {
                nodeToStudents.put(node, new ArrayList<>());
            }
            nodeToStudents.get(node).add(studentKey);
        });

        nodeToStudents.forEach((node, studentKeys) -> {
            System.out.println("=== Node (" + node.id() + ") ===");
            studentKeys.forEach(key -> System.out.println(studentCache.get(key).getFullName()));
        });

        Map<ClusterNode, List<DepartmentKey>> nodeToDepartments = new HashMap<>();
        departmentLocations.forEach((departmentKey, node) -> {
            if (!nodeToDepartments.containsKey(node)) {
                nodeToDepartments.put(node, new ArrayList<>());
            }
            nodeToDepartments.get(node).add(departmentKey);
        });

        nodeToDepartments.forEach((node, departmentKeys) -> {
            System.out.println("=== Node (" + node.id() + ") ===");
            departmentKeys.forEach(key -> System.out.println(departmentCache.get(key)));
        });

        ignite.close();
    }

}
