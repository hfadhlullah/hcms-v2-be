package com.example.hcms.member.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hcms.member.domain.Member;
import com.example.hcms.member.domain.MemberStatus;
import com.example.hcms.member.dto.CreateMemberRequest;
import com.example.hcms.member.dto.MemberResponse;
import com.example.hcms.member.dto.UpdateMemberRequest;
import com.example.hcms.member.repository.MemberRepository;

/**
 * Implementation of MemberService
 */
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final MemberRepository repository;

    public MemberServiceImpl(MemberRepository repository) {
        this.repository = repository;
    }

    @Override
    public MemberResponse createMember(@NonNull CreateMemberRequest request, Long createdById) {
        logger.info("Creating member for user: {}", request.getUserId());

        // Check if member already exists for this user
        if (repository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("Member already exists for user: " + request.getUserId());
        }

        // Check if employee number is unique
        if (request.getEmployeeNumber() != null && repository.existsByEmployeeNumber(request.getEmployeeNumber())) {
            throw new RuntimeException("Employee number already exists: " + request.getEmployeeNumber());
        }

        Member member = new Member(request.getUserId());
        mapRequestToEntity(request, member);
        member.setCreatedById(createdById);

        Member saved = repository.save(member);
        logger.info("Created member with ID: {}", saved.getId());

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberResponse> getAllMembers(@NonNull Pageable pageable) {
        return repository.findByStatus(MemberStatus.ACTIVE, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberResponse> searchMembers(String search, @NonNull Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getAllMembers(pageable);
        }

        return repository.findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(
                MemberStatus.ACTIVE, search.trim(), MemberStatus.ACTIVE, search.trim(), pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull Member getMemberById(@NonNull Long id) {
        Member member = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));

        return Objects.requireNonNull(member);
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull MemberResponse getMemberResponseById(@NonNull Long id) {
        return toResponse(getMemberById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull Member getMemberByUserId(@NonNull Long userId) {
        Member member = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Member not found for user: " + userId));

        return Objects.requireNonNull(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> getMembersByAttendanceGroup(@NonNull Long attendanceGroupId) {
        return repository.findByAttendanceGroupId(attendanceGroupId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MemberResponse updateMember(@NonNull Long id, @NonNull UpdateMemberRequest request, Long updatedById) {
        logger.info("Updating member: {}", id);

        Member member = getMemberById(id);

        // Check if employee number is being changed and if it's unique
        if (request.getEmployeeNumber() != null &&
                !request.getEmployeeNumber().equals(member.getEmployeeNumber()) &&
                repository.existsByEmployeeNumber(request.getEmployeeNumber())) {
            throw new RuntimeException("Employee number already exists: " + request.getEmployeeNumber());
        }

        mapRequestToEntity(request, member);
        Member updated = repository.save(member);

        logger.info("Updated member: {}", id);
        return toResponse(updated);
    }

    @Override
    public MemberResponse assignToAttendanceGroup(@NonNull Long memberId, @NonNull Long attendanceGroupId, Long updatedById) {
        logger.info("Assigning member: {} to attendance group: {}", memberId, attendanceGroupId);

        Member member = getMemberById(memberId);
        member.setAttendanceGroupId(attendanceGroupId);

        Member updated = repository.save(member);
        logger.info("Assigned member: {} to attendance group: {}", memberId, attendanceGroupId);

        return toResponse(updated);
    }

    @Override
    public MemberResponse removeFromAttendanceGroup(@NonNull Long memberId, Long updatedById) {
        logger.info("Removing member: {} from attendance group", memberId);

        Member member = getMemberById(memberId);
        member.setAttendanceGroupId(null);

        Member updated = repository.save(member);
        logger.info("Removed member: {} from attendance group", memberId);

        return toResponse(updated);
    }

    @Override
    public void deleteMember(@NonNull Long id) {
        logger.info("Deleting member: {}", id);
        repository.deleteById(id);
        logger.info("Deleted member: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public @NonNull MemberResponse toResponse(@NonNull Member member) {
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setUserId(member.getUserId());
        response.setFirstName(member.getFirstName());
        response.setLastName(member.getLastName());
        response.setPhoneNumber(member.getPhoneNumber());
        response.setDepartmentId(member.getDepartmentId());
        response.setJobTitle(member.getJobTitle());
        response.setAlias(member.getAlias());
        response.setDeskId(member.getDeskId());
        response.setPhoneExtension(member.getPhoneExtension());
        response.setEmployeeNumber(member.getEmployeeNumber());
        response.setGender(member.getGender());
        response.setWorkforceType(member.getWorkforceType());
        response.setDateOfEmployment(member.getDateOfEmployment());
        response.setCountry(member.getCountry());
        response.setCity(member.getCity());
        response.setDirectManager(member.getDirectManager());
        response.setDottedLineManager(member.getDottedLineManager());
        response.setAttendanceGroupId(member.getAttendanceGroupId());
        response.setStatus(member.getStatus());
        response.setCreatedAt(member.getCreatedAt());
        response.setUpdatedAt(member.getUpdatedAt());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAttendanceGroup(@NonNull Long attendanceGroupId) {
        return repository.countByAttendanceGroupId(attendanceGroupId);
    }

    private void mapRequestToEntity(CreateMemberRequest request, Member member) {
        if (request.getFirstName() != null) member.setFirstName(request.getFirstName());
        if (request.getLastName() != null) member.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) member.setPhoneNumber(request.getPhoneNumber());
        if (request.getDepartmentId() != null) member.setDepartmentId(request.getDepartmentId());
        if (request.getJobTitle() != null) member.setJobTitle(request.getJobTitle());
        if (request.getAlias() != null) member.setAlias(request.getAlias());
        if (request.getDeskId() != null) member.setDeskId(request.getDeskId());
        if (request.getPhoneExtension() != null) member.setPhoneExtension(request.getPhoneExtension());
        if (request.getEmployeeNumber() != null) member.setEmployeeNumber(request.getEmployeeNumber());
        if (request.getGender() != null) member.setGender(request.getGender());
        if (request.getWorkforceType() != null) member.setWorkforceType(request.getWorkforceType());
        if (request.getDateOfEmployment() != null) member.setDateOfEmployment(request.getDateOfEmployment());
        if (request.getCountry() != null) member.setCountry(request.getCountry());
        if (request.getCity() != null) member.setCity(request.getCity());
        if (request.getDirectManager() != null) member.setDirectManager(request.getDirectManager());
        if (request.getDottedLineManager() != null) member.setDottedLineManager(request.getDottedLineManager());
        if (request.getAttendanceGroupId() != null) member.setAttendanceGroupId(request.getAttendanceGroupId());
    }

    private void mapRequestToEntity(UpdateMemberRequest request, Member member) {
        if (request.getFirstName() != null) member.setFirstName(request.getFirstName());
        if (request.getLastName() != null) member.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) member.setPhoneNumber(request.getPhoneNumber());
        if (request.getDepartmentId() != null) member.setDepartmentId(request.getDepartmentId());
        if (request.getJobTitle() != null) member.setJobTitle(request.getJobTitle());
        if (request.getAlias() != null) member.setAlias(request.getAlias());
        if (request.getDeskId() != null) member.setDeskId(request.getDeskId());
        if (request.getPhoneExtension() != null) member.setPhoneExtension(request.getPhoneExtension());
        if (request.getEmployeeNumber() != null) member.setEmployeeNumber(request.getEmployeeNumber());
        if (request.getGender() != null) member.setGender(request.getGender());
        if (request.getWorkforceType() != null) member.setWorkforceType(request.getWorkforceType());
        if (request.getDateOfEmployment() != null) member.setDateOfEmployment(request.getDateOfEmployment());
        if (request.getCountry() != null) member.setCountry(request.getCountry());
        if (request.getCity() != null) member.setCity(request.getCity());
        if (request.getDirectManager() != null) member.setDirectManager(request.getDirectManager());
        if (request.getDottedLineManager() != null) member.setDottedLineManager(request.getDottedLineManager());
        if (request.getAttendanceGroupId() != null) member.setAttendanceGroupId(request.getAttendanceGroupId());
        if (request.getStatus() != null) member.setStatus(request.getStatus());
    }
}
