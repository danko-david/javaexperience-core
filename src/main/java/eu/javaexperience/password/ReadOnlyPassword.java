package eu.javaexperience.password;

import eu.javaexperience.exceptions.UnsupportedMethodException;

public abstract class ReadOnlyPassword implements PasswordModel
{
	@Override
	public void setPassword(String nonce, String password)
	{
		throw new UnsupportedMethodException("Changing password not supported");
	}
}
