package com.tuorg.businesslogic.services.appointment;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryLockProvider implements LockProvider {
    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    @Override
    public boolean acquire(String key, long timeoutMillis) {
        return locks.putIfAbsent(key, new Object()) == null;
    }

    @Override
    public void release(String key) {
        locks.remove(key);
    }
}
