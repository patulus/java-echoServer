import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 요청을 직렬화합니다. (객체 -> 문자열)
    public String serialize(MessageRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }

    // 응답을 직렬화합니다. (객체 -> 문자열)
    public String serialize(MessageResponse response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(response);
    }

    // 요청을 역직렬화합니다. (문자열 -> 객체)
    public MessageRequest deserializeRequest(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, MessageRequest.class);
    }

    // 응답을 역직렬화합니다. (문자열 -> 객체)
    public MessageResponse deserializeResponse(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, MessageResponse.class);
    }
}
