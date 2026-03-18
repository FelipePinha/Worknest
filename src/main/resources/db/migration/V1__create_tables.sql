CREATE TYPE task_status AS ENUM ('TODO', 'IN_PROGRESS', 'DONE');
CREATE TYPE user_role AS ENUM ('OWNER', 'CONTRIBUTOR');

CREATE TABLE tb_users (
    id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT pk_tb_users PRIMARY KEY (id),
    CONSTRAINT uk_tb_users_email UNIQUE (email)
);

CREATE TABLE tb_workplaces (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT pk_tb_workplaces PRIMARY KEY (id)
);

CREATE TABLE tb_tasks (
    id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status task_status NOT NULL,
    workspace_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT pk_tb_tasks PRIMARY KEY (id),
    CONSTRAINT fk_tb_tasks_workspace_id FOREIGN KEY (workspace_id) REFERENCES tb_workplaces(id)
);

CREATE TABLE tb_workspace_members (
    id UUID NOT NULL,
    user_id UUID,
    workplace_id UUID,
    role user_role,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT pk_tb_workspace_members PRIMARY KEY (id),
    CONSTRAINT fk_tb_workspace_members_user_id FOREIGN KEY (user_id) REFERENCES tb_users(id),
    CONSTRAINT fk_tb_workspace_members_workplace_id FOREIGN KEY (workplace_id) REFERENCES tb_workplaces(id)
);
