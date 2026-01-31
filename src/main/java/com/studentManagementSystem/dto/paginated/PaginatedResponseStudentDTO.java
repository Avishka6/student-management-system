package com.studentManagementSystem.dto.paginated;

import com.studentManagementSystem.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedResponseStudentDTO {
    private List<StudentDTO> content;
    private long dataCount;
}
