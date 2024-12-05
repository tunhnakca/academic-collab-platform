package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Project;

import java.util.List;

public interface ProjectService {
    Project findById(int id);

    List<Project> getProjectsByCourseId(int courseId);
}
