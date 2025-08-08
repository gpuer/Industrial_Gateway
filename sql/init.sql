/*
 Navicat Premium Dump SQL

 Source Server         : iot
 Source Server Type    : PostgreSQL
 Source Server Version : 120004 (120004)
 Source Host           : localhost:5432
 Source Catalog        : iot-test
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 120004 (120004)
 File Encoding         : 65001

 Date: 07/08/2025 17:24:43
*/


-- ----------------------------
-- Sequence structure for iot_device_template_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."iot_device_template_id_seq";
CREATE SEQUENCE "public"."iot_device_template_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1;
ALTER SEQUENCE "public"."iot_device_template_id_seq" OWNER TO "postgres";

-- ----------------------------
-- Table structure for iot_device
-- ----------------------------
DROP TABLE IF EXISTS "public"."iot_device";
CREATE TABLE "public"."iot_device" (
  "id" int4 NOT NULL,
  "device_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "sn" varchar(50) COLLATE "pg_catalog"."default",
  "offset" int4,
  "len" int4,
  "template_name" varchar(50) COLLATE "pg_catalog"."default",
  "gateway_name" varchar(20) COLLATE "pg_catalog"."default",
  "db" int4
)
;
ALTER TABLE "public"."iot_device" OWNER TO "postgres";

-- ----------------------------
-- Table structure for iot_device_template
-- ----------------------------
DROP TABLE IF EXISTS "public"."iot_device_template";
CREATE TABLE "public"."iot_device_template" (
  "id" int4 NOT NULL GENERATED ALWAYS AS IDENTITY (
INCREMENT 1
MINVALUE  1
MAXVALUE 2147483647
START 1
CACHE 1
),
  "field_name" varchar(30) COLLATE "pg_catalog"."default",
  "type" varchar(30) COLLATE "pg_catalog"."default",
  "length" int4,
  "scale" float4,
  "word_swap" int2,
  "byte_swap" int2,
  "offset" int4,
  "template_name" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."iot_device_template" OWNER TO "postgres";
COMMENT ON COLUMN "public"."iot_device_template"."length" IS '寄存器数量';
COMMENT ON COLUMN "public"."iot_device_template"."scale" IS '调整系数';
COMMENT ON COLUMN "public"."iot_device_template"."offset" IS '偏移';

-- ----------------------------
-- Table structure for iot_gateway
-- ----------------------------
DROP TABLE IF EXISTS "public"."iot_gateway";
CREATE TABLE "public"."iot_gateway" (
  "id" int4 NOT NULL,
  "name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "host" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "port" int4,
  "rack" int4,
  "slot" int4,
  "slave_id" int4,
  "type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL
)
;
ALTER TABLE "public"."iot_gateway" OWNER TO "postgres";

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."iot_device_template_id_seq"
OWNED BY "public"."iot_device_template"."id";
SELECT setval('"public"."iot_device_template_id_seq"', 115, true);

-- ----------------------------
-- Primary Key structure for table iot_device
-- ----------------------------
ALTER TABLE "public"."iot_device" ADD CONSTRAINT "iot_device_copy1_pkey1" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iot_device_template
-- ----------------------------
ALTER TABLE "public"."iot_device_template" ADD CONSTRAINT "iot_device_template_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table iot_gateway
-- ----------------------------
ALTER TABLE "public"."iot_gateway" ADD CONSTRAINT "iot_gateway_copy1_name_key1" UNIQUE ("name");

-- ----------------------------
-- Primary Key structure for table iot_gateway
-- ----------------------------
ALTER TABLE "public"."iot_gateway" ADD CONSTRAINT "iot_gateway_copy1_pkey1" PRIMARY KEY ("id");
