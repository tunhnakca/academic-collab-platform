package com.sau.learningplatform.Service;

import com.sau.learningplatform.Entity.Semester;
import com.sau.learningplatform.Repository.SemesterRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SemesterServiceImpl implements SemesterService{
    private SemesterRepository semesterRepository;

    public SemesterServiceImpl(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }


    @Override
    public Semester getCurrentSemester() {
        List<Semester> semesterList=semesterRepository.findByIsActiveTrue();
        if (semesterList.size()<1){
            throw new RuntimeException("There is No active Semester !");
        }
        if (semesterList.size()>1){
            throw new RuntimeException("There are multiple active Semesters !");
        }

        return semesterList.get(0);

    }


}
