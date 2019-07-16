package com.wf.ew.screen.utils;




import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Calendar;

import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.area.ClockBxArea;
import onbon.bx06.area.CounterBxArea;
import onbon.bx06.area.DateStyle;
import onbon.bx06.area.DateTimeBxArea;
import onbon.bx06.area.DynamicBxArea;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.TimeStyle;
import onbon.bx06.area.TimerBxArea;
import onbon.bx06.area.WeekStyle;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.area.unit.CounterBxUnit.CounterBxUnitFormat;
import onbon.bx06.area.unit.CounterBxUnit.CounterBxUnitMode;
import onbon.bx06.area.unit.TimerBxUnit.TimerBxUnitFormat;
import onbon.bx06.area.unit.TimerBxUnit.TimerBxUnitMode;
import onbon.bx06.cmd.dyn.DynamicBxAreaRule;
import onbon.bx06.file.BxFileWriterListener;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.series.Bx6M;
import onbon.bx06.series.Bx6Q;
import onbon.bx06.utils.DisplayStyleFactory;
import onbon.bx06.utils.DisplayStyleFactory.DisplayStyle;
import onbon.bx06.utils.TextBinary.Alignment;

public class ScreenPortUtil implements BxFileWriterListener<Bx6GScreen> {
	
	
	private Bx6GScreenClient screen;
	
	/**
	 * 设置单例对象
	 */
	private static  class SingletonClassInstance{
    	private static final ScreenPortUtil instance = new ScreenPortUtil();
    }
    
    private ScreenPortUtil() {}
    
    public static ScreenPortUtil getInstance() {
    	return SingletonClassInstance.instance;
    }
	
	/**
	 * 初始化
	 * @throws Exception
	 */
	public void init(String name) throws Exception {
		// SDK初始化
		Bx6GEnv.initial("log.properties", 30000);
		// Screen 类 与控制器的所有交互都需要通过
		//screen = new Bx6GScreenClient(name,new Bx6M());
		screen = new Bx6GScreenClient(name,new Bx6Q());
	}
	
	/**
	 * 得到屏幕对象
	 * @param ip
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public Bx6GScreenClient getScreen(String ip,int port) throws Exception {
		 if (!screen.connect(ip, port)) {
	           System.out.println("connect failed");
	           return null;
	     }
		 return screen ;
	}
	
	/**
	 * 创建图文区数据页
	 * 文本数据页
	 * @param content
	 * @return
	 */
	public TextBxPage getTextPage(String content,Color color) {
		TextBxPage page = new TextBxPage(content);
		page.setHorizontalAlignment(Alignment.NEAR);
		//page.setVerticalAlignment(Alignment.CENTER);
		page.setFont(new Font(" 宋体 ", Font.PLAIN, 15)); 
		//page.setBackground(Color.darkGray);
		page.setForeground(color);
		//page.setDisplayStyle(DisplayStyleFactory.getStyle(0));
		//page.setSpeed(1);
		//page.setStayTime(100);
        //page.setHorizontalAlignment(Alignment.FAR);
		
        return page;
	}
	
	
	
	
	/**
	 * 动态区域
	 * @param screen
	 * @return
	 */
	public DynamicBxAreaRule getDynamicAreaRule(Bx6GScreenClient screen,int no) {
		//DynamicBxAreaRule dynRule = new DynamicBxAreaRule(0,(byte)0,(byte)0,0);
		DynamicBxAreaRule dynRule = new DynamicBxAreaRule();
		dynRule.setId(no);
		dynRule.setRunMode((byte) 0);
		dynRule.setImmediatePlay((byte) 1);
		return dynRule;
	}
	
	public DynamicBxArea getDynamicArea(Bx6GScreenClient screen,int height) {
		DynamicBxArea dynamicBxArea = new DynamicBxArea(0, height, 128, 16, screen.getProfile());
		return dynamicBxArea;
	}
	
	/**
	 * 创建图文区
	 * @param screen
	 * @return
	 * @throws Exception
	 */
	public TextCaptionBxArea getTWQArea(Bx6GScreenClient screen) throws Exception {
		TextCaptionBxArea area = new TextCaptionBxArea(0, 0, 128, 16, screen.getProfile());
        area.setFrameShow(false);
        //area.setFrameSpeed(20);
        //area.loadFrameImage(13);
        return area;
	}
	
	/**
	 * 创建节目对象
	 * @param screen
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public ProgramBxFile getProgram(Bx6GScreenClient screen,String name) throws Exception {
		Bx6GScreenProfile profile = new Bx6GScreenProfile(128, 96, new Bx6Q());
		ProgramBxFile program = new ProgramBxFile(name, profile);//P000
        program.setFrameShow(false);
        program.setFrameSpeed(20);
        //program.loadFrameImage(13);
        return program;
	}
	
	/**
	 * 关闭屏幕连接
	 */
	public void closeScreen() {
		if (screen != null) {
			screen.disconnect();
		}
	}

	@Override
	public void cancel(Bx6GScreen owner, String fileName, Bx6GException ex) {
		ex.printStackTrace();
	}

	@Override
	public void done(Bx6GScreen owner) {
		
	}

	@Override
	public void fileFinish(Bx6GScreen owner, String fileName, int total) {
		System.out.println(fileName + " fileFinish:" + total);
	}

	@Override
	public void fileWriting(Bx6GScreen owner, String fileName, int total) {
		System.out.println(fileName + " fileWriting:" + total);
	}

	@Override
	public void progressChanged(Bx6GScreen owner, String fileName, int write, int total) {
		System.out.println(fileName + " progressChanged:" + write + "/" + total);
	}

}
