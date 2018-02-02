ALTER TABLE vehicle
  DROP COLUMN final_drive,
  DROP COLUMN tire_diameter,
  DROP COLUMN gear_ratios,
  ADD COLUMN gear_reference_speeds numeric [];
