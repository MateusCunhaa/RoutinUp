BEGIN;

CREATE UNIQUE INDEX IF NOT EXISTS
    idx_categoria_padrao_nome_unico
    ON categoria (LOWER(TRIM(nome)))
    WHERE usuario_id IS NULL;

INSERT INTO categoria (nome, cor, icone, usuario_id)
VALUES
    ('Esportes',    '#3478F6', 'sports',                 NULL),
    ('Social',      '#D946A8', 'groups',                 NULL),
    ('Saúde',       '#2EAD68', 'favorite',               NULL),
    ('Alimentação', '#F59E0B', 'restaurant',             NULL),
    ('Arte',        '#9B59B6', 'palette',                NULL),
    ('Estudos',     '#2980B9', 'school',                 NULL),
    ('Lazer',       '#E85D75', 'sports_esports',          NULL),
    ('Finanças',    '#16A6A1', 'account_balance_wallet', NULL),
    ('Trabalho',    '#536DFE', 'work',                   NULL),
    ('Casa',        '#B77945', 'home',                   NULL),
    ('Outros',      '#737B87', 'category',               NULL)
ON CONFLICT DO NOTHING;

COMMIT;
