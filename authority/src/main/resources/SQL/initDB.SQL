-- Table: gateway_dev.server_resource

-- DROP TABLE IF EXISTS gateway_dev.server_resource;

CREATE TABLE IF NOT EXISTS gateway_dev.server_resource
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    path character varying COLLATE pg_catalog."default" NOT NULL,
    actual_path character varying COLLATE pg_catalog."default",
    need_authority boolean NOT NULL,
    comment character varying COLLATE pg_catalog."default" NOT NULL,
    type integer NOT NULL,
    status integer NOT NULL,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default" NOT NULL,
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time character varying COLLATE pg_catalog."default",
    CONSTRAINT server_resource_pkey PRIMARY KEY (id),
    CONSTRAINT server_resource_unique_path UNIQUE (path)
    )
    WITH (
        OIDS = FALSE
        )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway_dev.server_resource
    OWNER to gateway_dev_01;

COMMENT ON TABLE gateway_dev.server_resource
    IS '资源表';

COMMENT ON COLUMN gateway_dev.server_resource.id
    IS '主键id';

COMMENT ON COLUMN gateway_dev.server_resource.path
    IS '资源路径';

COMMENT ON COLUMN gateway_dev.server_resource.actual_path
    IS '资源实际路径，转储路径';

COMMENT ON COLUMN gateway_dev.server_resource.need_authority
    IS '是否需要权限';

COMMENT ON COLUMN gateway_dev.server_resource.comment
    IS '备注';

COMMENT ON COLUMN gateway_dev.server_resource.type
    IS '资源类型：0-项目；1-路径；2-文件';

COMMENT ON COLUMN gateway_dev.server_resource.status
    IS '资源状态：0-创建；1-锁定；2-失效';

COMMENT ON COLUMN gateway_dev.server_resource.create_use_id
    IS '创建人id';

COMMENT ON COLUMN gateway_dev.server_resource.create_use_name
    IS '创建人名称';

COMMENT ON COLUMN gateway_dev.server_resource.create_time
    IS '创建时间';

COMMENT ON COLUMN gateway_dev.server_resource.update_use_id
    IS '更新人id';

COMMENT ON COLUMN gateway_dev.server_resource.update_use_name
    IS '更新人名称';

COMMENT ON COLUMN gateway_dev.server_resource.update_time
    IS '更新时间';

-- Table: gateway_dev.server_resource_authority_relation

-- DROP TABLE IF EXISTS gateway_dev.server_resource_authority_relation;

CREATE TABLE IF NOT EXISTS gateway_dev.server_resource_authority_relation
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    resource_id character varying COLLATE pg_catalog."default" NOT NULL,
    authority_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default" NOT NULL,
    create_time timestamp with time zone NOT NULL,
    CONSTRAINT server_resource_authority_relation_pkey PRIMARY KEY (id)
    )
    WITH (
        OIDS = FALSE
        )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway_dev.server_resource_authority_relation
    OWNER to gateway_dev_01;

COMMENT ON TABLE gateway_dev.server_resource_authority_relation
    IS '资源权限关联表';

COMMENT ON COLUMN gateway_dev.server_resource_authority_relation.id
    IS '主键id';

COMMENT ON COLUMN gateway_dev.server_resource_authority_relation.resource_id
    IS '资源id';

COMMENT ON COLUMN gateway_dev.server_resource_authority_relation.authority_id
    IS '权限id';

COMMENT ON COLUMN gateway_dev.server_resource_authority_relation.create_use_id
    IS '创建人id';

COMMENT ON COLUMN gateway_dev.server_resource_authority_relation.create_use_name
    IS '创建人名称';

COMMENT ON COLUMN gateway_dev.server_resource_authority_relation.create_time
    IS '创建时间';