package com.wf.ew.common.utils;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import gnu.io.SerialPort;

/**
 * java串口通讯
 * Created by DELL on 2017/4/24.
 */
public class Test4 implements SerialPortEventListener {

    private static final String DEMONAME = "串口测试";

    /**
     * 检测系统中可用的端口
     */
    private CommPortIdentifier portId;
    /**
     * 枚举类
     */
    private Enumeration portList;
    /**
     * 输入流
     */
    private static InputStream inputStream;
    /**
     * RS-232的串行口
     */
    private SerialPort serialPort;
    /**
     * 串口返回信息
     */
    private static String test = "";

    /**
     * 初始化
     *
     * @param baudRate 波特率
     */
    public void init(int baudRate) {
        //获取系统中可用的端口
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL && portId.getName().equals("COM3")) {
                System.out.println("发现端口："+portId.getName());
                try {
                    serialPort = (SerialPort) portId.open(DEMONAME,2000);
                    //设置串口监听
                    serialPort.addEventListener(new Test4());
                    //设置开启监听
                    serialPort.notifyOnDataAvailable(true);
                    //设置波特率、数据位、停止位、检验位
                    serialPort.setSerialPortParams(baudRate,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    //获取输入流
                    inputStream = serialPort.getInputStream();
                } catch (PortInUseException e) {
                    e.printStackTrace();
                } catch (TooManyListenersException e) {
                    e.printStackTrace();
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 监听函数
     * @param serialPortEvent
     */
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            //获取到有效信息
            case SerialPortEvent.DATA_AVAILABLE :
                readComm();
                break;
            default:
                break;
        }
    }

    /**
     * 读取串口信息
     */
    private void readComm() {
        byte[] readBuffer = new byte[15];
        try {
            int len = 0;
            while ((len = inputStream.read(readBuffer)) != -1) {
                test += new String(readBuffer,0, len).trim();
                break;
            }
            for (byte b : readBuffer) {
                if (b < 0) {
                    //byte的范围是-128到+127
                    int i = 128 + (int) b + 127 + 1;
                    //转换成16进制
                    System.out.println("读取的信息："+Integer.toHexString(i));
                }else {
                    System.out.println("读取的信息："+Integer.toHexString(b));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void close() {
        serialPort.close();
    }

     public static void main(String[] args) {
        new Test4().init(9600);
    }
}
