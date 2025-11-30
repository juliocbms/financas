DROP TABLE IF EXISTS tb_transacao CASCADE;


CREATE TABLE tb_transacao (
    id BIGSERIAL PRIMARY KEY,

    usuario_id BIGINT NOT NULL,
    categoria_id BIGINT,
    conta_id BIGINT,

    tipo VARCHAR(50) NOT NULL,

    valor NUMERIC(12,2) NOT NULL,
    data DATE NOT NULL,
    titulo TEXT,
    descricao TEXT,

    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW(),

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

CREATE TRIGGER update_tb_transacao_updated_at
    BEFORE UPDATE ON tb_transacao
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at_column();