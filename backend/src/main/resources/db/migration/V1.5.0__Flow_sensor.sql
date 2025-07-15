CREATE TABLE flow_sensors
(
    id                    INTEGER not null,
    pps                   INTEGER not null,
    pin_board             INTEGER,
    pin_nr                INTEGER,
    primary key (id),
    FOREIGN KEY (pin_board, pin_nr) REFERENCES gpio_pins ON DELETE RESTRICT
);

CREATE TABLE pumps_new
(
    id                    INTEGER not null,
    dType                 TEXT    not null,
    name                  TEXT unique,
    completed             BOOLEAN not null,
    tube_capacity         REAL check (tube_capacity >= 0),
    current_ingredient_id INTEGER references ingredients on delete set null,
    filling_level_in_ml   INTEGER not null check (filling_level_in_ml >= 0),
    is_pumped_up          BOOLEAN not null default false,
    oo_pin_board          INTEGER,
    oo_pin_nr             INTEGER,
    time_per_cl_in_ms     INTEGER check (time_per_cl_in_ms >= 1),
    is_power_state_high   BOOLEAN,
    acceleration          INTEGER check (acceleration BETWEEN 1 and 500000),
    step_pin_board        INTEGER,
    step_pin_nr           INTEGER,
    enable_pin_board      INTEGER,
    enable_pin_nr         INTEGER,
    steps_per_cl          INTEGER check (steps_per_cl >= 1),
    max_steps_per_second  INTEGER check (max_steps_per_second BETWEEN 1 and 500000),
    flow_sensor           INTEGER,
    primary key (id),
    FOREIGN KEY (oo_pin_board, oo_pin_nr) REFERENCES gpio_pins ON DELETE RESTRICT,
    FOREIGN KEY (step_pin_board, step_pin_nr) REFERENCES gpio_pins ON DELETE RESTRICT,
    FOREIGN KEY (enable_pin_board, enable_pin_nr) REFERENCES gpio_pins ON DELETE RESTRICT,
    FOREIGN KEY (flow_sensor) REFERENCES flow_sensors ON DELETE SET NULL
);

INSERT INTO pumps_new (id, dType, name, completed, tube_capacity, current_ingredient_id, filling_level_in_ml,
                       is_pumped_up, oo_pin_board, oo_pin_nr, time_per_cl_in_ms, is_power_state_high, acceleration,
                       step_pin_board, step_pin_nr, enable_pin_board, enable_pin_nr, steps_per_cl,
                       max_steps_per_second)
SELECT id, dType, name, completed, tube_capacity, current_ingredient_id, filling_level_in_ml,
       is_pumped_up, oo_pin_board, oo_pin_nr, time_per_cl_in_ms, is_power_state_high, acceleration,
       step_pin_board, step_pin_nr, enable_pin_board, enable_pin_nr, steps_per_cl,
       max_steps_per_second
FROM pumps;

DROP TABLE pumps;
ALTER TABLE pumps_new RENAME TO pumps;

CREATE VIEW pumps_with_dependencies AS
SELECT pumps.*, flow_sensors.pin_nr AS flow_sensor_pin_nr, flow_sensors.pps AS flow_sensor_pps, flow_sensors.pin_board AS flow_sensor_board
FROM pumps
LEFT JOIN flow_sensors ON pumps.flow_sensor = flow_sensors.id
