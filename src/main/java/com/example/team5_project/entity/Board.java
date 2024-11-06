package com.example.team5_project.entity;

import com.example.team5_project.dto.BoardResponseDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    
    @NotBlank
    private String boardTitle;
    
    public BoardResponseDto toResponseDto () {
    	return BoardResponseDto.builder()
    			.boardId(boardId)
    			.boardTitle(boardTitle)
    			.build();
    }
}