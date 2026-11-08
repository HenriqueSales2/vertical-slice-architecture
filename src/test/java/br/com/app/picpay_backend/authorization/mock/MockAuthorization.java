package br.com.app.picpay_backend.authorization.mock;

import br.com.app.authorization.entity.Authorization;

public class MockAuthorization {

    public Authorization mockAuthorization() {
        return buildMockAuthorization();
    }

    private Authorization buildMockAuthorization() {
        return new Authorization("Authorized");
    }
}