DELETE FROM TRANSACTIONS;

DELETE FROM WALLETS;

INSERT INTO
    WALLETS (
             ID,
             FULL_NAME,
             CPF,
             EMAIL,
             "PASSWORD",
             "TYPE",
             BALANCE
            )
    VALUES (
            1,
            'John Doe',
            11111111111,
            'johndoe@test.com',
            'user123',
            1,
            1000.00
     );

INSERT INTO
    WALLETS (
             ID,
             FULL_NAME,
             CPF,
             EMAIL,
             "PASSWORD",
             "TYPE",
             BALANCE
            )
    VALUES (
            2,
            'Mary Doe',
            22222222222,
            'marydoe@test.com',
            'admin123',
            2,
            1000.00
     );