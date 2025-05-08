package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.ProjectResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {
    Project findById(int id);

    List<ProjectResponse> getProjectsByCourseId(int courseId);

    ResponseEntity<MessageResponse> deleteById(int id);

    List<ProjectResponse> searchByCourseCodeAndProjectTitle(String courseCode, String title);

    List<ProjectResponse> getAllByResponse();

    List<ProjectResponse> filterOrSort(String courseCode,String queryParam);

    MessageResponseWithStatus saveProjectToCourseWithCode(Project project, String courseCode);
}
