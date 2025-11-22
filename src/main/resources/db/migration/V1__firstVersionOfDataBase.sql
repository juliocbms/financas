
CREATE TYPE tipo_transacao AS ENUM ('RECEITA', 'DESPESA');


CREATE TYPE tipo_categoria AS ENUM ('RECEITA', 'DESPESA');

CREATE TABLE tb_usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_usuario_email ON usuario(email);

CREATE TABLE tb_conta (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    tipo_conta VARCHAR(50) NOT NULL,
    saldo_inicial NUMERIC(12,2) DEFAULT 0,
    criado_em TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_conta_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_conta_usuario ON conta(usuario_id);


CREATE TABLE tb_categoria (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    tipo tipo_categoria NOT NULL,

    CONSTRAINT fk_categoria_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_categoria_usuario ON categoria(usuario_id);


CREATE TABLE tb_transacao (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    conta_id BIGINT NOT NULL,
    tipo tipo_transacao NOT NULL,
    valor NUMERIC(12,2) NOT NULL,
    data DATE NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_transacao_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_transacao_categoria
        FOREIGN KEY (categoria_id) REFERENCES categoria(id)
        ON DELETE SET NULL,

    CONSTRAINT fk_transacao_conta
        FOREIGN KEY (conta_id) REFERENCES conta(id)
        ON DELETE SET NULL
);

CREATE INDEX idx_transacao_usuario ON transacao(usuario_id);
CREATE INDEX idx_transacao_data ON transacao(data);
CREATE INDEX idx_transacao_categoria ON transacao(categoria_id);
CREATE INDEX idx_transacao_conta ON transacao(conta_id);


CREATE TABLE tb_meta_financeira (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    valor_meta NUMERIC(12,2) NOT NULL,
    valor_atual NUMERIC(12,2) DEFAULT 0,
    data_limite DATE,
    criado_em TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_meta_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_meta_usuario ON meta_financeira(usuario_id);


CREATE TABLE tb_reserva (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    valor_reservado NUMERIC(12,2) DEFAULT 0,
    criado_em TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_reserva_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_reserva_usuario ON reserva(usuario_id);
