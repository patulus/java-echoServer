import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoApplication {
    private static final int MIN_OPTION = 1;
    private static final int MAX_OPTION = 4;

    // 문자열을 객체로 역직렬화 또는 객체를 문자열로 직렬화를 담당하는 외부 라이브러리입니다.
    private final MessageMapper mapper = new MessageMapper();

    public void run(Socket socket) {
        try {
            // 사용자의 요청을 받기 위한 입력 스트림입니다.
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            // 사용자에게 응답하기 위한 출력 스트림입니다.
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            String received = null; // 요청 메시지

            // 요청 메시지가 EOF가 아닌 이상 사용자로부터 메시지를 계속 받을 수 있습니다.
            while ((received = in.readLine()) != null) {
                // 패킷을 역직렬화하여 객체로 변환합니다.
                MessageRequest request = mapper.deserializeRequest(received);

                String username = request.getUsername(); // 사용자 이름
                String beforeMessage = request.getMessage(); // 요청 메시지
                String afterMessage = null; // 응답 메시지

                MessageResponse response = null;

                // 옵션 검사
                int option = -1;
                try {
                    option = Integer.parseInt(request.getEchoOption());
                } catch (NumberFormatException e) {
                    // 옵션을 정수로 변환할 수 없는 경우 옵션을 다시 검사하여 오류 메시지를 응답합니다.
                    try {
                        // 옵션이 실수인 경우
                        Double.parseDouble(request.getEchoOption());
                        response = new MessageResponse(username, StateCode.INVALID_OPTION_NOT_INTEGER);
                    } catch (NumberFormatException ex) {
                        // 옵션이 정수도, 실수도 아니라면 문자열인 경우로 간주
                        response = new MessageResponse(username, StateCode.INVALID_OPTION_NOT_NUMERIC);
                    }

                    // 오류 메시지를 사용자에게 패킷으로 응답한 후, 새로운 요청을 기다립니다.
                    out.println(mapper.serialize(response));
                    out.flush();
                    continue;
                }

                // 옵션이 0 이하의 값인 경우
                if (option < MIN_OPTION) {
                    response = new MessageResponse(username, StateCode.INVALID_OPTION_NON_POSITIVE);
                    out.println(mapper.serialize(response));
                    out.flush();
                    continue;
                } else if (option >= MAX_OPTION) {
                    // 옵션이 4 이상의 값인 경우
                    response = new MessageResponse(username, StateCode.INVALID_OPTION_OUT_OF_RANGE);
                    out.println(mapper.serialize(response));
                    out.flush();
                    continue;
                }

                // 옵션 처리
                switch (option) {
                    case 1: // 일반 에코
                        afterMessage = beforeMessage;
                        break;
                    case 2: // 대문자로 에코
                        afterMessage = beforeMessage.toUpperCase();
                        break;
                    case 3: // 소문자로 에코
                        afterMessage = beforeMessage.toLowerCase();
                        break;
                }

                // 클라이언트에서 온 사용자 이름과 에코 옵션에 따라 '처리 전 메시지' 및 '처리 후 메시지'를 화면에 출력합니다.
                System.out.println("Before: [ " + username + " ]: " + beforeMessage);
                System.out.println("After : [ " + username + " ]: " + afterMessage);
                System.out.println();

                response = new MessageResponse(username, afterMessage);

                // 성공적으로 처리된 메시지를 다시 클라이언트에게 패킷으로 전송합니다.
                out.println(mapper.serialize(response));
                out.flush();
            }

            // 입력 스트림과 출력 스트림을 종료합니다.
            out.close();
            in.close();
        } catch (IOException e) {
        }
    }
}
