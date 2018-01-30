CREATE INDEX idx_entry_trip_id
  ON entry (trip_id);

CREATE INDEX idx_entry_coordinates_not_null
  ON entry (coordinates)
  WHERE coordinates IS NOT NULL;

CREATE INDEX idx_entry_device_time_desc
  ON entry (device_time DESC);