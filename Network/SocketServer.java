import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) throws IOException {
        BufferedReader in = null;
        BufferedReader stin = null;
        BufferedWriter out = null;
        ServerSocket listener = null;
        Socket socket = null;

        listener = new ServerSocket(9999);
         socket = listener.accept();
         System.out.println("연결됨");
         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         stin =  new BufferedReader(new InputStreamReader(System.in));
         out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

         String inputMessage;
         while(true){
             inputMessage = in.readLine();
             if(inputMessage.equalsIgnoreCase("bye")){
                 break;
             }

             System.out.println(inputMessage);
             String outputMessage = stin.readLine();
             out.write("서버>" + outputMessage+"\n"); // 키보드에서 읽은 문자열 전송
             out.flush();
         }

        socket.close(); // 클라이언트 소켓 닫기
        listener.close(); // 서버 소켓 닫기

    }
}
