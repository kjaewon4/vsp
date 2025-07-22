package com.bu.startup.type;

public enum ItemStatus {

    DRAFT("임시 저장"),            // 작성 중 (비공개)
    PENDING("승인 대기"),         // 관리자 승인 대기
    APPROVED("전시 중"),          // 승인되어 전시 중
    REJECTED("승인 거절"),        // 관리자 승인 거절
    ARCHIVED("전시 종료"),        // 전시 종료/보관됨
    DELETED("삭제됨");            // 사용자/관리자 삭제

    private final String displayName;

    ItemStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
