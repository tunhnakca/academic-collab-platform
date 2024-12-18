package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.EntityResponse.ProjectResponse;

import java.util.List;

public interface ProjectService {
    Project findById(int id);

    List<ProjectResponse> getProjectsByCourseId(int courseId);

    void deleteById(int id);

    List<ProjectResponse> searchByCourseCodeAndProjectTitle(String courseCode, String title);

    List<ProjectResponse> getAllByResponse();

    List<ProjectResponse> filterOrSort(String queryParam);
}
