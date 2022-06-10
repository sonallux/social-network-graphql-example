package de.sonallux.example.graphql.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static final AtomicInteger COUNTER = new AtomicInteger(4700);

    private IdGenerator() {
    }

    public static String nextId() {
        int id = COUNTER.getAndIncrement();
        return String.valueOf(id);
    }
}
