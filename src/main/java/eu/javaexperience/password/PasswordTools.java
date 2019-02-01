package eu.javaexperience.password;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import eu.javaexperience.text.Format;

public class PasswordTools
{
	public static final PasswordModel ALWAYS_PASS = new ReadOnlyPassword()
	{
		@Override
		public boolean authenticate(String nonce, String password)
		{
			return true;
		}
	};
	
	public static final PasswordModel ALWAYS_DENY = new ReadOnlyPassword()
	{
		@Override
		public boolean authenticate(String nonce, String password)
		{
			return false;
		}
	};
	
	public static PasswordModel createPlainFixedPassword(final String passwd)
	{
		return new ReadOnlyPassword()
		{
			@Override
			public boolean authenticate(String nonce, String password)
			{
				return passwd.equals(password);
			}
		};
	}
	
	public static String generateSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}
	
	public static String generateSaltedHashPBKDF2(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = generateSalt().getBytes();
		
		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + Format.toHex(salt) + ":" + Format.toHex(hash);
	}
		
	public static boolean validateSaltedPasswordPBKDF2(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String[] parts = storedPassword.split(":");
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = Format.fromHex(parts[1]);
		byte[] hash = Format.fromHex(parts[2]);
		
		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] testHash = skf.generateSecret(spec).getEncoded();
		
		int diff = hash.length ^ testHash.length;
		for(int i = 0; i < hash.length && i < testHash.length; i++)
		{
			diff |= hash[i] ^ testHash[i];
		}
		return diff == 0;
	}

}
