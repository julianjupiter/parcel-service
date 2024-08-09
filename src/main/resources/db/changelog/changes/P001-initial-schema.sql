--liquibase formatted sql
--changeset julianjupiter:P001 splitStatements:true endDelimiter:; context:local,dev,test,prod
CREATE TABLE IF NOT EXISTS parcel_type (
	id INT NOT NULL AUTO_INCREMENT,
	code VARCHAR(255) NOT NULL,
	name VARCHAR(255) NOT NULL,
	basic_cost DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
	created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    CONSTRAINT uq_parcel_type_code UNIQUE (code),
    CONSTRAINT uq_parcel_type_name UNIQUE (name)
);
INSERT INTO parcel_type (id, code, name, basic_cost) VALUE(1, 'REJECT', 'Reject', 0.00);
INSERT INTO parcel_type (id, code, name, basic_cost) VALUE(2, 'HEAVY_PARCEL', 'Heavy Parcel', 20.00);
INSERT INTO parcel_type (id, code, name, basic_cost) VALUE(3, 'SMALL_PARCEL', 'Small Parcel', 0.03);
INSERT INTO parcel_type (id, code, name, basic_cost) VALUE(4, 'MEDIUM_PARCEL', 'Medium Parcel', 0.04);
INSERT INTO parcel_type (id, code, name, basic_cost) VALUE(5, 'LARGE_PARCEL', 'Large Parcel', 0.05);