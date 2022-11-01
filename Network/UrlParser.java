import java.net.MalformedURLException;
import java.net.URL;

public class UrlParser {
    public static void main(String[] args) {
        URL opinion = null;
        URL homPage = null;

        try{
            homPage = new URL("https://tradeone.cj.net:80");
            opinion = new URL(homPage,"mpro/login.jsp"); // 상대 경로로 URL 객체 생성
        } catch (MalformedURLException e){
            System.out.println("잘못된 URL 입니다.");
        }
        System.out.println("protocol = " + opinion.getProtocol()); // 프로토콜 출력
        System.out.println("host = " + opinion.getHost()); // 호스트  이름 출력
        System.out.println("port = " + opinion.getPort()); // 포트 번호 출력
        System.out.println("path = " + opinion.getPath()); // 경로 부분 출력
        System.out.println("filename = " + opinion.getFile()); // 파일 이름 출력
    }

}
