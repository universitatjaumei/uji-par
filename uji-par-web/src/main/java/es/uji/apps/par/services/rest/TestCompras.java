package es.uji.apps.par.services.rest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestCompras {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA1");
		String iden = "123";
		String importe = "100";
		String mail = "test@4tic.com";
		String url = "http://www.google.com";
		String clave = "ertyudfghjcvbnm";
		//sha1 = 72277f0c5e514963225898c30879e7b407c15ae8
		
		String str = iden + importe + mail + url + clave;
		byte[] arr = md.digest(str.getBytes());
		
		StringBuffer sb = new StringBuffer("");
	    for (int i = 0; i < arr.length; i++) {
	    	sb.append(Integer.toString((arr[i] & 0xff) + 0x100, 16).substring(1));
	    }
		
		System.out.println(sb.toString());
	}

}
