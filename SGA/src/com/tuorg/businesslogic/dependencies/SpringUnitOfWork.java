package com.tuorg.businesslogic.dependencies;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class SpringUnitOfWork implements IUnitOfWorkDependency {
    private final PlatformTransactionManager txManager;
    private TransactionStatus status;

    public SpringUnitOfWork(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    @Override
    public void begin() {
        status = txManager.getTransaction(new DefaultTransactionDefinition());
    }

    @Override
    public void commit() {
        if (status != null && !status.isCompleted()) txManager.commit(status);
    }

    @Override
    public void rollback() {
        if (status != null && !status.isCompleted()) txManager.rollback(status);
    }
}
