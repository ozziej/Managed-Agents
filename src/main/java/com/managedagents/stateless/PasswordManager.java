/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.managedagents.stateless;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.ejb.Stateless;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author james
 */
@Stateless
public class PasswordManager
{
    private String password;
    private String encryptedPassword;
    private String salt;

    public void createRandomPassword()
    {
	password = RandomStringUtils.randomAlphanumeric(8);
    }

    public void encryptPassword() throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
	byte[] bSalt = new byte[8];

	SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	random.nextBytes(bSalt);
	digestPassword(bSalt);
    }

    public void verifyPassword() throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
	byte[] bSalt = Base64.getDecoder().decode(salt);
	digestPassword(bSalt);
    }

    private void digestPassword(byte[] bSalt) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
	MessageDigest digest = MessageDigest.getInstance("SHA-1");
	digest.reset();
	digest.update(bSalt);

	byte[] input = digest.digest(password.getBytes("UTF-8"));

	for (int i = 0; i < 5; i++)         //this is just to add some more randomness to the password
	{
	    digest.reset();
	    input = digest.digest(input);
	}

	encryptedPassword = new String(Base64.getEncoder().encode(input));
	salt = new String(Base64.getEncoder().encode(bSalt));
    }

    public String getPassword()
    {
	return password;
    }

    public void setPassword(String password)
    {
	this.password = password;
    }

    public String getSalt()
    {
	return salt;
    }

    public void setSalt(String salt)
    {
	this.salt = salt;
    }

    public String getEncryptedPassword()
    {
	return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword)
    {
	this.encryptedPassword = encryptedPassword;
    }
}
