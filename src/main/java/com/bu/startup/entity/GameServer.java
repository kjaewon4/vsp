package com.bu.startup.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Getter @Setter
@NoArgsConstructor
public class GameServer {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String executablePath; // 실행 파일 경로

    private String defaultParams; // 기본 실행 파라미터
    
    private boolean enabled = true;

	
}
