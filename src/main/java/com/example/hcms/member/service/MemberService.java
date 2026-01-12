package com.example.hcms.member.service;

import com.example.hcms.member.domain.Member;
import com.example.hcms.member.dto.CreateMemberRequest;
import com.example.hcms.member.dto.MemberResponse;
import com.example.hcms.member.dto.UpdateMemberRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Service interface for Member operations
 */
public interface MemberService {

    /**
     * Create a new member
     *
     * @param request the create request
     * @param createdById the user creating the member
     * @return the created member response
     */
    MemberResponse createMember(@NonNull CreateMemberRequest request, Long createdById);

    /**
     * Get all members with pagination
     *
     * @param pageable pagination parameters
     * @return page of member responses
     */
    Page<MemberResponse> getAllMembers(@NonNull Pageable pageable);

    /**
     * Search members by name
     *
     * @param search search term
     * @param pageable pagination parameters
     * @return page of member responses
     */
    Page<MemberResponse> searchMembers(String search, @NonNull Pageable pageable);

    /**
     * Get member by ID
     *
     * @param id the member ID
     * @return the member
     */
    @NonNull Member getMemberById(@NonNull Long id);

    /**
     * Get member response by ID
     *
     * @param id the member ID
     * @return the member response
     */
    @NonNull MemberResponse getMemberResponseById(@NonNull Long id);

    /**
     * Get member by user ID
     *
     * @param userId the user ID
     * @return the member
     */
    @NonNull Member getMemberByUserId(@NonNull Long userId);

    /**
     * Get members by attendance group
     *
     * @param attendanceGroupId the attendance group ID
     * @return list of member responses
     */
    List<MemberResponse> getMembersByAttendanceGroup(@NonNull Long attendanceGroupId);

    /**
     * Update a member
     *
     * @param id the member ID
     * @param request the update request
     * @param updatedById the user performing the update
     * @return the updated member response
     */
    MemberResponse updateMember(@NonNull Long id, @NonNull UpdateMemberRequest request, Long updatedById);

    /**
     * Assign member to attendance group
     *
     * @param memberId the member ID
     * @param attendanceGroupId the attendance group ID
     * @param updatedById the user performing the action
     * @return the updated member response
     */
    MemberResponse assignToAttendanceGroup(@NonNull Long memberId, @NonNull Long attendanceGroupId, Long updatedById);

    /**
     * Remove member from attendance group
     *
     * @param memberId the member ID
     * @param updatedById the user performing the action
     * @return the updated member response
     */
    MemberResponse removeFromAttendanceGroup(@NonNull Long memberId, Long updatedById);

    /**
     * Delete a member
     *
     * @param id the member ID
     */
    void deleteMember(@NonNull Long id);

    /**
     * Convert member entity to response DTO
     *
     * @param member the member entity
     * @return the member response
     */
    @NonNull MemberResponse toResponse(@NonNull Member member);

    /**
     * Count members in an attendance group
     *
     * @param attendanceGroupId the attendance group ID
     * @return the count
     */
    long countByAttendanceGroup(@NonNull Long attendanceGroupId);
}
