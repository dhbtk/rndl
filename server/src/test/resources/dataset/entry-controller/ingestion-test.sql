TRUNCATE "user" CASCADE;
TRUNCATE vehicle CASCADE;

INSERT INTO "user" (id, email, full_name, password_digest) VALUES (1, 'eduardo@edanni.io', 'aaa', '');
INSERT INTO user_group (id, owner_id, name) VALUES (1, 1, '');
INSERT INTO vehicle (id, name, torque_id, user_group_id) VALUES (1, 'Renault Clio', '18df7ad0aa6ef980c49851ec88fa0cf9', 1);

SELECT setval(sequence_name, 1)
FROM information_schema.sequences
WHERE sequence_schema = 'public';
