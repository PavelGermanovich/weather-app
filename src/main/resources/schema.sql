CREATE TABLE location (
                          id BIGINT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE weather (
                         id BIGINT PRIMARY KEY,
                         temperature DOUBLE NOT NULL,
                         windSpeedMtrHr DOUBLE NOT NULL,
                         pressureMb DOUBLE NOT NULL,
                         humidity INT NOT NULL,
                         weatherCondition VARCHAR(255) NOT NULL,
                         location_id BIGINT NOT NULL,
                         weatherDate DATE NOT NULL,
                         FOREIGN KEY (location_id) REFERENCES location(id)
);