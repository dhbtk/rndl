CREATE TABLE trip (
  id              bigserial PRIMARY KEY,
  created_at      timestamp with time zone NOT NULL DEFAULT now(),
  updated_at      timestamp with time zone NOT NULL DEFAULT now(),
  start_timestamp bigint                   NOT NULL,
  duration        time without time zone,
  distance        double precision,
  average_speed   integer,
  maximum_speed   integer,
  economy         double precision,
  fuel_used       numeric(5, 1),
  vehicle_id      bigint                   NOT NULL REFERENCES vehicle (id)
);
