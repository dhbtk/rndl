CREATE EXTENSION postgis;

CREATE TABLE entry (
  id              bigserial PRIMARY KEY,
  created_at      timestamp with time zone    NOT NULL DEFAULT now(),
  updated_at      timestamp with time zone    NOT NULL DEFAULT now(),
  trip_id         bigint                      NOT NULL REFERENCES trip (id),
  device_time     timestamp without time zone NOT NULL,
  coordinates     geography(point, 4326),
  gps_speed       numeric(5, 2),
  gps_altitude    numeric(5, 1),
  rpm             numeric(7, 2),
  economy         numeric(4, 2),
  speed           integer,
  throttle        numeric(5, 2),
  instant_economy numeric(4, 2),
  fuel_flow       numeric(7, 1),
  fuel_used       numeric(5, 1)
);
