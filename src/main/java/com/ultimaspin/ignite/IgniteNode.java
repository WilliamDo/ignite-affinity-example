package com.ultimaspin.ignite;

import org.apache.ignite.Ignition;

public class IgniteNode {
    public static void main(String[] args) {
        Ignition.start("ignite-config.xml");
    }
}
