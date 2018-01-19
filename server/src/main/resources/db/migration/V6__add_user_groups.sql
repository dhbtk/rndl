CREATE TABLE user_group (
  id         bigserial PRIMARY KEY,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  updated_at timestamp with time zone NOT NULL DEFAULT now(),
  owner_id   bigint                   NOT NULL REFERENCES "user" (id),
  name       varchar                  NOT NULL
);

CREATE TABLE user_group_membership (
  id            bigserial PRIMARY KEY,
  created_at    timestamp with time zone NOT NULL DEFAULT now(),
  updated_at    timestamp with time zone NOT NULL DEFAULT now(),
  user_id       bigint                   NOT NULL REFERENCES "user" (id),
  user_group_id bigint                   NOT NULL REFERENCES user_group (id),
  administrator boolean                  NOT NULL,
  CONSTRAINT uk_user_group_membership_user_id_user_group_id UNIQUE (user_id, user_group_id)
);

INSERT INTO user_group (owner_id, name)
  SELECT
    id,
    'Default Group'
  FROM "user"
  ORDER BY id
  LIMIT 1;

INSERT INTO user_group_membership (user_id, user_group_id, administrator)
  SELECT
    "user".id,
    user_group.id,
    "user".id = owner_id
  FROM "user", user_group;

ALTER TABLE vehicle
  ADD COLUMN user_group_id bigint REFERENCES user_group (id);
UPDATE vehicle
SET user_group_id = (SELECT id
                     FROM user_group
                     LIMIT 1);
ALTER TABLE vehicle
  ALTER COLUMN user_group_id SET NOT NULL;