package com.bu.startup.type;

public enum ItemStatus {
    DRAFT,        // 작성 중 (비공개)
    PENDING,      // 관리자 승인 대기
    APPROVED,     // 승인되어 전시 중
    REJECTED,     // 승인 거절됨
    ARCHIVED,     // 전시 종료/보관됨
    DELETED       // 사용자 또는 관리자 삭제 (논리 삭제)
}
