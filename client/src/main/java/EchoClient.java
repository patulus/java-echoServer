import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoClient {
    // 서버 측 호스트 이름과 포트를 지정합니다.
    private final String HOST_NAME;
    private final int HOST_PORT_NUMBER;

    // 객체를 문자열로 직렬화, 문자열을 객체로 역직렬화를 담당하는 외부 라이브러리입니다.
    private final MessageMapper mapper = new MessageMapper();

    public EchoClient(String hostName, int hostPortNumber) {
        this.HOST_NAME = hostName;
        this.HOST_PORT_NUMBER = hostPortNumber;
    }

    public void start() {
        // 사용자 콘솔에서 값을 입력 받을 입력 스트림입니다.
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        String username = null; // 사용자 이름
        // 사용자 이름을 입력 받습니다.
        try {
            // 올바른 사용자 이름이 입력될 때까지 반복합니다.
            while (true) {
                System.out.print("사용자 이름 입력 : ");

                username = inFromUser.readLine();
                // 사용자 이름 없이 EOF가 전송되는 경우 입력 스트림이 종료되었으므로 프로그램을 진행시키지 않고 강제 종료합니다.
                if (username == null) {
                    System.out.println("입력 스트림이 종료되어 프로그램을 실행할 수 없습니다.");
                    System.exit(1);
                }

                // 사용자 이름이 올바른 값이면 이 반복을 빠져나갑니다.
                if (!username.isBlank()) {
                    username = username.trim();
                    break;
                }
                // 사용자 이름이 공백이거나 길이가 0이면 다시 입력하도록 합니다.
                System.out.println("사용자 이름을 입력해야 합니다.");
            }
        } catch (IOException e) {
            System.out.println("쓰기 실패");
        }

        // 클라이언트 소켓을 생성합니다.
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(HOST_NAME, HOST_PORT_NUMBER);
        } catch (IOException e) {
            System.out.println("Failed to create a socket.");
            System.exit(1);
        }

        try {
            // 서버에서 온 응답을 받기 위한 입력 스트림입니다.
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            // 서버로 요청을 보내기 위한 출력 스트림입니다.
            PrintWriter outToServer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));

            String option = null; // 옵션
            String message = null; // 메시지
            String received = null; // 응답 메시지

            MessageRequest request = null; // 요청 메시지
            MessageResponse response = null; // 응답 메시지

            // TCP 연결이 되면 사용자로부터 입력을 반복해서 받습니다.
            while (true) {
                System.out.println("에코 옵션 (1: 일반 에코, 2: 대문자로 에코, 3: 소문자로 에코, 4: 채팅 종료");
                System.out.print("입력 : ");

                option = inFromUser.readLine();

                // 숫자 4가 입력되면 TCP 연결을 해제한 후 즉시 클라이언트를 종료합니다.
                if (option.equals("4")) {
                    System.out.println("서버와 연결을 종료합니다.");
                    break;
                }

                // 사용자에게 입력을 받습니다.
                System.out.print("메시지 입력 : ");
                message = inFromUser.readLine();

                // 서버로 요청을 직렬화하여 패킷을 보냅니다.
                request = new MessageRequest(username, option, message);
                outToServer.println(mapper.serialize(request));
                outToServer.flush();

                // 서버의 응답을 역직렬화합니다.
                received = inFromServer.readLine();
                response = mapper.deserializeResponse(received);

                // 정상 패킷이면 성공 메시지를 표시합니다.
                if (response.getStateCode() == StateCode.OK.getStateCode()) {
                    System.out.println("[ " + response.getUsername() + " ]: " + response.getMessage());
                } else {
                    // 오류 패킷이면 오류 메시지를 화면에 표시합니다.
                    System.out.println(response.getStateCode() + ", " + response.getMessage());

                    if (response.getStateCode() == StateCode.BYE.getStateCode()) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("쓰기 실패" + e.getMessage());
        }

        // TCP 연결을 해제합니다.
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
