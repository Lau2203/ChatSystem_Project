package chatsystem.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Arrays;
import java.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;


public class EncryptionHandler {

	private static final String WITNESS_STRING = "THAT IS COOL";

	private String witnessFilePath;
	private final String ENCRYPTION_ALGORITHM = "AES/ECB/PKCS5PADDING";

	private Cipher cipher;

	public EncryptionHandler(String witnessFilePath) {
		try {
			this.cipher = Cipher.getInstance(this.ENCRYPTION_ALGORITHM);
		} catch (Exception e) {}

		this.witnessFilePath = witnessFilePath;
	}

	public String getFingerprint(String passwd) {

		byte[] key = null;
		MessageDigest sha = null;

		try {
			key = passwd.getBytes("UTF-8");	

			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
		} catch (Exception e) { e.printStackTrace(); }

		return Base64.getEncoder().encodeToString(key);
	}

	private byte[] getKey(String passwd) {
	
		byte[] key = null;
		MessageDigest sha = null;

		try {
			key = passwd.getBytes("UTF-8");	

			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			
		} catch (Exception e) { e.printStackTrace(); }

		return key;
	}

	public String encrypt(String key, String toBeEncrypted) {

		byte[] encrypted = null;

		SecretKeySpec sks = new SecretKeySpec(this.getKey(key), "AES");

		try {
			this.cipher.init(Cipher.ENCRYPT_MODE, sks);
			encrypted = this.cipher.doFinal(toBeEncrypted.getBytes("UTF-8"));
		} catch (Exception e) {System.out.println("ERROR: " + e);}

		return Base64.getEncoder().encodeToString(encrypted);
	}	

	public String decrypt(String key, String toBeDecrypted) throws BadPaddingException {

		byte[] decrypted = null;

		SecretKeySpec sks = new SecretKeySpec(this.getKey(key), "AES");

		try {
			this.cipher.init(Cipher.DECRYPT_MODE, sks);
			decrypted = this.cipher.doFinal(Base64.getDecoder().decode(toBeDecrypted));
		} 
		catch (BadPaddingException bpe) { throw bpe; }
		catch (Exception e) { e.printStackTrace(); }


		return new String(decrypted);
	}

	public boolean testWitnessFile(String key) {

		BufferedReader in;
		String line;

		try {
			in = new BufferedReader(new FileReader(this.witnessFilePath));
			line = in.readLine();

		} catch (IOException ioe) { System.out.println(ioe); return false;}

		try {
			if (this.decrypt(key, line).equals(EncryptionHandler.WITNESS_STRING))
				return true;
		} catch (BadPaddingException bpe) { return false; }

		return false;
	}
}

