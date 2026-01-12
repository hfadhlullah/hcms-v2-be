package com.example.hcms.member.controller;

import com.example.hcms.member.dto.CreateMemberRequest;
import com.example.hcms.member.dto.MemberResponse;
import com.example.hcms.member.dto.UpdateMemberRequest;
import com.example.hcms.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.lang.NonNull;
import java.util.List;

/**
 * REST controller for Member management endpoints
 */
@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "Members", description = "Member management endpoints")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Get all members with pagination
     *
     * @param pageable pagination parameters
     * @return page of members
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'ADMIN', 'EMPLOYEE')")
    @Operation(summary = "Get all members", description = "Get all members with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Members retrieved successfully")
    })
    public ResponseEntity<Page<MemberResponse>> getAllMembers(
            @PageableDefault(size = 20, sort = "firstName", direction = Sort.Direction.ASC) @NonNull Pageable pageable) {
        Page<MemberResponse> members = memberService.getAllMembers(pageable);
        return ResponseEntity.ok(members);
    }

    /**
     * Search members by name
     *
     * @param search search term
     * @param pageable pagination parameters
     * @return page of members
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'ADMIN', 'EMPLOYEE')")
    @Operation(summary = "Search members", description = "Search members by first or last name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    public ResponseEntity<Page<MemberResponse>> searchMembers(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "firstName", direction = Sort.Direction.ASC) @NonNull Pageable pageable) {
        Page<MemberResponse> members = memberService.searchMembers(search, pageable);
        return ResponseEntity.ok(members);
    }

    /**
     * Get a specific member by ID
     *
     * @param id the member ID
     * @return the member
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'ADMIN', 'EMPLOYEE')")
    @Operation(summary = "Get member by ID", description = "Get a specific member by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable @NonNull Long id) {
        MemberResponse member = memberService.getMemberResponseById(id);
        return ResponseEntity.ok(member);
    }

    /**
     * Get members by attendance group
     *
     * @param attendanceGroupId the attendance group ID
     * @return list of members
     */
    @GetMapping("/attendance-group/{attendanceGroupId}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'ADMIN', 'EMPLOYEE')")
    @Operation(summary = "Get members by attendance group", description = "Get all members in an attendance group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Members retrieved successfully")
    })
    public ResponseEntity<List<MemberResponse>> getMembersByAttendanceGroup(
            @PathVariable @NonNull Long attendanceGroupId) {
        List<MemberResponse> members = memberService.getMembersByAttendanceGroup(attendanceGroupId);
        return ResponseEntity.ok(members);
    }

    /**
     * Create a new member
     *
     * @param request the create request
     * @param authentication the authenticated user
     * @return the created member
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'ADMIN')")
    @Operation(summary = "Create a new member", description = "Create a new member in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MemberResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Member already exists")
    })
    public ResponseEntity<MemberResponse> createMember(
            @Valid @RequestBody @NonNull CreateMemberRequest request,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        MemberResponse member = memberService.createMember(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    /**
     * Update a member
     *
     * @param id the member ID
     * @param request the update request
     * @param authentication the authenticated user
     * @return the updated member
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'ADMIN')")
    @Operation(summary = "Update member", description = "Update an existing member's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody @NonNull UpdateMemberRequest request,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        MemberResponse member = memberService.updateMember(id, request, userId);
        return ResponseEntity.ok(member);
    }

    /**
     * Assign a member to an attendance group
     *
     * @param memberId the member ID
     * @param attendanceGroupId the attendance group ID
     * @param authentication the authenticated user
     * @return the updated member
     */
    @PostMapping("/{memberId}/assign-to-group/{attendanceGroupId}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'ADMIN')")
    @Operation(summary = "Assign member to attendance group", description = "Assign a member to an attendance group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Member or attendance group not found")
    })
    public ResponseEntity<MemberResponse> assignToAttendanceGroup(
            @PathVariable @NonNull Long memberId,
            @PathVariable @NonNull Long attendanceGroupId,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        MemberResponse member = memberService.assignToAttendanceGroup(memberId, attendanceGroupId, userId);
        return ResponseEntity.ok(member);
    }

    /**
     * Remove a member from their attendance group
     *
     * @param memberId the member ID
     * @param authentication the authenticated user
     * @return the updated member
     */
    @PostMapping("/{memberId}/remove-from-group")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'ADMIN')")
    @Operation(summary = "Remove member from attendance group", description = "Remove a member from their attendance group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member removed from group successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<MemberResponse> removeFromAttendanceGroup(
            @PathVariable @NonNull Long memberId,
            Authentication authentication) {
        Long userId = extractUserId(authentication);
        MemberResponse member = memberService.removeFromAttendanceGroup(memberId, userId);
        return ResponseEntity.ok(member);
    }

    /**
     * Delete a member
     *
     * @param id the member ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'ADMIN')")
    @Operation(summary = "Delete member", description = "Delete a member from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<Void> deleteMember(@PathVariable @NonNull Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Helper method to extract user ID from authentication
     */
    private Long extractUserId(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }
}
