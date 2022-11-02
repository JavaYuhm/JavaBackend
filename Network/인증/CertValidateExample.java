import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CertValidateExample {
    public static void main(String[] args) throws CertificateException, IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {

        // Bouncty Catle Provicer 추가
        Security.addProvider(new BouncyCastleProvider());

        // X.509 인증서

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        ClassPathResource resource = new ClassPathResource("signCert.der");
        Path path = Paths.get(resource.getURI());

        File certFile = new File(path.toString());
        X509Certificate cert = generateCertificate(certificateFactory, certFile);

        resource = new ClassPathResource("yessign.der");
        path = Paths.get(resource.getURI());
        File yessginFile = new File(path.toString());
        X509Certificate yessign = generateCertificate(certificateFactory, yessginFile);

        // 인증서는 「인증서 발행국 (CA)」에 의해 관리 및 보증
        // 최상위인증기관 인증서 - KISA RootCA 4(RSA)
        resource = new ClassPathResource("root-rsa-sha2.der");
        path = Paths.get(resource.getURI());

        File trustFile = new File(path.toString());
        X509Certificate trust = generateCertificate(certificateFactory, trustFile);

        List<X509Certificate> certificates = new ArrayList<X509Certificate>();

        certificates.add(yessign);

        CertPath certPath = certificateFactory.generateCertPath(certificates);
        TrustAnchor anchor = new TrustAnchor(trust, null);
        PKIXParameters params = new PKIXParameters(Collections.singleton(anchor));
        params.setRevocationEnabled(false); // 폐기 정보 비활성화, CRL 검증은 건너뜀
        CertPathValidator cpv = CertPathValidator.getInstance("PKIX", "BC");
        PKIXCertPathValidatorResult result;
        try {
            result = (PKIXCertPathValidatorResult) cpv.validate(certPath, params);
            System.out.println("유효한 인증서 입니다.");
            System.out.println();
            System.out.println(result);
        } catch (CertPathValidatorException e) {
            System.out.println("유효하지 않은 인증서 입니다.");
            e.printStackTrace();
        }
    }

    private static X509Certificate generateCertificate(CertificateFactory certificateFactory, File certFile) throws FileNotFoundException {

        X509Certificate x509Certificate;

        InputStream input = new BufferedInputStream(new FileInputStream(certFile));

        try{
            x509Certificate = (X509Certificate) certificateFactory.generateCertificate(input);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                input.close();

            } catch (IOException ie){

            }
        }
        return x509Certificate;

    }
}
