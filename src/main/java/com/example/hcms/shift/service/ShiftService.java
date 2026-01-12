package com.example.hcms.shift.service;

import com.example.hcms.shift.domain.Shift;
import com.example.hcms.shift.domain.ShiftStatus;
import com.example.hcms.shift.dto.CreateShiftRequest;
import com.example.hcms.shift.dto.ShiftResponse;
import com.example.hcms.shift.dto.UpdateShiftRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

/**
 * Service interface for shift management
 */
public interface ShiftService {
    /**
     * Get a shift by ID
     */
    Shift getShiftById(@NonNull Long id);

    /**
     * Get all shifts with optional filtering
     */
    Page<ShiftResponse> getAllShifts(String search, ShiftStatus status, @NonNull Pageable pageable);

    /**
     * Create a new shift
     */
    ShiftResponse createShift(@NonNull CreateShiftRequest request, @NonNull Long userId);

    /**
     * Update an existing shift
     */
    ShiftResponse updateShift(@NonNull Long id, @NonNull UpdateShiftRequest request, @NonNull Long userId);

    /**
     * Delete (soft delete) a shift
     */
    void deleteShift(@NonNull Long id, @NonNull Long userId);

    /**
     * Convert shift entity to response DTO
     */
    ShiftResponse toResponse(Shift shift);
}
