-- Table: gateway_dev.room

-- DROP TABLE IF EXISTS gateway_dev.room;

CREATE TABLE IF NOT EXISTS gateway_dev.room
(
    -- Inherited from table gateway_dev.create_columns: create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table gateway_dev.create_columns: create_use_name character varying COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table gateway_dev.create_columns: create_time timestamp with time zone NOT NULL,
    -- Inherited from table gateway_dev.update_columns: update_use_id character varying COLLATE pg_catalog."default",
    -- Inherited from table gateway_dev.update_columns: update_use_name character varying COLLATE pg_catalog."default",
    -- Inherited from table gateway_dev.update_columns: update_time timestamp with time zone,
    id character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    capacity integer NOT NULL,
    used integer NOT NULL,
    secret character varying COLLATE pg_catalog."default",
    CONSTRAINT room_pkey PRIMARY KEY (id),
    CONSTRAINT room_name_uni UNIQUE (name)
    )
    INHERITS (gateway_dev.create_columns, gateway_dev.update_columns)
    WITH (
        OIDS = FALSE
        )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway_dev.room
    OWNER to gateway_dev_01;

COMMENT ON TABLE gateway_dev.room
    IS '房间';

COMMENT ON COLUMN gateway_dev.room.id
    IS '主键';

COMMENT ON COLUMN gateway_dev.room.name
    IS '房间名称';

COMMENT ON COLUMN gateway_dev.room.capacity
    IS '房间容量';

COMMENT ON COLUMN gateway_dev.room.used
    IS '已加入成员';

COMMENT ON COLUMN gateway_dev.room.secret
    IS '房间密码';

COMMENT ON CONSTRAINT room_name_uni ON gateway_dev.room
    IS '房间名称唯一键';


-- Table: gateway_dev.room_member

-- DROP TABLE IF EXISTS gateway_dev.room_member;

CREATE TABLE IF NOT EXISTS gateway_dev.room_member
(
    -- Inherited from table gateway_dev.create_columns: create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table gateway_dev.create_columns: create_use_name character varying COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table gateway_dev.create_columns: create_time timestamp with time zone NOT NULL,
    -- Inherited from table gateway_dev.update_columns: update_use_id character varying COLLATE pg_catalog."default",
    -- Inherited from table gateway_dev.update_columns: update_use_name character varying COLLATE pg_catalog."default",
    -- Inherited from table gateway_dev.update_columns: update_time timestamp with time zone,
    id character varying COLLATE pg_catalog."default" NOT NULL,
    room_id character varying COLLATE pg_catalog."default" NOT NULL,
    user_id character varying COLLATE pg_catalog."default" NOT NULL,
    status integer NOT NULL,
    comment character varying COLLATE pg_catalog."default",
    CONSTRAINT room_member_pkey PRIMARY KEY (id)
    )
    INHERITS (gateway_dev.create_columns, gateway_dev.update_columns)
    WITH (
        OIDS = FALSE
        )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway_dev.room_member
    OWNER to gateway_dev_01;

COMMENT ON TABLE gateway_dev.room_member
    IS '房间成员';

COMMENT ON COLUMN gateway_dev.room_member.id
    IS '主键';

COMMENT ON COLUMN gateway_dev.room_member.room_id
    IS '房间id';

COMMENT ON COLUMN gateway_dev.room_member.user_id
    IS '用户id';

COMMENT ON COLUMN gateway_dev.room_member.status
    IS '房间成员状态';

COMMENT ON COLUMN gateway_dev.room_member.comment
    IS '备注';


-- Table: gateway_dev.room_member_client

-- DROP TABLE IF EXISTS gateway_dev.room_member_client;

CREATE TABLE IF NOT EXISTS gateway_dev.room_member_client
(
    -- Inherited from table gateway_dev.create_columns: create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table gateway_dev.create_columns: create_use_name character varying COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table gateway_dev.create_columns: create_time timestamp with time zone NOT NULL,
    -- Inherited from table gateway_dev.update_columns: update_use_id character varying COLLATE pg_catalog."default",
    -- Inherited from table gateway_dev.update_columns: update_use_name character varying COLLATE pg_catalog."default",
    -- Inherited from table gateway_dev.update_columns: update_time timestamp with time zone,
    id character varying COLLATE pg_catalog."default" NOT NULL,
    room_member_id character varying COLLATE pg_catalog."default" NOT NULL,
    client_id character varying COLLATE pg_catalog."default" NOT NULL,
    client_action integer NOT NULL,
    platform character varying COLLATE pg_catalog."default" NOT NULL,
    play_page_url character varying COLLATE pg_catalog."default",
    publish_page_url character varying COLLATE pg_catalog."default",
    status integer,
    CONSTRAINT room_member_client_pkey PRIMARY KEY (id)
    )
    INHERITS (gateway_dev.create_columns, gateway_dev.update_columns)
    WITH (
        OIDS = FALSE
        )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway_dev.room_member_client
    OWNER to gateway_dev_01;

COMMENT ON TABLE gateway_dev.room_member_client
    IS '房间成员 使用客户端';

-- Table: gateway_dev.invite_record

-- DROP TABLE IF EXISTS gateway_dev.invite_record;

CREATE TABLE IF NOT EXISTS gateway_dev.invite_record
(
    -- Inherited from table gateway_dev.create_columns: create_use_id character varying COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table gateway_dev.create_columns: create_use_name character varying COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table gateway_dev.create_columns: create_time timestamp with time zone NOT NULL,
    -- Inherited from table gateway_dev.update_columns: update_use_id character varying COLLATE pg_catalog."default",
    -- Inherited from table gateway_dev.update_columns: update_use_name character varying COLLATE pg_catalog."default",
    -- Inherited from table gateway_dev.update_columns: update_time timestamp with time zone,
    id character varying COLLATE pg_catalog."default" NOT NULL,
    resource_id character varying COLLATE pg_catalog."default" NOT NULL,
    resource_type integer NOT NULL,
    sender_id character varying COLLATE pg_catalog."default" NOT NULL,
    send_time timestamp with time zone NOT NULL,
    receiver_id character varying COLLATE pg_catalog."default",
    receive_time timestamp with time zone,
    status integer NOT NULL,
    result integer,
    message character varying COLLATE pg_catalog."default",
    CONSTRAINT invite_record_pkey PRIMARY KEY (id)
    )
    INHERITS (gateway_dev.create_columns, gateway_dev.update_columns)
    WITH (
        OIDS = FALSE
        )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS gateway_dev.invite_record
    OWNER to gateway_dev_01;

COMMENT ON TABLE gateway_dev.invite_record
    IS '邀请记录';