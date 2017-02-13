package com.ultimaspin.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.configuration.CacheConfiguration;

public class AffinityExample {

    public static void main(String[] args) {
        Ignition.setClientMode(true);
        Ignite ignite = Ignition.start("ignite-config.xml");
        CacheConfiguration<AffinityKey<StudentKey>, Student> studentCacheCfg = new CacheConfiguration<>();
        studentCacheCfg.setName("studentCache");
        studentCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        studentCacheCfg.setBackups(0);
        IgniteCache<AffinityKey<StudentKey>, Student> studentCache = ignite.getOrCreateCache(studentCacheCfg);

        CacheConfiguration<DepartmentKey, Department> departmentCacheCfg = new CacheConfiguration<>();
        departmentCacheCfg.setName("departmentCache");
        departmentCacheCfg.setCacheMode(CacheMode.PARTITIONED);
        departmentCacheCfg.setBackups(0);
        IgniteCache<DepartmentKey, Department> departmentCache = ignite.getOrCreateCache(departmentCacheCfg);

        departmentCache.put(new DepartmentKey("Engineering", "Computing"), new Department("Computing"));
        departmentCache.put(new DepartmentKey("Engineering", "Aeronautics"), new Department("Aeronautics"));
        departmentCache.put(new DepartmentKey("Natural Sciences", "Mathematics"), new Department("Mathematics"));
        departmentCache.put(new DepartmentKey("Natural Sciences", "Materials"), new Department("Materials"));
        departmentCache.put(new DepartmentKey("Medicine", "Medicine"), new Department("Medicine"));

        ignite.close();
    }

}
