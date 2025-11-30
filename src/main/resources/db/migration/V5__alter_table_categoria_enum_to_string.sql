ALTER TABLE tb_categoria
ALTER COLUMN tipo TYPE VARCHAR(50)
USING tipo::text;

DROP TYPE IF EXISTS tipo_categoria;