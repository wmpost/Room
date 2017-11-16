package DB;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

class PwdMgr {

    private SecureRandom secRand;

    public PwdMgr(){
        try {
            secRand = SecureRandom.getInstanceStrong();
        }
        catch(NoSuchAlgorithmException e) {
            System.err.println(e.getStackTrace());
        }

    }
    private byte[] makeSalt(){
        byte[] salt = new byte[32];
        secRand.nextBytes(salt);
        //System.out.println("Salt length: " + salt.length);
        return salt;
    }

    public byte [] []generateValues(String password){
        byte [][] ret = new byte [2][];
        byte [] pwdhash;

        byte [] salt = makeSalt();
        ret[0] = salt;

        pwdhash = hashPassword(password.toCharArray(), salt);
        ret[1] = pwdhash;
        return ret;
    }

    public boolean validatePassword( final String password, final byte[] hash, final byte[] salt){
        byte [] inputHash;
        inputHash = hashPassword(password.toCharArray(), salt);
        return Arrays.equals(hash, inputHash);
    }
    private  static byte[] hashPassword( final char[] password, final byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec( password, salt, 4000, 256 );
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded();
            return res;
        } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
        finally {
            spec.clearPassword();
        }
    }

}
