package com.tuorg.businesslogic.services.appointment;

public interface LockProvider {
    boolean acquire(String key, long timeoutMillis);
    void release(String key);
}
