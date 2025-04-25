package com.sau.learningplatform.EntityResponse;

import com.sau.learningplatform.Enum.EnumSeason;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SemesterResponse {

    Long id;

    private String season;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isActive;

    public String getStartDateFormatted() {
        if (startDate == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return startDate.format(formatter);
    }

    public String getEndDateFormatted() {
        if (endDate == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return endDate.format(formatter);
    }


}
