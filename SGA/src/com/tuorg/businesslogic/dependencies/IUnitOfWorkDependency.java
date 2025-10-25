package com.tuorg.businesslogic.dependencies;

public interface IUnitOfWorkDependency {
    void begin();
    void commit();
    void rollback();
}
