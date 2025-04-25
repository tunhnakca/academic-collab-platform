package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Semester;
import com.sau.learningplatform.EntityResponse.SemesterResponse;
import com.sau.learningplatform.Enum.EnumSeason;
import com.sau.learningplatform.Repository.SemesterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


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
    public void saveOrUpdateResponse(SemesterResponse semesterResponse) {

        Semester semester=new Semester(semesterResponse.getId(),semesterResponse.getStartDate(),semesterResponse.getEndDate());

        System.out.println(semesterResponse);

        if (semesterResponse.getSeason().equals("FALL") || semesterResponse.getSeason().equals("fall")){
            semester.setSeason(EnumSeason.FALL);
        }
        else if(semesterResponse.getSeason().equals("SPRING") || semesterResponse.getSeason().equals("spring")){
            semester.setSeason(EnumSeason.SPRING);
        }
        else {
            log.error("invalid season name!");
        }
        semesterRepository.save(semester);
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
