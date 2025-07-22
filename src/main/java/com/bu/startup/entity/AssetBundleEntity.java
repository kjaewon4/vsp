package com.bu.startup.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetBundleEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bundleName;
    private String bundleTitle;
    private String bundleDescription;
    
    private String[] filePath;

    private LocalDateTime uploadedAt;
    
    @PrePersist
    public void prePersist() {
        if (this.bundleTitle == null) {
            this.bundleTitle = "번들기본타이틀";
        }
        if (this.bundleDescription == null) {
            this.bundleDescription = "번들기본설명";
        }
    }
    
}