package com.sau.learningplatform.EntityResponse;

import com.sau.learningplatform.Enum.EnumSeason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemesterResponse {

    int id;

    private String season;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isActive;


}
