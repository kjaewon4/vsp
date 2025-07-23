package com.bu.startup.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.bu.startup.type.CategoryType;
import com.bu.startup.type.ItemStatus;
import jakarta.persistence.*;
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
public class AssetBundleEntity extends BaseEntity{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bundleName;
    private String bundleTitle;
    private String bundleDescription;

    @ElementCollection
    @OrderColumn(name = "file_path_order") // 순서 컬럼 명시
    private List<String> filePath;

    // 번들 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;             // 작성자

    private String thumbnailPath;      // 대표 이미지 경로

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CategoryType category;

    private String detailDescription;  // 상세 설명

    private int views;                 // 조회수

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ItemStatus status;         // 번들 상태 (승인 대기, 승인됨 등)

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