package com.example.team5_project.dto;

import com.example.team5_project.entity.Board;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostDto {
    
    @NotBlank
    private String boardTitle;
    
    public Board toBoard() {
    	return Board.builder().
    			boardTitle(boardTitle).
    			build();
    }
}

