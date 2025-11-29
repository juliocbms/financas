package com.financas.julio.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @Column(name = "criado_em")
    private LocalDateTime createAt;

    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;
}
