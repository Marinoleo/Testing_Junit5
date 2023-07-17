package org.example;

import java.math.BigDecimal;

public class Cuenta {
    private Banco banco;
    private String persona;
    private BigDecimal saldo;

    public Cuenta() {
    }
    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void debito (BigDecimal monto) {
        saldo = saldo.subtract(monto);
    }
    public void credito (BigDecimal monto) {
       saldo = saldo.add(monto);
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }
}
