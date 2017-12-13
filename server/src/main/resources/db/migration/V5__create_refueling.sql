CREATE TABLE fuel_type (
  id         bigserial PRIMARY KEY,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  updated_at timestamp with time zone NOT NULL DEFAULT now(),
  name       varchar                  NOT NULL
);

CREATE TABLE refueling (
  id              bigserial PRIMARY KEY,
  created_at      timestamp with time zone NOT NULL DEFAULT now(),
  updated_at      timestamp with time zone NOT NULL DEFAULT now(),
  fuel_id         bigint                   NOT NULL REFERENCES fuel_type (id),
  vehicle_id      bigint                   NOT NULL REFERENCES vehicle (id),
  date            timestamp with time zone NOT NULL,
  price_per_liter numeric(6, 3)            NOT NULL,
  liters          numeric(6, 3)            NOT NULL,
  odometer        numeric(7, 1)            NOT NULL
);
