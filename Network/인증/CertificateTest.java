
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.security.cert.X509Extension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.x509.X509V2CRLGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
// import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;

// 자바 인증서 예제
// ref : http://cris.joongbu.ac.kr/course/2017-1/jcp/example/certificate/CertificateTest.java
@SuppressWarnings("deprecation")
public class CertificateTest {
    // Leaf certificate (end-entity certificate)
    enum CertType {ROOT,INTER,ENDENTITY};


    // 1. RSA KeyPair
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator 객체를 Public Key/Private Key 쌍을 특정 알고리즘으로 Retrun
        KeyPairGenerator kpg  = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        return kpg.genKeyPair();
    }


    public static X509Certificate generateCertificate(
            X500Principal subjectDN,
            PublicKey pubKey,
            PrivateKey signatureKey,
            X509Certificate caCert,
            CertType type)
            throws CertificateEncodingException,NoSuchProviderException,NoSuchAlgorithmException,SignatureException,InvalidKeyException,CertificateParsingException {
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        if(type==CertType.ROOT)
            certGen.setIssuerDN(subjectDN);
        else
            certGen.setIssuerDN(caCert.getSubjectX500Principal());
        certGen.setSubjectDN(subjectDN);
        GregorianCalendar currentDate = new GregorianCalendar();
        GregorianCalendar expiredDate
                = new GregorianCalendar(currentDate.get(Calendar.YEAR)+2,currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH));
        certGen.setNotBefore(currentDate.getTime());
        certGen.setNotAfter(expiredDate.getTime());
        certGen.setPublicKey(pubKey);
        certGen.setSignatureAlgorithm("SHA1withRSAEncryption");
        if(type!=CertType.ROOT){
            certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false,
                    new AuthorityKeyIdentifierStructure(caCert));
//			certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, 
            // new SubjectKeyIdentifierStructure(pubKey));
        }
        if(type!=CertType.ENDENTITY){
            certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(0));
            certGen.addExtension(X509Extensions.KeyUsage, true,
                    new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));
        }
        else
            certGen.addExtension(X509Extensions.KeyUsage, true,
                    new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
        return certGen.generate(signatureKey,"BC");
    }

    // 3. CRL
    public static X509CRL generateCRL(
            X509Certificate caCert,
            PrivateKey signatureKey,
            BigInteger serialNumber)
            throws CRLException,NoSuchProviderException,NoSuchAlgorithmException,SignatureException,InvalidKeyException{
        X509V2CRLGenerator crlGen = new X509V2CRLGenerator();
        crlGen.setIssuerDN(caCert.getSubjectX500Principal());
        GregorianCalendar currentDate = new GregorianCalendar();
        GregorianCalendar nextDate
                = new GregorianCalendar(currentDate.get(Calendar.YEAR)+1,(currentDate.get(Calendar.MONTH)+1)%12,currentDate.get(Calendar.DAY_OF_MONTH));
        crlGen.setThisUpdate(currentDate.getTime());
        crlGen.setNextUpdate(nextDate.getTime());
        crlGen.setSignatureAlgorithm("SHA1withRSAEncryption");
        if(serialNumber!=null)
            crlGen.addCRLEntry(serialNumber, currentDate.getTime(),
                    CRLReason.superseded);
        return crlGen.generate(signatureKey,"BC");
    }

    public static void main(String[] args) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        // BouncyCastle Provider
        Security.addProvider(new BouncyCastleProvider());

        System.out.println("* 1. Start : Create KeyPairs ");

        try{
            // 루트
            KeyPair rootKeyPair = generateRSAKeyPair();
            // 루트 Certifacate X.509
            X509Certificate rootCert = generateCertificate(
                    new X500Principal("C=KR,CN=ROOT"), rootKeyPair.getPublic(),
                    rootKeyPair.getPrivate(), null, CertType.ROOT);
            System.out.println("Create : rootCert");
            //System.out.println(rootCert);
            KeyPair interKeyPair = generateRSAKeyPair();
            X509Certificate interCert = generateCertificate(
                    new X500Principal("C=KR,CN=INTER"), interKeyPair.getPublic(),
                    rootKeyPair.getPrivate(), rootCert, CertType.INTER);
            System.out.println("Create interCert ");
            //System.out.println(interCert);
            KeyPair aliceKeyPair = generateRSAKeyPair();
            X509Certificate aliceCert = generateCertificate(
                    new X500Principal("C=KR,O=KUT,OU=IME,CN=Alice"), aliceKeyPair.getPublic(),
                    interKeyPair.getPrivate(), interCert, CertType.ENDENTITY);
            System.out.println("- Create Alice Cert  ");
            //System.out.println(aliceCert);
            KeyPair bobKeyPair = generateRSAKeyPair();
            X509Certificate bobCert = generateCertificate(
                    new X500Principal("C=KR,CN=Bob"), bobKeyPair.getPublic(),
                    interKeyPair.getPrivate(), interCert, CertType.ENDENTITY);
            System.out.println("- Create Bob Cert ");
            //System.out.println(bobCert);
            System.out.println("Bob: "+bobCert.getSerialNumber());

            // 1.1
            System.out.println("1.1 Verfify Public Key");
            rootCert.verify(rootKeyPair.getPublic());
            interCert.verify(rootKeyPair.getPublic());
            aliceCert.verify(interKeyPair.getPublic());
            bobCert.verify(interKeyPair.getPublic());

            // 1.2
            System.out.println("1.2");
            try{
                // 인증서 기간 체크
                aliceCert.checkValidity(new Date());
            }
            catch(CertificateExpiredException cee){
                cee.printStackTrace();
            }
            catch(CertificateNotYetValidException cnyve){
                cnyve.printStackTrace();
            }
            aliceCert.verify(interKeyPair.getPublic());  // Alice Verify
            System.out.println();

            // 2.(CRL)

            X509CRL rootCRL = generateCRL(rootCert,rootKeyPair.getPrivate(),null);

            X509CRL interCRL = generateCRL(interCert,interKeyPair.getPrivate(),null);

            // X509CRL interCRL = generateCRL(interCert,interKeyPair.getPrivate(),bobCert.getSerialNumber());

            System.out.println("* 2. CRL TEST ");
            System.out.println("- Root CRL ");
            //System.out.println(rootCRL);
            System.out.println("- Inter CRL");
            //System.out.println(interCRL);
            System.out.println("InterCRL REVOKE : "+interCRL.getRevokedCertificates());

            // 2.1 CRL
            System.out.println("2.1 CRL Verify");
            rootCRL.verify(rootKeyPair.getPublic());
            interCRL.verify(interKeyPair.getPublic());
            System.out.println();

            // 3.
            // 3.1 Alice
            System.out.println("* 3.  ");
            System.out.println("3.1 ");
            System.out.println("- Alice ");
            //System.out.println(aliceCert);
            System.out.println("- Alice Cert ");
            System.out.println(aliceCert.getPublicKey());
            FileOutputStream fos = new FileOutputStream(new File("aliceCert.der"));
            fos.write(aliceCert.getEncoded());
            fos.close();

            // 3.2 Alice
            System.out.println("* 3.2  ");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            FileInputStream fis = new FileInputStream(new File("aliceCert.der"));
            X509Certificate cert = (X509Certificate)cf.generateCertificate(fis);
            fis.close();
            System.out.println("- Alice GET public ");
            //System.out.println(cert);
            System.out.println(cert.getPublicKey());
            System.out.println();

            // 4.
            System.out.println("* 4.  ");

            // 4.1
            System.out.println("* 4.1 ");
            System.out.println(" - Alice GET private ");
            System.out.println(aliceKeyPair.getPrivate()); 	

            String secretkey="SuperSecretKey";  
            char[] code = secretkey.toCharArray();
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null,null);

            X509Certificate[] chain = new X509Certificate[3];
            chain[0] = aliceCert;
            chain[1] = interCert;
            chain[2] = rootCert;
            ks.setKeyEntry("AlicePrivateKeyAlias",aliceKeyPair.getPrivate(),code,chain);
            fos = new FileOutputStream(new File("alicePriv.key"));
            ks.store(fos,code);
            fos.close();

            // 4.2

            System.out.println("* 4.2  ");
            fis = new FileInputStream(new File("alicePriv.key"));
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(fis,code);
            fis.close();
            PrivateKey alicePrivateKey = (PrivateKey)ks.getKey("AlicePrivateKeyAlias",code);
            System.out.println(" - Alice alicePrivateKey");
            System.out.println(alicePrivateKey);
            System.out.println();

            // 5. RSA
            System.out.println("* 5. RSA ");

            System.out.println(" 5.1 Bob Alice");
            String plaintext = "CERT TEST!";
            System.out.println("  "+plaintext);
            byte[] t0 = plaintext.getBytes();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());
            byte[] b0 = cipher.doFinal(t0);
            System.out.println(" : " + ByteUtils.toHexString(b0));

            // 5.2 RSA
            System.out.println(" 5.2 AliceRSA ");
            cipher.init(Cipher.DECRYPT_MODE, alicePrivateKey);
            byte[] b1 = cipher.doFinal(b0);
            System.out.println("  : "+ new String(b1));
            System.out.println();

            // 6. RSA 
            System.out.println("* 6. RSA (Alice Bob)");

            // 6.1 RSA
            System.out.println(" 6.1 RSA )");
            String sigData=" ";
            byte[] data = sigData.getBytes("UTF8");
            System.out.println(" Plaintext : "+sigData);
            Signature sig = Signature.getInstance("MD5WithRSA");
            fis = new FileInputStream(new File("alicePriv.key"));
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(fis,code);
            fis.close();
            PrivateKey alicePrivateKey1 = (PrivateKey)ks.getKey("AlicePrivateKeyAlias",code); 
            sig.initSign(alicePrivateKey1);
            sig.update(data);
            byte[] signatureBytes = sig.sign();
            System.out.println("  : " + ByteUtils.toHexString(signatureBytes));

            // 6.2 RSA
            System.out.println(" 6.2 RSA ");
            CertificateFactory cf1 = CertificateFactory.getInstance("X.509");
            FileInputStream fis1 = new FileInputStream(new File("aliceCert.der"));
            X509Certificate cert1 = (X509Certificate)cf1.generateCertificate(fis1);
            fis1.close();
            sig.initVerify(cert1.getPublicKey());
            sig.update(data);
            System.out.println(" Verification: "+sig.verify(signatureBytes));
            System.out.println();

            // 7.
            System.out.println("* 7.    ");
            X509CRLEntry entry = interCRL.getRevokedCertificate(bobCert.getSerialNumber());
            if(entry!=null){
                System.out.printf(": %d%n", entry.getSerialNumber());
                if(entry.getCertificateIssuer()==null)
                    System.out.printf(": %s%n", interCRL.getIssuerX500Principal());
                else System.out.printf(" : %s%n", entry.getCertificateIssuer());
            }
            System.out.println();

            // 8. CRL
            System.out.println("* 8. CRL   ");
            // 8.1 CRL
            System.out.println(" 8.1 CRL   ");
            fos = new FileOutputStream(new File("inter.crl"));
            fos.write(interCRL.getEncoded());
            fos.close();

            // 8.2 CRL
            System.out.println(" 8.2 CRL    ");
            cf = CertificateFactory.getInstance("X.509");
            fis = new FileInputStream(new File("inter.crl"));
            X509CRL newcrl = (X509CRL)cf.generateCRL(fis);
            fis.close();
            entry = newcrl.getRevokedCertificate(bobCert.getSerialNumber());
            System.out.println("* CRL :  ");
            if(entry!=null){
                System.out.printf(": %d%n", entry.getSerialNumber());
                if(entry.getCertificateIssuer()==null)
                    System.out.printf(": %s%n", newcrl.getIssuerX500Principal());
                else System.out.printf(": %s%n", entry.getCertificateIssuer());
            }
            System.out.println();

            List<X509Extension> list = new ArrayList<X509Extension>();
            list.add(rootCert);
            list.add(interCert);
            list.add(aliceCert);
            list.add(bobCert);
            list.add(rootCRL);
            list.add(interCRL);
            //System.out.println(list);

            CollectionCertStoreParameters params = new CollectionCertStoreParameters(list);
            CertStore store = CertStore.getInstance("Collection",params);
            System.out.println();


            cf = CertificateFactory.getInstance("X.509");
            List<Certificate> certChain = new ArrayList<Certificate>();
            certChain.add(bobCert);
            certChain.add(interCert);
            CertPath certPath = cf.generateCertPath(certChain);
            Set<TrustAnchor> trust = Collections.singleton(new TrustAnchor(rootCert,null));
            CertPathValidator validator = CertPathValidator.getInstance("PKIX","BC");
            PKIXParameters param = new PKIXParameters(trust);
            param.addCertStore(store);
            param.setDate(new Date());
            try{
                PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult)validator.validate(certPath,param);
                System.out.println(result);
            }
            catch(CertPathValidatorException e){

                //System.out.println(e.getCertPath());

                System.out.println("validation failed "+e.getIndex()+" detail: "+e.getMessage());
            }
        }
        catch(NoSuchAlgorithmException nsae){
            nsae.printStackTrace();
        }
        catch(CertificateException ce){
            ce.printStackTrace();
        }
        catch(InvalidKeyException ike){
            ike.printStackTrace();
        }
        catch(InvalidAlgorithmParameterException iape){
            iape.printStackTrace();
        }
        catch(SignatureException se){
            se.printStackTrace();
        }
        catch(NoSuchProviderException nspre){
            nspre.printStackTrace();
        }
        catch(KeyStoreException kse){
            kse.printStackTrace();
        }
        catch(UnrecoverableKeyException uke){
            uke.printStackTrace();
        }
        catch(CRLException nsae){
            nsae.printStackTrace();
        }
    }
}
