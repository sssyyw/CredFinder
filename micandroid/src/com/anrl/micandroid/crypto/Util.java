package com.anrl.micandroid.crypto;
//Author Wesley Ellis; wesleyjellis@gmail.com

public class Util {
	public static byte[] makeBytesfromString(String input) {
		byte[] output = new byte[16];
		String[] inputs = input.split("/");
		for (int i = 0; i < inputs.length; i++) {
			output[i] = (byte) Integer.parseInt(inputs[i]);
		}
		return output;
	}
	public static String makeStringfromBytes(byte[] input){
		String output = "";
		for (int i = 0; i < (input.length - 1); i++) {
			output = output + input[i] + "/";
		}
		//Because we've added one too many slashes
		output = output + input[input.length - 1];
		return output;
	}

}
