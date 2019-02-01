package eu.javaexperience.password;

public interface PasswordModel
{
	public void setPassword(String nonce, String password);
	public boolean authenticate(String nonce, String password);
}
