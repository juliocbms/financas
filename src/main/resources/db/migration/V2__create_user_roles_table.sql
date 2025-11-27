CREATE TYPE tipo_transacao AS ENUM ('RECEITA', 'DESPESA');
CREATE TYPE tipo_categoria AS ENUM ('RECEITA', 'DESPESA');

CREATE TABLE tb_usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_usuario_email ON tb_usuario(email);

CREATE TABLE tb_conta (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    tipo_conta VARCHAR(50) NOT NULL,
    saldo_inicial NUMERIC(12,2) DEFAULT 0,
    criado_em TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_conta_usuario
        FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_conta_usuario ON tb_conta(usuario_id);

CREATE TABLE tb_categoria (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    tipo tipo_categoria NOT NULL,
    CONSTRAINT fk_categoria_usuario
        FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_categoria_usuario ON tb_categoria(usuario_id);

CREATE TABLE tb_transacao (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    categoria_id BIGINT,
    conta_id BIGINT,
    tipo tipo_transacao NOT NULL,
    valor NUMERIC(12,2) NOT NULL,
    data DATE NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_transacao_usuario
        FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_transacao_categoria
        FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_transacao_conta
        FOREIGN KEY (conta_id) REFERENCES tb_conta(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_transacao_usuario ON tb_transacao(usuario_id);
CREATE INDEX idx_transacao_data ON tb_transacao(data);
CREATE INDEX idx_transacao_categoria ON tb_transacao(categoria_id);
CREATE INDEX idx_transacao_conta ON tb_transacao(conta_id);

CREATE TABLE tb_meta_financeira (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    valor_meta NUMERIC(12,2) NOT NULL,
    valor_atual NUMERIC(12,2) DEFAULT 0,
    data_limite DATE,
    criado_em TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_meta_usuario
        FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_meta_usuario ON tb_meta_financeira(usuario_id);

CREATE TABLE tb_reserva (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    valor_reservado NUMERIC(12,2) DEFAULT 0,
    criado_em TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_reserva_usuario
        FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_reserva_usuario ON tb_reserva(usuario_id);