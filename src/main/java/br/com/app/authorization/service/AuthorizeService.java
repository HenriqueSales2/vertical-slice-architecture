package br.com.app.authorization.service;

import br.com.app.authorization.exception.UnauthorizedTransactionException;
import br.com.app.authorization.entity.Authorization;
import br.com.app.transaction.entity.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthorizeService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeService.class);

    private final RestClient restClient;

    public AuthorizeService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://quickmock.dev/m/uYBZwE6cDmAc")
                .build();

    }

    public void authorize(Transaction transaction) {
        logger.info("Authorizing transaction: {}", transaction);
        var response = restClient.get()
                .retrieve()
                .toEntity(Authorization.class);

        if (response.getStatusCode().isError() || !response.getBody().isAuthorized())
            throw new UnauthorizedTransactionException("Unauthorized transaction!");

        logger.info("Transaction authorized: {}", transaction);
    }
}