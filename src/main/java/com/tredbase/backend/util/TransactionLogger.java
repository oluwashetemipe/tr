package com.tredbase.backend.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
public class TransactionLogger {

    public void registerTransactionLogger(String transactionalOperation) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status == TransactionSynchronization.STATUS_COMMITTED) {
                        log.info("Transaction committed for: {}", transactionalOperation);
                    } else if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                        log.warn("Transaction rolled back for: {}", transactionalOperation);
                    }
                }
            });
        } else {
            log.warn("No active transaction to register logger for: {}", transactionalOperation);
        }
    }
}
