ALTER TABLE user ADD COLUMN IF NOT EXISTS attendance_group_id BIGINT;
ALTER TABLE user ADD CONSTRAINT fk_user_attendance_group FOREIGN KEY (attendance_group_id) REFERENCES attendance_groups(id) ON DELETE SET NULL;
CREATE INDEX idx_user_attendance_group ON user(attendance_group_id);
