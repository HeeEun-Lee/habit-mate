package com.habitmate.exception;

public enum ErrorMessage {
    //habit
    HABIT_NOT_FOUND("해당 습관을 찾을 수 없습니다."),
    HABIT_DELETE_NOT_FOUND("삭제할 습관이 존재하지 않습니다."),
    HABIT_NAME_REQUIRED("습관 이름은 필수 입력입니다."),
    HABIT_LIST_EMPTY("등록된 습관이 없습니다."),
    NO_COMPLETED_HABITS("완료된 습관이 없습니다."),
    HABIT_FORBIDDEN("본인의 습관만 접근할 수 있습니다."),
    // user
    USER_NOT_FOUND("존재하지 않는 이메일입니다."),
    USER_ALREADY_EXISTS("이미 사용 중인 이메일입니다."),
    USER_NOT_AUTHENTICATED("인증 정보가 없습니다. 로그인 후 이용해주세요."),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다."),
    LOGIN_USER_NOT_FOUND("로그인한 사용자를 찾을 수 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
