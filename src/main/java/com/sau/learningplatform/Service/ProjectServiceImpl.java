package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.EntityResponse.ProjectResponse;
import com.sau.learningplatform.Repository.CourseRepository;
import com.sau.learningplatform.Repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService{
    private ProjectRepository projectRepository;
    private CourseRepository courseRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, CourseRepository courseRepository) {
        this.projectRepository = projectRepository;
        this.courseRepository = courseRepository;
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
        else{
            log.info("{} project found related with given keyword",projects.size());
        }

        return projects.stream().map(this::mapToResponse).toList();

    }

    @Override
    public void deleteById(int id) {
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectResponse> searchByCourseCodeAndProjectTitle(String courseCode, String title) {
       List<Project>projects=projectRepository.findByCourseCodeAndTitleContainingIgnoreCase(courseCode,title);
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

    @Override
    public List<ProjectResponse> filterOrSort(String queryParam) {
        List<Project>projects=new ArrayList<>();
        if (queryParam.equals("new")){
            projects=projectRepository.findByOrderByDateCreatedDesc();
        }
        if (queryParam.equals("old")){
            projects=projectRepository.findByOrderByDateCreatedAsc();
        }
        if (queryParam.equals("open")){
            projects=projectRepository.findByDateEndAfter(LocalDateTime.now());
        }
        if (queryParam.equals("closed")){
            projects=projectRepository.findByDateEndBefore(LocalDateTime.now());
        }
        return projects.stream().map(this::mapToResponse).toList();
    }

    @Override
    public void saveProject(Project project, String courseCode) {
        Optional<Course> course=courseRepository.findByCode(courseCode);

        if (course.isEmpty()){
            throw new RuntimeException("No course found with given code !");
        }

        project.setCourse(course.get());

        projectRepository.save(project);

        log.info("new project has been saved successfully!");
    }


    private ProjectResponse mapToResponse(Project project){

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
