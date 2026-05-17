package com.learnova.progress.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressUpdateRequest {

    private Long userId;
    private String userEmail;
    private Long courseId;
    private Long lessonId;
    private Boolean completed;
}
