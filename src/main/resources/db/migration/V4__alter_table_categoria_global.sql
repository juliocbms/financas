
ALTER TABLE tb_categoria ALTER COLUMN usuario_id DROP NOT NULL;


INSERT INTO tb_categoria (nome, tipo, usuario_id) VALUES
('Alimentação', 'DESPESA', NULL),
('Transporte', 'DESPESA', NULL),
('Moradia', 'DESPESA', NULL),
('Lazer', 'DESPESA', NULL),
('Saúde', 'DESPESA', NULL),
('Educação', 'DESPESA', NULL),
('Salário', 'RECEITA', NULL),
('Investimentos', 'RECEITA', NULL);