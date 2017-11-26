CREATE TABLE vehicle (
  id            bigserial PRIMARY KEY,
  created_at    timestamp with time zone NOT NULL DEFAULT now(),
  updated_at    timestamp with time zone NOT NULL DEFAULT now(),
  name          varchar                  NOT NULL,
  torque_id     varchar                  NOT NULL,
  license_plate varchar,
  gear_ratios   numeric(4, 2) []         NOT NULL DEFAULT ARRAY [] :: numeric [],
  final_drive   numeric(4, 2)            NOT NULL DEFAULT 1,
  tire_diameter numeric(8, 4)            NOT NULL DEFAULT 1
);
