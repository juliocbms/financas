CREATE TABLE tb_user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
        REFERENCES tb_usuario(id)
        ON DELETE CASCADE
);