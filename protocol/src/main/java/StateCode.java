public enum StateCode {
    BYE(100, "서버와 연결을 종료합니다."),

    OK(200, null),

    INVALID_OPTION_NON_POSITIVE(401, "0 이하의 정수를 입력했습니다. 1부터 4까지의 정수만 입력할 수 있습니다."),
    INVALID_OPTION_NOT_INTEGER(402, "정수가 아닌 실수를 입력했습니다. 1부터 4까지의 정수만 입력할 수 있습니다."),
    INVALID_OPTION_OUT_OF_RANGE(403, "5 이상의 정수를 입력했습니다. 1부터 4까지의 정수만 입력할 수 있습니다."),
    INVALID_OPTION_NOT_NUMERIC(404, "문자열을 입력했습니다. 1부터 4까지의 정수만 입력할 수 있습니다.")
    ;

    private final int stateCode;
    private final String message;

    StateCode(int stateCode, String message) {
        this.stateCode = stateCode;
        this.message = message;
    }

    public int getStateCode() {
        return stateCode;
    }

    public String getMessage() {
        return message;
    }
}
