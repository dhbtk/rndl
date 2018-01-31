ALTER TABLE refueling
  DROP COLUMN fuel_id;

DROP TABLE fuel_type;

ALTER TABLE refueling
  ADD COLUMN fuel_type varchar NOT NULL DEFAULT 'GASOLINE';