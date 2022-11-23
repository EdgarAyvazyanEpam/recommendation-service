package com.recommendationservice.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edgar_Ayvazyan
 * @created 18/11/2022
 * @project recommendation-service
 */
public enum UploadedFIleStatusEnum {
    UPDATED(1, "ALREADY_PROCESSED"),
    STORED(2, "STORED");

    private static final Map<Integer, UploadedFIleStatusEnum> codeMap = new HashMap<>();

    static {
        for (UploadedFIleStatusEnum s : UploadedFIleStatusEnum.values()) {
            codeMap.put(s.getCode(), s);
        }
    }

    private final int code;
    private final String name;

    UploadedFIleStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static UploadedFIleStatusEnum getById(int id) {
        return codeMap.get(id);
    }
}
