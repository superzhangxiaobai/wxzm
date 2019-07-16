package com.wf.ew.screen.utils;

import java.awt.Color;
import java.awt.Font;


import onbon.bx06.Bx6GEnv;
import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreen.Result;
import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.Bx6GScreenRS;
import onbon.bx06.Bx6GScreenRS.BaudRate;
import onbon.bx06.area.DateStyle;
import onbon.bx06.area.DateTimeBxArea;
import onbon.bx06.area.DynamicBxArea;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.TimeStyle;
import onbon.bx06.area.page.ImageFileBxPage;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.area.page.TextFileBxPage;
import onbon.bx06.cmd.dyn.DynamicBxAreaRule;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.message.area.TextCaptionArea;
import onbon.bx06.message.common.ErrorType;
import onbon.bx06.message.global.ACK;
import onbon.bx06.message.led.ReturnControllerStatus;
import onbon.bx06.series.Bx6Card;
import onbon.bx06.series.Bx6M;
import onbon.bx06.utils.DisplayStyleFactory;
import onbon.bx06.utils.DisplayStyleFactory.DisplayStyle;
import onbon.bx06.utils.TextBinary.Alignment;

public class ScreenUtil {

	public static void main(String[] args) throws Exception {
		// SDK 初始化 ,六代控制器
		Bx6GEnv.initial("log.properties", 30000);
		
		//  六代控制器创建 screen 对象方法，以 BX-6M 系列为例,串口方式通信
		Bx6GScreenRS screen = new Bx6GScreenRS("MyScreen", new Bx6M());
		
		//  屏幕连接
		//  连接控制器
		//  其中， 192.168.88.199 为控制器的实际 IP 地址，请根据实际情况填写。
		//  如果你不知道控制器的 IP 地址是多少，请先使用 LedshowTW 软件对控制器进行 IP 设置
		//  端口号默认为 5005
		if (!screen.connect("COM3",BaudRate.RATE_9600))
		{
			System.out.print("connected failed!");
			return;
		}
		// 可以通过以下接口，回读控制器状态
		Bx6GScreen.Result<ReturnControllerStatus> result = screen.checkControllerStatus();
		if (result.isOK()) {
			ReturnControllerStatus status = result.reply;
			status.getBrightness();
			status.getRtcDay();
			status.getScreenOnOff();
			//
			// status 还有很对其他接口，可以根据实际需求再次调用以获取相应状态
		} else {
			ErrorType error = result.getError();
		}
		
		//
		//  下面开始创建第一个节目， P000
		//  此节目包括只包括一个图文区 ,  图文区中包括两个数据页：一页文本，一页图片
		//  显示节目边框
		//  区域显示边框
		//  创建节目文件
		Bx6GScreenProfile profile = new Bx6GScreenProfile(200, 100, new Bx6M());
		ProgramBxFile p0 = new ProgramBxFile("P000", profile);
		//  是否显示节目边框
		p0.setFrameShow(true);
		//  节目边框的移动速度
		p0.setFrameSpeed(20);
		//  使用第几个内置边框
		p0.loadFrameImage(13);
		
		//  断开与控制器之间的链接
		screen.disconnect();
		//end
	}
	
	public static void createDynamicArea(Bx6GScreenRS screen,ProgramBxFile p0) {
		//
		// 定义一个动态区
		// 可以通过 ID 来更新不同的动态区内容，此处 ID 为 0
		//DynamicBxAreaRule dynRule = new DynamicBxAreaRule(0,(byte)0,(byte)0,0);
		DynamicBxAreaRule dynRule = new DynamicBxAreaRule();
		dynRule.setId(0);
		/**
		 * RunMode –  表示动态区的运行模式，其具体定义如下：
			0x00 ：多个数据页循环显示
			0x01 ：显示完成后静止显示最后一页数据
			0x02 ：循环显示，超过设定时间后数据仍未更新时不再显示
			0x03 ：循环显示，超过设定时间后数据仍未更新时显示 Logo 信息
			0x04 ：循环显示，显示完最后一页后就不再显示
		 */
		dynRule.setRunMode((byte)0);
		//dynRule.setTimeout(3);
		/**
		 * immediatePlay –  表示类容更新后动态区播放方式，其具体定义如下：
			0x00 ：与异步节目一起播放，即可以作为某一个或多个节目的一个区域进行显示
			0x01 ：异步节目停止播放，仅播放动态区，可以理解成此时，动态区是最为单独的一个节目来播放。
			且更新后会立即播放。
			文档内部编号
			上海仰邦科技股份有限公司 |  Java API User Manual
			- 13 -
			0x02 ：当播放完节目编号最高的异步节目后播放该动态区。此时动态区也是作为单独节目播放，但
			会等普通节目播放完成再播放。
		 */
		dynRule.setImmediatePlay((byte) 0);
		TextCaptionBxArea dArea = new TextCaptionBxArea(0, 0, 200, 100, screen.getProfile());
		//dArea.addProgram("P000");
		//dArea.addProgram("P001");
		//创建显示内容
		//动态区的显示内容与普通图文区一致
		TextCaptionBxArea dAreaContent = new
		TextCaptionBxArea(0,0,160,64,screen.getProfile());
		TextBxPage page = new TextBxPage("Dynamic");
		dAreaContent.addPage(page);
		//发送动态区
		// 发送动态区之前，如果需要删除之前的动态区，可以调用以下接口
		// 通常如果动态区的位置或大小没有发生改变，不用删除
		screen.deleteAllDynamic();
		// 更新动态区
		DynamicBxArea dAreaContent1 = new DynamicBxArea(0, 0, 160, 64, screen.getProfile());
		screen.writeDynamic(dynRule,dAreaContent1);
		//
	}
	
	public static void sendProgram(Bx6GScreenRS screen,ProgramBxFile p0) throws Bx6GException {
		//
		// 将节目文件发送到控制器
		// 发送节目有三种方式
		// 可以根据自己的需求进行选择
		//1.writeProgramsAsync() -  异步方式，即 SDK 会自己起线程来发送
		// 此时需传入 BxFileWriterListener
		// 可在相应的接口对相应的时间进行处理
		//screen.writeProgramsAsync(plist,new WriteProgramTextCaptionWithStyle());
		//
		//2.writePrograms -  同步方式，即 SDK 会 BLOCK 住一直等到节目发送完毕
		//screen.writePrograms(plist);
		screen.writeProgram(p0);
		//
		// 此方法通常不用
		//3. 同步方式将节目写入控制器，本方法不做任何检查，从而提高发送效率
		//screen.writeProgramQuickly(pf);
		//注：发送节目时，如果控制器上之前存储有相同文件名的节目，则老节目会被自动覆盖。因此，通
		//常无需手动调用删除节目命令。
	}
	public static void createTimeArea(Bx6GScreenRS screen,Bx6GScreenProfile profile,ProgramBxFile p0) {
		//
		//  下面代码创建了一个时间区
		//  注意：只需要输入时间区的起始坐标，区域的宽度和高度 SDK 会根据字体和显示方式自动计算
		DateTimeBxArea dtArea = new DateTimeBxArea(0, 0, screen.getProfile());
		//  设置字体
		dtArea.setFont(new Font("Arial", Font.PLAIN, 12));
		//  设置颜色
		dtArea.setForeground(Color.yellow);
		//  多行显示还是单行显示
		dtArea.setMultiline(true);
		//
		//  年月日的显示方式
		//  如果不显示，则设置为 null
		dtArea.setDateStyle(DateStyle.YYYY_MM_DD_1);
		dtArea.setTimeStyle(TimeStyle.HH_MM_SS_1);
		dtArea.setWeekStyle(null);
		//  将时间区添加到节目中
		p0.addArea(dtArea);
	}
	/**
	 * 创建了一个文本区，并向这个区域中，添加一个文本页，一个图片页。
	 * @param screen
	 */
	public static void createTextArea(Bx6GScreenRS screen,Bx6GScreenProfile profile,ProgramBxFile p0) {
		TextCaptionBxArea  tArea = new TextCaptionBxArea(0, 0, 200, 100, profile);
		//  创建一个数据页，并希望显示 “P000”  这几个文字
		TextBxPage page = new TextBxPage("P000 - This is the first program!");
		
		//  对文本的处理是否自动换行
		//page.setLineBreak(true);
		
		//  设置文本水平对齐方式
		page.setHorizontalAlignment(Alignment.NEAR);
		//  设置文本垂直居中方式
		page.setVerticalAlignment(Alignment.CENTER);
		//  设置文本字体
		//  ！！！注意：有些字体是不支持中文的，比如： consolas  字体，因此，如果要显示中文请将字体改成宋
		//体等中文字体
		//page.setFont(new Font(" 宋体 ", Font.PLAIN, 12)); //  字体
		page.setFont(new Font("宋体", Font.PLAIN, 12));
		//page.setFont(new Font("consolas", Font.PLAIN, 12)); //  字体
		//  设置文本颜色
		page.setForeground(Color.red);
		//  设置区域背景色，默认为黑色
		page.setBackground(Color.darkGray);
		//  调整特技方式
		page.setDisplayStyle(DisplayStyleFactory.getStyle(0));
		//  调整特技速度
		page.setSpeed(1);
		//  调整停留时间 ,  单位 10ms
		page.setStayTime(100);
		//  将前面创建的 page  添加到 area  中
		tArea.addPage(page);
		//  再创建一个数据页，用于显示图片
		ImageFileBxPage iPage = new ImageFileBxPage("d:/1.png");
		//  调整特技方式
		iPage.setDisplayStyle(DisplayStyleFactory.getStyle(1));
		//  调整特技速度
		iPage.setSpeed(1);
		//  调整停留时间 ,  单位 10ms
		iPage.setStayTime(100);
		//  将前面创建的 iPage  添加到 area  中
		tArea.addPage(iPage);
		//  将 area  添加到节目中
		p0.addArea(tArea);
	}
	/**
	 * 屏幕控制
	 */
	public static void turnOff(Bx6GScreenRS screen) {
		//关机
		Result<ACK> turnOff = screen.turnOff();
	}
	public static void turnOn(Bx6GScreenRS screen) {
		//开机
		Result<ACK> turnOn = screen.turnOn();
	}
	public static void syncTime(Bx6GScreenRS screen) {
		//校时
		Result<ACK> syncTime = screen.syncTime();
	}
	public static void manualBrightness(Bx6GScreenRS screen) {
		//
		//  调整亮度
		//  亮度值 为 1- 16 ， 16 级为最高亮度
		screen.manualBrightness((byte)8); //  将屏幕亮度调整至 8
		screen.manualBrightness((byte)16); //  将屏幕亮度调整到 16
	}
	//end
	
}
