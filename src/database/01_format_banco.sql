BEGIN;

-- 1. Remover tabelas
DROP TABLE IF EXISTS etapa, tarefa, categoria, usuario CASCADE;


-- Cria Tabelas
CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,

                         nome VARCHAR(100) NOT NULL UNIQUE,
                         gmail VARCHAR(255) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,

                         xp INTEGER NOT NULL DEFAULT 0
                             CHECK (xp >= 0),

                         nivel INTEGER NOT NULL DEFAULT 1
                             CHECK (nivel >= 1),

                         habitos_criados INTEGER NOT NULL DEFAULT 0
                             CHECK (habitos_criados >= 0)
);

CREATE TABLE categoria (
                           id SERIAL PRIMARY KEY,

                           nome VARCHAR(100) NOT NULL,
                           cor VARCHAR(30),
                           icone VARCHAR(100) NOT NULL DEFAULT 'category',

                           usuario_id INTEGER,

                           CONSTRAINT fk_categoria_usuario
                               FOREIGN KEY (usuario_id)
                                   REFERENCES usuario(id)
                                   ON DELETE CASCADE
);

CREATE TABLE tarefa (
                        id SERIAL PRIMARY KEY,

                        usuario_id INTEGER NOT NULL,
                        categoria_id INTEGER,

                        nome VARCHAR(150) NOT NULL,
                        descricao TEXT NOT NULL DEFAULT '',
                        horario VARCHAR(5) NOT NULL,

                        duracao DOUBLE PRECISION NOT NULL DEFAULT 0
                            CHECK (duracao >= 0),

                        prioridade DOUBLE PRECISION NOT NULL DEFAULT 1
                            CHECK (prioridade BETWEEN 1 AND 5),

                        porcentagem DOUBLE PRECISION NOT NULL DEFAULT 0
                            CHECK (porcentagem BETWEEN 0 AND 100),

                        concluida BOOLEAN NOT NULL DEFAULT FALSE,
                        repeticao BOOLEAN NOT NULL DEFAULT FALSE,

                        dias_semana TEXT NOT NULL DEFAULT '',

                        sequencia INTEGER NOT NULL DEFAULT 0
                            CHECK (sequencia >= 0),

                        data_conclusao DATE,

                        bonus_habito_recebido BOOLEAN NOT NULL DEFAULT FALSE,
                        xp_recebido BOOLEAN NOT NULL DEFAULT FALSE,

                        CONSTRAINT fk_tarefa_usuario
                            FOREIGN KEY (usuario_id)
                                REFERENCES usuario(id)
                                ON DELETE CASCADE,

                        CONSTRAINT fk_tarefa_categoria
                            FOREIGN KEY (categoria_id)
                                REFERENCES categoria(id)
                                ON DELETE SET NULL
);

CREATE TABLE etapa (
                       id SERIAL PRIMARY KEY,

                       nome VARCHAR(150) NOT NULL,

                       peso DOUBLE PRECISION NOT NULL DEFAULT 0
                           CHECK (peso >= 0),

                       concluida BOOLEAN NOT NULL DEFAULT FALSE,

                       tarefa_id INTEGER NOT NULL,

                       CONSTRAINT fk_etapa_tarefa
                           FOREIGN KEY (tarefa_id)
                               REFERENCES tarefa(id)
                               ON DELETE CASCADE
);


-- 6. Indices para consultas frequentes
CREATE INDEX idx_tarefa_usuario
    ON tarefa(usuario_id);

CREATE INDEX idx_tarefa_categoria
    ON tarefa(categoria_id);

CREATE INDEX idx_etapa_tarefa
    ON etapa(tarefa_id);

CREATE INDEX idx_categoria_usuario
    ON categoria(usuario_id);


COMMIT;
