BEGIN;

-- Remover tabelas se ja existirem
DROP TABLE IF EXISTS etapa, tarefa, categoria, usuario CASCADE;


-- Criar Tabelas
CREATE TABLE usuario (
                        --  Variaveis e especificacoes
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
                            -- Variaveis e especificacoes
                           id SERIAL PRIMARY KEY,

                           nome VARCHAR(100) NOT NULL,
                           cor VARCHAR(30),
                           icone VARCHAR(100) NOT NULL DEFAULT 'category',

                           usuario_id INTEGER,

                            -- Define regra no banco de dados
                           CONSTRAINT fk_categoria_usuario

                               -- Chave estrangeira e pra onde aponta
                               FOREIGN KEY (usuario_id)
                                   REFERENCES usuario(id)

                                   -- Se usuário for excluído, suas categorias tbm serão automaticamente.
                                   ON DELETE CASCADE
);

CREATE TABLE tarefa (
                        -- Variaveis e especificacoes
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

                        -- Define regra no banco de dados
                        CONSTRAINT fk_tarefa_usuario

                            -- Chave estrangeira e pra onde aponta
                            FOREIGN KEY (usuario_id)
                                REFERENCES usuario(id)

                                -- Se usuário for excluído, suas tarefas tbm serão automaticamente.
                                ON DELETE CASCADE,

                        -- Define regra no banco de dados
                        CONSTRAINT fk_tarefa_categoria

                            -- Chave estrangeira e pra onde aponta
                            FOREIGN KEY (categoria_id)
                                REFERENCES categoria(id)

                                -- categoria_id da tarefa recebera NULL quando a categoria for excluída
                                ON DELETE SET NULL
);

CREATE TABLE recorrencia (
    -- Variaveis e especificacoes
    id SERIAL PRIMARY KEY,
    tarefa_id INTEGER NOT NULL UNIQUE,
    tipo_recorrencia VARCHAR(20) NOT NULL DEFAULT 'NUNCA'
        -- Restrição verificar se valor atente as condições
        CHECK ( tipo_recorrencia IN (
            'NUNCA',
            'DIARIA',
            'SEMANAL',
            'MENSAL',
            'ANUAL'
        )),

    intervalo INTEGER NOT NULL DEFAULT 1
        -- Restrição verificar se valor atente as condições
        CHECK ( intervalo >= 1 ),
    data_inicio DATE NOT NULL,
    dias_semana TEXT NOT NULL DEFAULT '',
    regra_calendario VARCHAR(20)
        -- Restrição verificar se valor atente as condições
        CHECK ( regra_calendario IN (
            'DIA_DO_MES',
            'DIA_DA_SEMANA'
            )),

    dia_do_mes INTEGER
        -- Restrição verificar se valor atente as condições,se esta dentro desse intervalo (Entre 1 e 31)
        CHECK ( dia_do_mes BETWEEN 1 AND 31),

    ultimo_dia_mes BOOLEAN NOT NULL DEFAULT FALSE,

    mes_do_ano INTEGER
        -- Restrição verificar se valor atente as condições,se esta dentro desse intervalo (Entre 1 e 12)
        CHECK ( mes_do_ano BETWEEN 1 AND 12),

    posicao_mes VARCHAR(20)
        CHECK ( posicao_mes IN (
            'PRIMEIRA',
            'SEGUNDA',
            'TERCEIRA',
            'QUERTA',
            'ULTIMA'
            )),

    dia_semana_mes VARCHAR(20)
        CHECK ( dia_semana_mes IN (
            'SUNDAY'
            'MONDAY',
            'TUESDAY',
            'WEDNESDAY',
            'THURSDAY',
            'FRIDAY',
            'SATURDAY'
            )),

    -- Define regra dos dados
    CONSTRAINT fk_recorrencia_tarefa
        -- Chave estrangeira e pra onde aponta
        FOREIGN KEY (tarefa_id)
        REFERENCES tarefa(id)
        -- Se tarefa for excluído, suas recorrencias tbm serão automaticamente.
        ON DELETE CASCADE
);

CREATE TABLE etapa (
                        -- Variaveis e especificacoes
                       id SERIAL PRIMARY KEY,

                       nome VARCHAR(150) NOT NULL,

                       peso DOUBLE PRECISION NOT NULL DEFAULT 0
                           CHECK (peso >= 0),

                       concluida BOOLEAN NOT NULL DEFAULT FALSE,

                       tarefa_id INTEGER NOT NULL,

                        -- Define regra dos dados
                       CONSTRAINT fk_etapa_tarefa

                           -- Chave estrangeira e pra onde aponta
                           FOREIGN KEY (tarefa_id)
                               REFERENCES tarefa(id)

                               -- Se tarefa for excluído, suas etapas tbm serão automaticamente.
                               ON DELETE CASCADE
);


-- Indices para consultas frequentes

-- Cria indice (Estrutura auxiliar encontrar registros mais rapidamente)
CREATE INDEX idx_tarefa_usuario
    -- Indica onde o índice será criado
    ON tarefa(usuario_id);

-- Cria indice (Estrutura auxiliar encontrar registros mais rapidamente)
CREATE INDEX idx_tarefa_categoria
    -- Indica onde o índice será criado
    ON tarefa(categoria_id);

-- Cria indice (Estrutura auxiliar encontrar registros mais rapidamente)
CREATE INDEX idx_etapa_tarefa
    -- Indica onde o índice será criado
    ON etapa(tarefa_id);

-- Cria indice (Estrutura auxiliar encontrar registros mais rapidamente)
CREATE INDEX idx_categoria_usuario
    -- Indica onde o índice será criado
    ON categoria(usuario_id);

-- Cria indice (Estrutura auxiliar encontrar registros mais rapidamente)
CREATE INDEX idx_recorrencia_tarefa
    -- Indica onde o índice será criado
    ON recorrencia(tarefa_id);

-- Confirma as operações realizadas
COMMIT;
