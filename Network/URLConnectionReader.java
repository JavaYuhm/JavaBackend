import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionReader {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://tradeone.cj.net/mpro/");
            URLConnection uc = url.openConnection(); // URL 객체에서 URLConnection 객체 생성
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream())); // 입력 스트림 생성
            String inpuLine;

            while((inpuLine = in.readLine()) != null)
                System.out.println(inpuLine);
        } catch (IOException e) {
            System.out.println("URL에서 데이터를 읽는 중 오류가 발생했습니다.");
        }

    }
}
