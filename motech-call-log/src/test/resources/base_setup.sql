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

DROP SCHEMA IF EXISTS call_log cascade;

CREATE SCHEMA call_log;

ALTER SCHEMA call_log OWNER TO postgres;

SET search_path = call_log, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;