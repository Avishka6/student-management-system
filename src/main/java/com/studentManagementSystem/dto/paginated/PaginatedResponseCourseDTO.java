package com.studentManagementSystem.dto.paginated;


import com.studentManagementSystem.dto.CourseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginatedResponseCourseDTO {
    List<CourseDTO> content;
    private long dataCount;
}


