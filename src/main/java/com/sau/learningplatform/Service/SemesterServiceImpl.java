package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Semester;
import com.sau.learningplatform.EntityResponse.MessageResponseWithStatus;
import com.sau.learningplatform.EntityResponse.SemesterAndMessageDTO;
import com.sau.learningplatform.EntityResponse.SemesterAndMessageResponseWithStatusDTO;
import com.sau.learningplatform.EntityResponse.SemesterResponse;
import com.sau.learningplatform.Enum.EnumSeason;
import com.sau.learningplatform.Repository.SemesterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class SemesterServiceImpl implements SemesterService{
    private SemesterRepository semesterRepository;

    public SemesterServiceImpl(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }


    @Override
    public Semester getCurrentSemester() {
        List<Semester> semesterList=semesterRepository.findActiveSemesters(LocalDateTime.now());

        if (semesterList.size()<1){
            throw new RuntimeException("There is no active Semester !");
        }
        if (semesterList.size()>1){
            throw new RuntimeException("There are multiple active Semesters !");
        }

        return semesterList.get(0);

    }

    public SemesterResponse getActiveSemesterResponseIfNotEmptyResponse() {
        List<Semester> semesterList=semesterRepository.findActiveSemesters(LocalDateTime.now());

        if (semesterList.size()>1){
            throw new RuntimeException("There are multiple active Semesters !");
        }

        if (semesterList.size()<1){
            SemesterResponse semesterResponse= new SemesterResponse();
            semesterResponse.setIsActive(false);
            return semesterResponse;
        }

        return mapToSemesterResponse(semesterList.get(0));

    }

    @Override
    public SemesterAndMessageResponseWithStatusDTO saveOrUpdateResponse(SemesterResponse semesterResponse) {

        Semester semester=new Semester(semesterResponse.getId(),semesterResponse.getStartDate(),semesterResponse.getEndDate());

        //System.out.println(semesterResponse);

        SemesterAndMessageResponseWithStatusDTO semesterAndMessageResponseWithStatusDTO=new SemesterAndMessageResponseWithStatusDTO();

        if (semesterResponse.getSeason().equalsIgnoreCase("fall")){
            semester.setSeason(EnumSeason.FALL);
        }
        else if(semesterResponse.getSeason().equalsIgnoreCase("spring")){
            semester.setSeason(EnumSeason.SPRING);
        }
        else {
            log.error("invalid season name!");
            semesterAndMessageResponseWithStatusDTO.setMessageResponseWithStatus(new MessageResponseWithStatus("Semester could not be saved due to invalid season name!",false));
        }

        if (semesterResponse.getId()==null){
            log.info("new semester has been saved successfully!");
            semesterAndMessageResponseWithStatusDTO.setMessageResponseWithStatus(new MessageResponseWithStatus("new semester has been saved successfully",true));
        }
        else {
            log.info("semester has been updated successfully!");
            semesterAndMessageResponseWithStatusDTO.setMessageResponseWithStatus(new MessageResponseWithStatus("semester has been updated successfully",true));

        }
        Semester semesterEntity=semesterRepository.save(semester);

        semesterAndMessageResponseWithStatusDTO.setSemester(semesterEntity);

        return semesterAndMessageResponseWithStatusDTO;
    }

    @Override
    public Semester getClosestPastSemester() {
        Optional<Semester>optionalPastSemester=semesterRepository.findTopByEndDateBeforeOrderByEndDateDesc(LocalDateTime.now());
        if (optionalPastSemester.isEmpty()){
            throw new RuntimeException("previous semester is not found!");
        }
        return optionalPastSemester.get();
    }


    private SemesterResponse mapToSemesterResponse(Semester semester){
        return SemesterResponse.builder()
                .id(semester.getId())
                .endDate(semester.getEndDate())
                .startDate(semester.getStartDate())
                .season(String.valueOf(semester.getSeason()))
                .isActive(semester.getEndDate().isAfter(LocalDateTime.now()))
                .build();
    }






}
