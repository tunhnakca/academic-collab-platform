package com.sau.learningplatform.Service;
import com.sau.learningplatform.Entity.Semester;

import com.sau.learningplatform.EntityResponse.SemesterAndMessageResponseWithStatusDTO;
import com.sau.learningplatform.EntityResponse.SemesterResponse;


public interface SemesterService {
    Semester getCurrentSemester();
    SemesterResponse getActiveSemesterResponseIfNotEmptyResponse();

    SemesterAndMessageResponseWithStatusDTO saveOrUpdateResponse(SemesterResponse semesterResponse);

    Semester getClosestPastSemester();
}
