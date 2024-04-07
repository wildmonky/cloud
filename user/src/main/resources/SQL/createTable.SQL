-- PostgreSQL

-- Table: gateway.user

-- DROP TABLE IF EXISTS gateway."user";

CREATE TABLE IF NOT EXISTS gateway."user"
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    phone character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL DEFAULT 88888888,
    status smallint NOT NULL DEFAULT 0,
    create_use_id character varying COLLATE pg_catalog."default",
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT user_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway."user"
    OWNER to gateway;

COMMENT ON TABLE gateway."user"
    IS '用户表';

COMMENT ON COLUMN gateway."user".phone
    IS '电话号码';

COMMENT ON COLUMN gateway."user".name
    IS '用户名';

COMMENT ON COLUMN gateway."user".status
    IS '用户状态: 0-初始; 1-可用; 2-禁用';


-- Table: gateway.group

-- DROP TABLE IF EXISTS gateway."group";

CREATE TABLE IF NOT EXISTS gateway."group"
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    parent_id character varying COLLATE pg_catalog."default",
    name character varying COLLATE pg_catalog."default" NOT NULL,
    status smallint NOT NULL DEFAULT 0,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT group_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway."group"
    OWNER to gateway;

COMMENT ON TABLE gateway."group"
    IS '组';

COMMENT ON COLUMN gateway."group".status
    IS '组状态：0-初始；1-可用; 2-停用';



-- Table: gateway.role

-- DROP TABLE IF EXISTS gateway.role;

CREATE TABLE IF NOT EXISTS gateway.role
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    parent_id character varying COLLATE pg_catalog."default",
    name character varying COLLATE pg_catalog."default" NOT NULL,
    status smallint NOT NULL DEFAULT 0,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT role_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway.role
    OWNER to gateway;

COMMENT ON TABLE gateway.role
    IS '角色表';

COMMENT ON COLUMN gateway.role.status
    IS '角色状态：0-初始；1-可用; 2-停用';


-- Table: gateway.authority

-- DROP TABLE IF EXISTS gateway.authority;

CREATE TABLE IF NOT EXISTS gateway.authority
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    comment character varying COLLATE pg_catalog."default" NOT NULL,
    status smallint NOT NULL DEFAULT 0,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT authority_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway.authority
    OWNER to gateway;

COMMENT ON TABLE gateway.authority
    IS '权限表';

COMMENT ON COLUMN gateway.authority.status
    IS '权限状态：0-初始；1-可用; 2-停用';


-- Table: gateway.group_authority_relation

-- DROP TABLE IF EXISTS gateway.group_authority_relation;

CREATE TABLE IF NOT EXISTS gateway.group_authority_relation
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    group_id character varying COLLATE pg_catalog."default" NOT NULL,
    authority_id character varying COLLATE pg_catalog."default" NOT NULL,
    valid boolean NOT NULL DEFAULT false,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT group_authority_relation_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway.group_authority_relation
    OWNER to gateway;

COMMENT ON TABLE gateway.group_authority_relation
    IS '组和权限关系表';


-- Table: gateway.group_user_relation

-- DROP TABLE IF EXISTS gateway.group_user_relation;

CREATE TABLE IF NOT EXISTS gateway.group_user_relation
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    group_id character varying COLLATE pg_catalog."default" NOT NULL,
    user_id character varying COLLATE pg_catalog."default" NOT NULL,
    valid boolean NOT NULL DEFAULT false,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT group_user_relation_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway.group_user_relation
    OWNER to gateway;

COMMENT ON TABLE gateway.group_user_relation
    IS '组和用户关系表';


-- Table: gateway.role_authority_relation

-- DROP TABLE IF EXISTS gateway.role_authority_relation;

CREATE TABLE IF NOT EXISTS gateway.role_authority_relation
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    role_id character varying COLLATE pg_catalog."default" NOT NULL,
    authority_id character varying COLLATE pg_catalog."default" NOT NULL,
    valid boolean NOT NULL DEFAULT false,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT role_authority_relation_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway.role_authority_relation
    OWNER to gateway;

COMMENT ON TABLE gateway.role_authority_relation
    IS '角色和权限关系表';


-- Table: gateway.user_authority_relation

-- DROP TABLE IF EXISTS gateway.user_authority_relation;

CREATE TABLE IF NOT EXISTS gateway.user_authority_relation
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    user_id character varying COLLATE pg_catalog."default" NOT NULL,
    authority_id character varying COLLATE pg_catalog."default" NOT NULL,
    valid boolean NOT NULL DEFAULT false,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT user_authority_relation_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway.user_authority_relation
    OWNER to gateway;

COMMENT ON TABLE gateway.user_authority_relation
    IS '用户和权限关系表';



-- Table: gateway.user_role_relation

-- DROP TABLE IF EXISTS gateway.user_role_relation;

CREATE TABLE IF NOT EXISTS gateway.user_role_relation
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    user_id character varying COLLATE pg_catalog."default" NOT NULL,
    role_id character varying COLLATE pg_catalog."default" NOT NULL,
    valid boolean NOT NULL DEFAULT false,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT user_role_relation_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway.user_role_relation
    OWNER to gateway;

COMMENT ON TABLE gateway.user_role_relation
    IS '用户和角色关系表';

-- Table: gateway_dev.group_role_relation

-- DROP TABLE IF EXISTS gateway_dev.group_role_relation;

CREATE TABLE IF NOT EXISTS gateway_dev.group_role_relation
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    group_id character varying COLLATE pg_catalog."default" NOT NULL,
    role_id character varying COLLATE pg_catalog."default" NOT NULL,
    valid boolean NOT NULL DEFAULT false,
    create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    create_use_name character varying COLLATE pg_catalog."default",
    create_time timestamp with time zone NOT NULL,
    update_use_id character varying COLLATE pg_catalog."default",
    update_use_name character varying COLLATE pg_catalog."default",
    update_time timestamp with time zone,
    CONSTRAINT group_role_relation_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway_dev.group_role_relation
    OWNER to gateway_dev_01;

COMMENT ON TABLE gateway_dev.group_role_relation
    IS '组和角色关系表';