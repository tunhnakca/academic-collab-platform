package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Course;
import com.sau.learningplatform.Entity.Project;
import com.sau.learningplatform.EntityResponse.CourseResponse;
import com.sau.learningplatform.EntityResponse.MessageResponse;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.ProjectResponse;
import com.sau.learningplatform.Repository.CourseRepository;
import com.sau.learningplatform.Repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    public ProjectResponse getResponseById(int id) {
        Optional<Project>result=projectRepository.findById(id);

        if (result.isEmpty()){
            throw new RuntimeException("Project not found with given id !");
        }

        return mapToResponse(result.get());
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
    public ResponseEntity<MessageResponse> deleteById(int id) {
        try {
            log.info("project with id: {} is deleted!",id);
            projectRepository.deleteById(id);
            return new ResponseEntity<>(new MessageResponse("Project deleted successfully"), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Project could not be deleted");
            return new ResponseEntity<>(new MessageResponse("Project could not be deleted"), HttpStatus.OK);
        }
    }

    @Override
    public List<ProjectResponse> searchByCourseCodeAndProjectTitle(String courseCode, String title) {
       List<Project>projects=projectRepository.searchByCourseCodeAndTitlePrefix(courseCode,title);
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
    public List<ProjectResponse> filterOrSort(String courseCode,String queryParam) {
        List<Project>projects=new ArrayList<>();
        if (queryParam.equals("new")){
            projects=projectRepository.findByCourseCodeOrderByDateCreatedDesc(courseCode);
        }
        if (queryParam.equals("old")){
            projects=projectRepository.findByCourseCodeOrderByDateCreatedAsc(courseCode);
        }
        if (queryParam.equals("open")){
            projects=projectRepository.findByCourseCodeAndDateEndAfter(courseCode,LocalDateTime.now());
        }
        if (queryParam.equals("closed")){
            projects=projectRepository.findByCourseCodeAndDateEndBefore(courseCode,LocalDateTime.now());
        }
        return projects.stream().map(this::mapToResponse).toList();
    }


    @Override
    public MessageResponseWithStatus saveProjectToCourseWithCode(Project project, String courseCode) {
        Optional<Course> course=courseRepository.findByCode(courseCode);

        if (course.isEmpty()){
            log.error("No course found with given code !");
            return new MessageResponseWithStatus("Project could not be added!", false);
        }

        String htmlDescription=convertMarkdownToHtml(project.getDescription());

        project.setDescription(htmlDescription);
        project.setCourse(course.get());

        projectRepository.save(project);

        log.info("new project has been saved successfully!");
        return new MessageResponseWithStatus("New project has been saved successfully!", true);

    }


    private ProjectResponse mapToResponse(Project project){

        if (project.getCourse()==null){
            log.error("a project without course is found!");
        }

        CourseResponse courseResponse=CourseResponse.builder()
                .code(project.getCourse().getCode())
                .owner(project.getCourse().getOwner())
                .id(project.getCourse().getId())
                .title(project.getCourse().getTitle())
                .build();

        return ProjectResponse
                .builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .isValid(project.getDateEnd().isAfter(LocalDateTime.now()))
                .startDate(project.getDateCreated())
                .endDate(project.getDateEnd())
                .course(courseResponse)
                .build();

    }

    private String convertMarkdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(markdown));
    }


}
