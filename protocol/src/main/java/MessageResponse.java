public class MessageResponse {
    private String username; // 사용자 이름
    private int stateCode; // 상태 코드
    private String message; // 메시지

    protected MessageResponse() {
    }

    public MessageResponse(String username, int stateCode, String message) {
        this.username = username;
        this.stateCode = stateCode;
        this.message = message;
    }

    public MessageResponse(String username, String message) {
        this(username, StateCode.OK.getStateCode(), message);
    }

    public MessageResponse(String username, StateCode stateCode) {
        this(username, stateCode.getStateCode(), stateCode.getMessage());
    }

    public String getUsername() {
        return username;
    }

    public int getStateCode() {
        return stateCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "username='" + username + '\'' +
                ", stateCode=" + stateCode +
                ", message='" + message + '\'' +
                '}';
    }
}
