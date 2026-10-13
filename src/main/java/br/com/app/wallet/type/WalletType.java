package br.com.app.wallet.type;

public enum WalletType {
    COMMON(1), SHOPKEEPER(2);

    private int value;

    WalletType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}