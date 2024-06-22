package com.jsbs.casemall.constant;

public enum ProductModelSelect {
    DEFAULT_MODEL("기본 모델"),
    IPHONE15("아이폰15"),
    IPHONE15PLUS("아이폰15플러스"),
    IPHONE15PRO("아이폰15PRO"),
    IPHONE15MAX("아이폰15MAX"),
    GALAXYS24("갤럭시S24"),
    GALAXYS24PLUS("갤럭시S24플러스"),
    GALAXYS24ULTRA("갤럭시S24울트라"),
    NOTE20("노트20"),
    NOTE20ULTRA("노트20울트라");

    private final String displayName;

    ProductModelSelect(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
