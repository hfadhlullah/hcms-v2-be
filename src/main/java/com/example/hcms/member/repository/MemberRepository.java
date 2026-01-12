package com.example.hcms.member.repository;

import com.example.hcms.member.domain.Member;
import com.example.hcms.member.domain.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Member entity operations
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Find member by user ID
     *
     * @param userId the user ID
     * @return Optional containing member if found
     */
    Optional<Member> findByUserId(Long userId);

    /**
     * Find members by attendance group ID
     *
     * @param attendanceGroupId the attendance group ID
     * @return list of members
     */
    List<Member> findByAttendanceGroupId(Long attendanceGroupId);

    /**
     * Count members in an attendance group
     *
     * @param attendanceGroupId the attendance group ID
     * @return number of members
     */
    long countByAttendanceGroupId(Long attendanceGroupId);

    /**
     * Find members by status with pagination
     *
     * @param status the member status
     * @param pageable pagination parameters
     * @return page of members
     */
    Page<Member> findByStatus(MemberStatus status, Pageable pageable);

    /**
     * Find members by status and name search
     *
     * @param status the member status
     * @param searchTerm search term for first or last name
     * @param pageable pagination parameters
     * @return page of members
     */
    Page<Member> findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(
            MemberStatus status, String searchTerm1, MemberStatus status2, String searchTerm2, Pageable pageable);

    /**
     * Check if member exists by user ID
     *
     * @param userId the user ID
     * @return true if member exists
     */
    boolean existsByUserId(Long userId);

    /**
     * Check if employee number exists
     *
     * @param employeeNumber the employee number
     * @return true if exists
     */
    boolean existsByEmployeeNumber(String employeeNumber);
}
