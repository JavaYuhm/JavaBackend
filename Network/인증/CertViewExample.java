import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CertViewExample {
    public static void main(String[] args) throws CertificateException, IOException {

        // 공인인증서 가져오기
        ClassPathResource resource = new ClassPathResource("signCert.der");

        Path path = Paths.get(resource.getURI());
        System.out.println(path);

        File certFile = new File(path.toString());
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = null;

        InputStream inputStream = new BufferedInputStream(new FileInputStream(certFile));
        try{
            cert = (X509Certificate) certificateFactory.generateCertificate(inputStream);
        } finally {
            try{
                inputStream.close();

            } catch (IOException ie){

            }
        }
        System.out.println(cert);
    }
}
