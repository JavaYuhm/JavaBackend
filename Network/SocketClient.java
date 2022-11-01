import java.io.*;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        BufferedReader in = null;
        BufferedReader stin = null;
        BufferedWriter out = null;
        Socket socket = null;

        socket = new Socket("localhost", 9999);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        stin = new BufferedReader(new InputStreamReader(System.in));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        String outputMessage;
        while(true){
            outputMessage = stin.readLine();
            if(outputMessage.equalsIgnoreCase("bye")){
                out.write(outputMessage);
                out.flush();
                break;
            }
            out.write("<클라이언트>" + outputMessage);
            out.flush();
            String inputMessage  = in.readLine();
            System.out.println(inputMessage);
        }
        socket.close(); // 클라이언트 소켓 닫기

    }
}
