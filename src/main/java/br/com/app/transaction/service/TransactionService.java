package br.com.app.transaction.service;

import br.com.app.authorization.service.AuthorizeService;

import br.com.app.notification.service.NotificationService;

import br.com.app.transaction.repository.TransactionRepository;
import br.com.app.transaction.entity.Transaction;
import br.com.app.transaction.exception.InvalidTransactionException;

import br.com.app.wallet.entity.Wallet;
import br.com.app.wallet.repository.WalletRepository;
import br.com.app.wallet.type.WalletType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizeService authorizeService;
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizeService authorizeService, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizeService = authorizeService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transaction create(Transaction transaction) {

        validate(transaction);

        Transaction newTransaction = transactionRepository.save(transaction);

        Wallet walletPayer = walletRepository.findById(transaction.payer()).get();
        Wallet walletPayee = walletRepository.findById(transaction.payee()).get();
        walletRepository.save(walletPayer.debit(transaction.value()));
        walletRepository.save(walletPayee.credit(transaction.value()));

        authorizeService.authorize(newTransaction);

        notificationService.notify(transaction);

        return newTransaction;
    }

    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.payee())
                .map(payee -> walletRepository.findById(transaction.payer())
                        .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                        .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction)));
    }

    private static boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMMON.getValue()
                && payer.balance().compareTo(transaction.value()) >= 0
                && !payer.id().equals(transaction.payee());
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}