package com.example.team5_project.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardResponseDto {

	private Long boardId;
    
    @NotBlank
    private String boardTitle;
}
