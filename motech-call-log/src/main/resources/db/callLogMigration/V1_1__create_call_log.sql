CREATE EXTENSION IF NOT EXISTS hstore;

CREATE TABLE call_log
(
  call_log_id serial NOT NULL,
  call_id text,
  phone_number character varying(20),
  start_date_time timestamp without time zone,
  end_date_time timestamp without time zone,
  disposition character varying(50),
  error_message character varying(50),
  call_type character varying(32),
  request_id character varying(32),
  attempt_date_time timestamp without time zone,
  attempt_number character varying(30),
  call_events hstore,
  custom_data hstore
)
WITH (
  OIDS=FALSE
);
