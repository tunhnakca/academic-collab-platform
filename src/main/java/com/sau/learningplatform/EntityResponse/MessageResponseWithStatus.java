package com.sau.learningplatform.EntityResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseWithStatus {
    String message;
    Boolean isStatusOk;
}
