--
-- PostgreSQL database dump
--

SET statement_timeout = 0;

--SET client_encoding = 'SQL_ASCII';

SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: report; Type: SCHEMA; Schema: -; Owner: postgres
--

DROP SCHEMA IF EXISTS excel_builder cascade;

CREATE SCHEMA excel_builder;

ALTER SCHEMA excel_builder OWNER TO postgres;

SET search_path = excel_builder, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

COMMIT;

CREATE TABLE IF NOT EXISTS excel_builder.test_report
(
  id text,
  name character varying(20),
  start_date_time timestamp without time zone
)
WITH (
  OIDS=FALSE
);
