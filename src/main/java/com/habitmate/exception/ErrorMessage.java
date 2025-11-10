package com.habitmate.exception;

public enum ErrorMessage {
    HABIT_NOT_FOUND("해당 ID의 습관이 존재하지 않습니다."),
    HABIT_DELETE_NOT_FOUND("삭제할 습관이 존재하지 않습니다."),
    HABIT_NAME_REQUIRED("습관 이름은 필수 입력입니다."),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
