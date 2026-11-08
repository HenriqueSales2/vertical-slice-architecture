package br.com.app.picpay_backend.authorization.service;

import br.com.app.authorization.entity.Authorization;
import br.com.app.authorization.exception.UnauthorizedTransactionException;
import br.com.app.authorization.service.AuthorizeService;

import br.com.app.picpay_backend.authorization.mock.MockAuthorization;
import br.com.app.picpay_backend.transaction.mock.MockTransaction;

import nl.altindag.log.LogCaptor;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorizationServiceTest {

    MockTransaction input;
    MockAuthorization authorization;

    @InjectMocks
    private AuthorizeService service;

    @BeforeEach
    void setUp() {
        input = new MockTransaction();
        authorization = new MockAuthorization();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    void isAuthorized() {
        assertEquals(new Authorization("Authorized"), authorization.mockAuthorization());
    }

    @Test
    @Order(2)
    void authorize() {
        LogCaptor logCaptor = LogCaptor.forClass(AuthorizeService.class);

        var transaction = input.mockEntity(0);

        service.authorize(transaction);

        assertThat(logCaptor.getLogs().contains("Authorizing transaction: " + transaction));
        assertThat(logCaptor.getLogs().contains("Transaction authorized: " + transaction));
    }

    @Test
    @Order(3)
    void authorizeExceptionErrorUrl() {
        LogCaptor logCaptor = LogCaptor.forClass(AuthorizeService.class);

        RestClient restClient = Mockito.mock(RestClient.class);

        when(restClient.get()).thenThrow(new UnauthorizedTransactionException("Unauthorized"));

        ReflectionTestUtils.setField(service, "restClient", restClient);

        var transaction = input.mockEntity(0);

        assertThrows(UnauthorizedTransactionException.class, () -> service.authorize(transaction));

        assertThat(logCaptor.getLogs().contains("Authorizing transaction: " + transaction));
    }
}