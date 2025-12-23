package com.financas.julio.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "usuario_id", nullable = false)
    private User user;

    @Column(name = "nome",nullable = false, length = 120)
    private String name;

    @Column(name = "tipo_conta", nullable = false, length = 100)
    private String tipoConta;

    @Column(name = "saldo_inicial", nullable = false)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_atual", precision = 12, scale = 2)
    private BigDecimal saldoAtual = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public Conta() {
    }

    public Conta(Long id, User user, String name, String tipoConta, BigDecimal saldoInicial, BigDecimal saldoAtual, LocalDateTime createAt, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.tipoConta = tipoConta;
        this.saldoInicial = saldoInicial;
        this.saldoAtual = saldoAtual;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public void aplicarTransacao(TipoTransacao tipo, BigDecimal valor) {
        if (tipo == TipoTransacao.RECEITA) {
            this.saldoAtual = this.saldoAtual.add(valor);
        } else {
            this.saldoAtual = this.saldoAtual.subtract(valor);
        }
    }

    public void estornarTransacao(TipoTransacao tipo, BigDecimal valor) {
        if (tipo == TipoTransacao.RECEITA) {
            this.saldoAtual = this.saldoAtual.subtract(valor);
        } else {
            this.saldoAtual = this.saldoAtual.add(valor);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return Objects.equals(id, conta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
