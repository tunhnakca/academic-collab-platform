package com.sau.learningplatform.EntityResponse;

import com.sau.learningplatform.Entity.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SemesterAndMessageResponseWithStatusDTO {
    MessageResponseWithStatus messageResponseWithStatus;
    Semester semester;
}
