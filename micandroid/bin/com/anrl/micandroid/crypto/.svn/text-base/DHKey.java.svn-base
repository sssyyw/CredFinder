package com.anrl.micandroid.crypto;
//Author Wesley Ellis; wesleyjellis@gmail.com

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;


import android.util.Log;

public class DHKey {

	private static final String TAG = "micandroid";

	private byte[] keyarray = new byte[16];
	
	public String initDH(String A) {
		//This method is passed the passes the server's public Diffie-Hellmen key
		// Assume A is encoded as a base64 string.
		//Assume once  A is decoded it an X509Encoded key spec
		//All of this is based off of 
		//http://download.oracle.com/javase/1.5.0/docs/guide/security/jce/JCERefGuide.html#DH2Ex
		String B = "failed";
		try {
			byte[] ServerKey = Base64.decode(A);
			X509EncodedKeySpec RawKey = new X509EncodedKeySpec(ServerKey);
			//Intantiate a keyfactory
			KeyFactory keyFact = KeyFactory.getInstance("DH");
			//Create a public key from RawKey
			PublicKey ServerPK = keyFact.generatePublic(RawKey);
			//Acquire the paramters used by the server from it's public key
			DHParameterSpec dhSkipParamSpec = ((DHPublicKey) ServerPK)
					.getParams();
			//Create a keygen object
			KeyPairGenerator kpGen = KeyPairGenerator.getInstance("DH");
			//initialize the key generator with the server's parameters
			kpGen.initialize(dhSkipParamSpec);
			//Generate our DH key pair
			KeyPair pair = kpGen.generateKeyPair();

			PublicKey pubkey = pair.getPublic();
			PrivateKey privkey = pair.getPrivate();

			byte[] tmpArray = pubkey.getEncoded();
			B = Base64.encodeBytes(tmpArray);
			
			//Here is where the shared key is actually generated
			KeyAgreement KA = KeyAgreement.getInstance("DH");
			KA.init(privkey);
			KA.doPhase(ServerPK, true);
			
			//The key that is generated is too long, so hash it in order to get the right length
			byte[] key = KA.generateSecret();
			keyarray = MD5hash.hash(key);
			
		} catch (Exception e) {
			Log.d(TAG, "Exception occured in DHKey " + e.toString());
		}
		return B;
	}

	public String encrypt(String message) {
		//This method is used to encrypt messages to the server
		byte[] toSend = AES.encryptByte(message.getBytes(), keyarray);
		return Base64.encodeBytes(toSend);
	}

	public String decrypt(String message) {
		byte[] temp;
		try {
			temp = Base64.decode(message);
			return new String(AES.decryptByte(temp, keyarray));
		} catch (IOException e) {
			Log.d(TAG, "IOException whilst decoding base64 string");
		}
		return "Failed";
	}

}
