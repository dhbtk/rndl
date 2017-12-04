TRUNCATE "user" CASCADE;

INSERT INTO "user" (email, full_name, password_digest)
VALUES ('eduardo@edanni.io', 'Eduardo Niehues', '{bcrypt}$2a$11$uotSbccL0gy./zZOqP8DOuL6OgUYjWfSF6eq1EB/WN0aQJeS5V97e');
