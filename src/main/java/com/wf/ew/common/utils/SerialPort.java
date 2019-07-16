package com.wf.ew.common.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.sound.sampled.Line;

public class SerialPort {

	/**
	 * 当执行main方法中的代码且未执行关闭串口时，程序将一直处于开启状态，自动监听，接收读取来自串口的数据。
	 *	注意：一个串口只能打开一次，并只支持一个程序调用。
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// 实例化串口操作类对象
        //SerialPortUtils serialPort = new SerialPortUtils();
        SerialPortUtils serialPort = SerialPortUtils.getInstance();
        // 创建串口必要参数接收类并赋值，赋值串口号，波特率，校验位，数据位，停止位
        ParamConfig paramConfig = new ParamConfig("COM4", 9600, 0, 8, 1);
        //ParamConfig paramConfig = new ParamConfig("6[6]", 9600, 0, 8, 1);
        // 初始化设置,打开串口，开始监听读取串口数据
        serialPort.init(paramConfig);
        // 调用串口操作类的sendComm方法发送数据到串口
        //serialPort.sendComm("FEF10A000000000000000AFF");
       
        //serialPort.sendComm("hhhhhh");
        //读取串口数据
        /*gnu.io.SerialPort sp = serialPort.getSerialPort();
        OutputStream outputStream = sp.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
        bw.write("@0600\\CR");
        //PrintWriter pw = new PrintWriter(outputStream);
        //pw.write("stopvideo");
        
        outputStream.flush();
        outputStream.close();
        bw.close();*/
        //pw.close();
       
        //InputStream inputStream = sp.getInputStream();
        //serialPort.sendComm(serialPort.bytesToHexString("@0600".getBytes())+"0D");//设置关闭所有通道
        //serialPort.sendComm(serialPort.bytesToHexString("@0600".getBytes())+"0D");//设置关闭所有通道
        
        serialPort.sendComm(serialPort.bytesToHexString("@1507".getBytes())+"0D");//设置关闭所有通道
        //serialPort.sendComm(serialPort.bytesToHexString("$156".getBytes())+"0D");//设置关闭所有通道
        //serialPort.sendComm(serialPort.bytesToHexString("@15".getBytes())+"0D");//设置关闭所有通道
        int parseInt = Integer.parseInt("01");
        //System.out.println(Math.pow(2, 3));
        //2kai,!020D00
        //1kai,!010E00
        //13kai,!050A00
        //123kai,!070800
        //3kai,!040B00
        //4kai,!080700
        //34kai,!0C0300
        //guanbi,!000F00
        
        //serialPort.sendComm(serialPort.bytesToHexString("@061F".getBytes())+"0D");//设置所有通道输出
        //16 关闭1、4  , 01  1open ,02  2open, 03  12open ,04  3open ,05  13open ,06 23open
        //07 123open ,08 4open ,09 14open ,0A 24open ,0B 124open , 0C 34open ,0D 134open
        //0E 234open , 0F 1234open , 10 5open ,11 15open ,12 25open , 13 125open , 14 35open
        //15 135open ,16 235open , 17 1235open ,18 45open , 19 145open
        //17 关闭4 ,  18  :123close  , 19  :23close ,  1A  :13close ,  1B   :3close , 1C   :12close
        //1D   :2close  , 1E    :1close  , 1F  all open
       //@061F\CR
        //@0600\CR //设置关闭所有通道
        //@061F\CR  //设置所有通道输出
        
        //serialPort.readComm();
        // 关闭串口
        //serialPort.closeSerialPort();
        Thread.sleep(2000);
        serialPort.readComm();
        serialPort.closeSerialPort();
	}
	
}
