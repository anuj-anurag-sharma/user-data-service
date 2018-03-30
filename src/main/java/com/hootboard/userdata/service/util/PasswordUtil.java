package com.hootboard.userdata.service.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

	public static String hashPassword(String password) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] ePass = digest.digest(password.getBytes());
		return Base64.getEncoder().encodeToString(ePass);

	}

}
