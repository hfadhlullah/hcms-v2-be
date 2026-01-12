package com.example.hcms.attendanceGroup.domain;

/**
 * How members are tracked for time attendance
 */
public enum MemberTrackingMode {
    ALL, // All members tracked
    CUSTOM, // Custom selection with conditions
    NONE // No members tracked
}
