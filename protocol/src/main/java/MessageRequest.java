public class MessageRequest {
    private String username; // 사용자 이름
    private String echoOption; // 에코 옵션
    private String message; // 메시지

    protected MessageRequest() {
    }

    public MessageRequest(String username, String echoOption, String message) {
        this.username = username;
        this.echoOption = echoOption;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getEchoOption() {
        return echoOption;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageRequest{" +
                "username='" + username + '\'' +
                ", echoOption=" + echoOption +
                ", message='" + message + '\'' +
                '}';
    }
}
