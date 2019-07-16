package com.wf.ew.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


public class RunUtil implements Runnable {
	
	Process process = null ;

	public RunUtil(Process process) {
		this.process = process;
	}

	@Override
	public void run() {
		
		try {
			process.waitFor();
		} catch (InterruptedException e1) {
			
			e1.printStackTrace();
		}
		InputStream in = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = null ;
		System.out.println("多线程开始-----------------");
		try {
			while ((line = br.readLine()) != null) {
				System.out.println("line-------"+line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				br.close();
				in.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static void main(String[] args) throws Exception {
		
		
	}
	

}
