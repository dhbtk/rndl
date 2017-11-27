CREATE TABLE "user" (
  id              bigserial PRIMARY KEY,
  created_at      timestamp with time zone NOT NULL DEFAULT now(),
  updated_at      timestamp with time zone NOT NULL DEFAULT now(),
  email           varchar                  NOT NULL,
  full_name       varchar                  NOT NULL,
  password_digest varchar                  NOT NULL
);

CREATE UNIQUE INDEX uk_user_email
  ON "user" (lower(email));

CREATE TABLE user_token (
  id         bigserial PRIMARY KEY,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  updated_at timestamp with time zone NOT NULL DEFAULT now(),
  expires_at timestamp with time zone NOT NULL,
  user_id    bigint                   NOT NULL REFERENCES "user" (id),
  token      varchar                  NOT NULL
);
