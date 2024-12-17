package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.EntityResponse.ProjectResponse;
import com.sau.learningplatform.Repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService{
    private ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project findById(int id) {
        Optional<Project>result=projectRepository.findById(id);

        if (result.isEmpty()){
            throw new RuntimeException("Project not found with given id !");
        }
        return result.get();
    }


    @Override
    public List<ProjectResponse> getProjectsByCourseId(int courseId) {

        List<Project>projects=projectRepository.findByCourseId(courseId);

        if (projects.isEmpty()){
            log.info("No projects found for the given course!");
        }

        return projects.stream().map(this::mapToResponse).toList();

    }

    @Override
    public void deleteById(int id) {
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectResponse> searchByCourseIdAndProjectTitle(int courseId,String title) {
       List<Project>projects=projectRepository.findByCourseIdAndTitleContainingIgnoreCase(courseId,title);
       if (projects.isEmpty()){
           log.info("Any project related with search keyword is not found!");
       }
       return projects.stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<ProjectResponse> getAllByResponse() {
        List<Project> projects=projectRepository.findAll();
        if (projects.isEmpty()){
            log.info("There is no any project!");
        }
        return projects.stream().map(this::mapToResponse).toList();
    }


    ProjectResponse mapToResponse(Project project){

        return ProjectResponse
                .builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .isValid(project.getDateEnd().isAfter(LocalDateTime.now()))
                .startDate(project.getDateCreated())
                .endDate(project.getDateEnd())
                .build();

    }


}
