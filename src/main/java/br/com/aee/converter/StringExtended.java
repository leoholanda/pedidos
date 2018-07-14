package br.com.aee.converter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;

public class StringExtended {

	public static String toMD5(String string) {
		String hashMD5;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(string.getBytes()));
			hashMD5 = hash.toString(16);
			return hashMD5;
		} catch (NoSuchAlgorithmException | NullPointerException e) {
			// e.printStackTrace();
			return null;
		}

	}

	public static String toASCII(String string) {
		if (string != null) {
			string = Normalizer.normalize(string, Normalizer.Form.NFD);
			string = string.replaceAll("[^\\p{ASCII}]", "");
		}
		return string;
	}
	
	public static String getNomeComIniciaisMaiuscula(String nome) {

		String sNova = "";
		for (String sNome : nome.toLowerCase().split(" ")) {
			if (!"".equals(sNome)) {
				if (!"".equals(sNova))
					sNova += " ";
				if (sNome.length() > 2) {
					sNova += sNome.substring(0, 1).toUpperCase() + sNome.substring(1);
				} else {
					sNova += sNome;
				}
			}
		}
		return sNova;
	}
}
