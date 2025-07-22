package com.bu.startup.type;

public enum CategoryType {

    IT_SOFTWARE("IT/소프트웨어"),
    ECOMMERCE("전자상거래 및 유통"),
    EDUCATION("교육"),
    HEALTHCARE("헬스케어/바이오"),
    MEDIA_CONTENT("미디어 및 콘텐츠"),
    FINTECH("핀테크 및 블록체인"),
    SOCIAL_IMPACT("사회적 가치 창출"),
    FOOD("음식 및 외식 산업"),
    FASHION_BEAUTY("패션 및 뷰티 산업"),
    LIFESTYLE("생활 및 리빙 분야"),
    ETC("기타");

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
