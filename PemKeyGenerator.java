import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class PemKeyGenerator {

    public static void main(String[] args) throws Exception {
        // Generate RSA key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Save private key
        writePemFile(
                "PRIVATE KEY",
                keyPair.getPrivate().getEncoded(),
                "private.pem"
        );

        // Save public key
        writePemFile(
                "PUBLIC KEY",
                keyPair.getPublic().getEncoded(),
                "public.pem"
        );

        System.out.println("PEM files generated successfully.");
    }

    private static void writePemFile(String type, byte[] bytes, String fileName)
            throws Exception {

        String encoded = Base64.getMimeEncoder(64, new byte[]{'\n'})
                .encodeToString(bytes);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("-----BEGIN " + type + "-----\n");
            writer.write(encoded);
            writer.write("\n-----END " + type + "-----\n");
        }
    }
}