TRUNCATE "user" CASCADE;
TRUNCATE vehicle CASCADE;

INSERT INTO vehicle (id, name, torque_id) VALUES (1, 'Renault Clio', '18df7ad0aa6ef980c49851ec88fa0cf9');

SELECT setval(sequence_name, 1)
FROM information_schema.sequences
WHERE sequence_schema = 'public';
