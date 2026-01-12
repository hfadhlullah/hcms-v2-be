-- V007: Create Member table and refactor user_attendance_group relationship
-- This migration separates member/employee profile management from authentication

-- Create member table to store employee profile information
CREATE TABLE member (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    department_id BIGINT,
    job_title VARCHAR(100),
    alias VARCHAR(100),
    desk_id VARCHAR(50),
    phone_extension VARCHAR(20),
    employee_number VARCHAR(50) UNIQUE,
    gender VARCHAR(20),
    workforce_type VARCHAR(50),
    date_of_employment DATE,
    country VARCHAR(100),
    city VARCHAR(100),
    direct_manager VARCHAR(100),
    dotted_line_manager VARCHAR(100),
    attendance_group_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by_id BIGINT,
    
    -- Indexes
    INDEX idx_user_id (user_id),
    INDEX idx_attendance_group_id (attendance_group_id),
    INDEX idx_employee_number (employee_number),
    
    -- Foreign key to user table
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Remove member-related columns from user table (if they exist)
-- These have been moved to the member table
ALTER TABLE user 
    DROP COLUMN IF EXISTS first_name,
    DROP COLUMN IF EXISTS last_name,
    DROP COLUMN IF EXISTS phone_number,
    DROP COLUMN IF EXISTS department_id,
    DROP COLUMN IF EXISTS job_title,
    DROP COLUMN IF EXISTS alias,
    DROP COLUMN IF EXISTS desk_id,
    DROP COLUMN IF EXISTS phone_extension,
    DROP COLUMN IF EXISTS employee_number,
    DROP COLUMN IF EXISTS user_identifier,
    DROP COLUMN IF EXISTS gender,
    DROP COLUMN IF EXISTS workforce_type,
    DROP COLUMN IF EXISTS date_of_employment,
    DROP COLUMN IF EXISTS country,
    DROP COLUMN IF EXISTS city,
    DROP COLUMN IF EXISTS direct_manager,
    DROP COLUMN IF EXISTS dotted_line_manager,
    DROP COLUMN IF EXISTS attendance_group_id;
