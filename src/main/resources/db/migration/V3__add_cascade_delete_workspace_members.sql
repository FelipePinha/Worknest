ALTER TABLE tb_workspace_members
DROP CONSTRAINT fk_tb_workspace_members_workplace_id;

ALTER TABLE tb_workspace_members
ADD CONSTRAINT fk_tb_workspace_members_workplace_id
FOREIGN KEY (workplace_id) REFERENCES tb_workspaces(id) ON DELETE CASCADE;
