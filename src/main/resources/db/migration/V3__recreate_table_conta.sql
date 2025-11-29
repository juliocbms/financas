DROP TABLE IF EXISTS tb_conta;
DROP TYPE IF EXISTS tipo_conta;


CREATE TABLE tb_conta (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(120) NOT NULL,
    tipo_conta VARCHAR(100) NOT NULL,
    saldo_inicial NUMERIC(12,2) DEFAULT 0,
    saldo_atual NUMERIC(12,2) DEFAULT 0,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_conta_usuario
        FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)
        ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.atualizado_em = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_tb_conta_updated_at
    BEFORE UPDATE ON tb_conta
    FOR EACH ROW
    EXECUTE PROCEDURE update_updated_at_column();