package com.studentManagementSystem.dto.paginated;

import com.studentManagementSystem.dto.EnrollmentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedResponseEnrollmentDTO {
    List<EnrollmentDTO> content;
    private long dataCount;
}
