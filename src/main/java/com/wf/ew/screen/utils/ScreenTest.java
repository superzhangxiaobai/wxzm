package com.wf.ew.screen.utils;


import java.awt.Color;

import onbon.bx06.Bx6GScreen.Result;
import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.area.DynamicBxArea;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.cmd.dyn.DynamicBxAreaRule;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.message.global.ACK;

public class ScreenTest {

	
	public static void main(String[] args) throws Exception {
		//获取单例对象
		ScreenPortUtil instance = ScreenPortUtil.getInstance();
		//初始化
		instance.init("MyScreen");
		Bx6GScreenClient screen = instance.getScreen("192.168.101.86", 5005);
		//得到节目对象
		//ProgramBxFile program = instance.getProgram(screen, "P000");
		//图文区域
		//TextCaptionBxArea twqArea = instance.getTWQArea(screen);
		//图文数据页面
		TextBxPage textPage = instance.getTextPage("车号：皖C83199",Color.RED);
		TextBxPage toPage = instance.getTextPage("目的地：宋程机目的的吗吗吗",Color.GREEN);
		TextBxPage diaojiPage = instance.getTextPage("吊机号：1号吊机",Color.BLUE);
		TextBxPage shipPage = instance.getTextPage("船号：明光222",Color.YELLOW);
		TextBxPage weightPage = instance.getTextPage("净重：63.48吨",Color.RED);
		//twqArea.addPage(textPage);
		
		DynamicBxAreaRule dynRule = instance.getDynamicAreaRule(screen,0);
		DynamicBxAreaRule dynRule1 = instance.getDynamicAreaRule(screen,1);
		DynamicBxAreaRule dynRule2 = instance.getDynamicAreaRule(screen,2);
		DynamicBxAreaRule dynRule3 = instance.getDynamicAreaRule(screen,3);
		DynamicBxAreaRule dynRule4 = instance.getDynamicAreaRule(screen,4);
		
		DynamicBxArea dynamicArea = instance.getDynamicArea(screen,2);
		DynamicBxArea dynamicArea1 = instance.getDynamicArea(screen,19);
		DynamicBxArea dynamicArea2 = instance.getDynamicArea(screen,36);
		DynamicBxArea dynamicArea3 = instance.getDynamicArea(screen,53);
		DynamicBxArea dynamicArea4 = instance.getDynamicArea(screen,70);
		dynamicArea.addPage(textPage);
		//dynamicArea.addPage(textPageValue);
		dynamicArea1.addPage(toPage);
		dynamicArea2.addPage(diaojiPage);
		dynamicArea3.addPage(shipPage);
		dynamicArea4.addPage(weightPage);
		//screen.deleteAllDynamic();
		// 更新动态区
		screen.writeDynamic(dynRule,dynamicArea);
		screen.writeDynamic(dynRule1,dynamicArea1);
		screen.writeDynamic(dynRule2,dynamicArea2);
		screen.writeDynamic(dynRule3,dynamicArea3);
		screen.writeDynamic(dynRule4,dynamicArea4);
		
		//screen.writeDynamic(dynRule,dynamicArea2);
		//screen.deleteProgram("P000");
		//Result<ACK> deletePrograms = screen.deletePrograms();
		//program.addArea(twqArea);
		//screen.writeProgramAsync(program, instance);
		Thread.sleep(1000);
		//screen.ping();
		//关闭连接
		/**
		 * 红色：绿色
		 * 车号：皖C83199
		 * 目的地：宋程机场
		 * 吊机号：称程续
		 * 船号：明光222
		 * 红色：紫色
		 * 净重：63.48吨
		 * 
		 * 
		 * 
		 */
		instance.closeScreen();
	}
	
}
