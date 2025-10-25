package com.tuorg.businesslogic.services.base;

import com.tuorg.businesslogic.dependencies.IUnitOfWorkDependency;
import org.springframework.transaction.annotation.Transactional;

public abstract class AppServiceBase {
    protected final IUnitOfWorkDependency uow;

    protected AppServiceBase(IUnitOfWorkDependency uow) {
        this.uow = uow;
    }

    @Transactional
    protected void runInTransaction(Runnable action) {
        action.run();
    }
}
