package com.quantum.matcher;

/**
 * Matching Unit
 * 
 */
public class App {

	public static void main(String[] args) {

	}

	public static void usage() {
		System.out.println("Usage:");
		System.out.println("-h to get this help");
		System.out
				.println("-ref <filename_path> to define a filename image as reference");
		System.out
				.println("-cand <filename_path> to define a filename image as candidate");
		System.out
				.println("-alg <DFT|FFT> to define algorithm to use; by default FFT");

	}
}
