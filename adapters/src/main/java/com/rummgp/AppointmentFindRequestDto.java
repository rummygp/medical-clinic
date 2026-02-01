package com.rummgp;

import lombok.Builder;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Builder
public record AppointmentFindRequestDto(@RequestParam(required = false) Long doctorId,
                                        @RequestParam(required = false) Long patientId,
                                        @RequestParam(required = false) String specialization,
                                        @RequestParam(required = false) LocalDateTime startTime,
                                        @RequestParam(required = false) LocalDateTime endTime,
                                        @RequestParam(required = false) Boolean freeSlots,
                                        @RequestParam(defaultValue = "0") int pageNumber,
                                        @RequestParam(defaultValue = "20") int pageSize) {
}
