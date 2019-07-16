package com.wf.ew.light.utils;

import com.wf.ew.common.JsonResult;
import com.wf.ew.common.utils.ParamConfig;
import com.wf.ew.common.utils.SerialPortUtils;
import com.wf.ew.light.model.Lamp;

/**
 * 串口操作工具类
 * @author 
 *
 */
public class SerialPortUtil {
	
	/**
	 * 一键开灯和关灯向串口发送命令
	 * @param lamp
	 * @param isOpenClose
	 * @return
	 */
	public static JsonResult serialPortOneKeyHelp(Lamp lamp,boolean isOpenClose) {
		// 实例化串口操作对象
		SerialPortUtils serialPort = SerialPortUtils.getInstance();
		// 创建串口必要参数接收类并赋值，赋值串口号，波特率，校验位，数据位，停止位
		ParamConfig paramConfig = new ParamConfig(lamp.getRemark().trim(), 9600, 0, 8, 1);
		// 命令地址
		String cmdurl = lamp.getMemo().trim();
		// 初始化设置,打开串口，开始监听读取串口数据
		if (serialPort.getSerialPort() == null) {
        	serialPort.init(paramConfig);
		}
		String code = null;
		if (isOpenClose) {
			//一键开灯
			code = "1111";
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0F").getBytes())+"0D");// 关闭1通道路灯
		} else {
			//一键关灯
			code = "0000";
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"00").getBytes())+"0D");// 关闭1通道路灯
		}
		
		 // 串口命令返回的提示信息
        String msg = "" ;
        // 超时设置开始时间
        long startTime = System.currentTimeMillis();
        // 循环等待串口返回
        while (true) {
			try {
				// 休眠 100 毫秒
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 如果命令发送 5 秒还没反应，返回超时
			if (System.currentTimeMillis() - startTime > 100*3) {
				// 串口标志设为 0
				serialPort.flag = 0 ;
				// 串口字符串设为空
				serialPort.msg = "" ;
				// 缓存字符串清空
				serialPort.ssb.delete(0, serialPort.ssb.length());
				msg = "请求超时" ;
				break ;
			}
			if (serialPort.flag == 1) {
				// 设置串口返回字符串
				msg = serialPort.msg ;
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				// 缓存字符串清空
				serialPort.ssb.delete(0, serialPort.ssb.length());
				break ;
			}
		}
        System.out.println("一键串口返回结果======>"+msg);
        // 关闭串口
        serialPort.closeSerialPort();
        return JsonResult.ok(code).put("res", msg) ;
	}
	
	/**
	 * 向串口发送命令
	 * @param lamp
	 * @param bstate
	 * @param type
	 * @return
	 */
	public static JsonResult serialPortHelp (Lamp lamp,String bstate,Integer type) {
		// 实例化串口操作对象
		SerialPortUtils serialPort = SerialPortUtils.getInstance();
		// 创建串口必要参数接收类并赋值，赋值串口号，波特率，校验位，数据位，停止位
		ParamConfig paramConfig = new ParamConfig(lamp.getRemark().trim(), 9600, 0, 8, 1);
		// 命令地址
		String cmdurl = lamp.getMemo().trim();
		// 初始化设置,打开串口，开始监听读取串口数据
		if (serialPort.getSerialPort() == null) {
        	serialPort.init(paramConfig);
		}
        //serialPort.init(paramConfig);
        //路灯状态字符串构建
        StringBuilder sb = new StringBuilder() ;
        //发送命令判断条件
        Integer panduantiaojian = null ;
        if (type == null) {
        	//跟以前一模一样逻辑,通用逻辑
			panduantiaojian = lamp.getLampBefore() ;
		}else {
			if (type == 0) {
				//灯塔带路灯的灯塔逻辑
				panduantiaojian = lamp.getLampBefore() ;
			} else {
				// 灯塔带路灯的路灯逻辑
				panduantiaojian = lamp.getLampSecond() ;
			}
		}
        // 查询上一次命令状态十六进制字符串，转换成十进制数进行运算，通过数组下标。
        String shiliu = "0123456789ABCDEF" ;
        if (panduantiaojian == null) {
        	// 关灯判断
        	if (lamp.getCategory().startsWith("路灯") || lamp.getCategory().startsWith("顶灯")) {
        		//路灯和顶灯是一组操作,关闭路灯和顶灯。
        		sb.append("0000");
        		serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"00").getBytes())+"0D");// 关闭1通道路灯
			}else {
				// 之前状态十进制表示
				int parseInt = shiliu.indexOf(bstate);
				// 灯塔通道数
				int parseInt2 = Integer.parseInt(lamp.getChannel());
				if (type != null && type == 1) {
					// 灯塔路灯通道数
					parseInt2 = Integer.parseInt(lamp.getSecondchannel());
				}
				// 本次通道减除十进制数
				double pow = Math.pow(2, parseInt2-1);
				//之前状态判断
				String channelHelp = StateContrastUtil.channelHelp(parseInt);
				char charAt = channelHelp.charAt(parseInt2-1);
				// 上次返回结果 - 本次数 = 要发送的数
				int i = parseInt - (int) pow;
				boolean shangci = false;
				if (charAt == '0') {
					//之前状态已关灯,直接放上次命令状态
					System.out.println("---------------------------之前状态已关灯---------------------------");
					shangci = true ;
				}
				if (i<0 || shangci == true) {
					// 发送之前查询到的命令
					serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0"+bstate).getBytes())+"0D");// 发送上一次命令状态，不变
				} else {
					// 要发送的数从十进制转换成十六进制
					serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0"+shiliu.charAt(i)).getBytes())+"0D");// 关闭n通道
				}
				// 关灯设置 0000
				sb.append("0000");
			}
        	
		} else {
			// 开灯判断
			if (lamp.getCategory().startsWith("路灯") || lamp.getCategory().startsWith("顶灯")) {
        		sb.append("1111");
        		serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"01").getBytes())+"0D");// 开启1通道路灯
			}else {
				// 之前状态十进制表示
				int parseInt = shiliu.indexOf(bstate);
				// 查询灯塔通道
				int parseInt2 = Integer.parseInt(lamp.getChannel());
				if (type != null && type == 1) {
					// 查询灯塔路灯的通道
					parseInt2 = Integer.parseInt(lamp.getSecondchannel());
				}
				// 本次通道加上 十进制数
				double pow = Math.pow(2, parseInt2-1);
				//之前状态判断
				String channelHelp = StateContrastUtil.channelHelp(parseInt);
				char charAt = channelHelp.charAt(parseInt2-1);
				
				// 上次返回的结果 + 本次数 = 要发送的数
				int i = parseInt + (int) pow;
				boolean shangci = false;
				if (charAt == '1') {
					//此通道已经开灯,直接发送上次命令状态
					System.out.println("-----------------------此通道已经开灯-----------------------------------------");
					shangci = true ;
				}
				if (i > 15 || shangci == true) {
					serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0"+bstate).getBytes())+"0D");// 发送上一次命令状态，不变
				} else {
					// 要发送的数从十进制转换成十六进制数
					serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0"+shiliu.charAt(i)).getBytes())+"0D");// 开启n通道
				}
				
				// 开灯设置 1111
				sb.append("1111");
			}
			
		}
        // 数据库中保存的路灯和灯塔状态结果
        String code = sb.toString() ;
        System.out.println("code==============="+code);
        // 串口命令返回的提示信息
        String msg = "" ;
        // 超时设置开始时间
        long startTime = System.currentTimeMillis();
        // 循环等待串口返回
        while (true) {
			try {
				// 休眠 100 毫秒
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 如果命令发送 5 秒还没反应，返回超时
			if (System.currentTimeMillis() - startTime > 1000*5) {
				// 串口标志设为 0
				serialPort.flag = 0 ;
				// 串口字符串设为空
				serialPort.msg = "" ;
				// 缓存字符串清空
				serialPort.ssb.delete(0, serialPort.ssb.length());
				msg = "请求超时" ;
				break ;
			}
			if (serialPort.flag == 1) {
				// 设置串口返回字符串
				msg = serialPort.msg ;
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				// 缓存字符串清空
				serialPort.ssb.delete(0, serialPort.ssb.length());
				break ;
			}
		}
        System.out.println("串口返回结果======>"+msg);
        // 关闭串口
        serialPort.closeSerialPort();
        return JsonResult.ok(code).put("res", msg) ;
	}
	
	/**
	 * 发送之前查询串口状态
	 * @param lamp
	 * @return
	 */
	public static JsonResult serialPortHelpBefore (Lamp lamp) {
		// 实例化串口操作对象
		SerialPortUtils serialPort = SerialPortUtils.getInstance();
		// 创建串口必要参数接收类并赋值，赋值串口号，波特率，校验位，数据位，停止位
        ParamConfig paramConfig = new ParamConfig(lamp.getRemark().trim(), 9600, 0, 8, 1);
        
        // 初始化设置,打开串口，开始监听读取串口数据
        if (serialPort.getSerialPort() == null) {
        	serialPort.init(paramConfig);
		}
        //serialPort.init(paramConfig);
        // 命令地址
     	String cmdurl = lamp.getMemo().trim();
        //查询串口状态
        //serialPort.sendComm(serialPort.bytesToHexString("$066".getBytes())+"0D");
        serialPort.sendComm(serialPort.bytesToHexString(("$"+cmdurl+"6").getBytes())+"0D");
        // 接收串口返回信息
        String msg = "" ;
        // 发送开始时间
        long startTime = System.currentTimeMillis();
        // 循环等待串口返回结果
        while (true) {
			try {
				// 休眠 100 毫秒
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 5秒为超时时间
			if (System.currentTimeMillis() - startTime > 100*5) {
				// 设置串口状态标记为 0
				serialPort.flag = 0 ;
				// 设置串口返回信息为空
				serialPort.msg = "" ;
				// 清空串口缓存信息
				serialPort.ssb.delete(0, serialPort.ssb.length());
				// 设置返回字符串，并退出循环
				msg = "请求超时" ;
				break ;
			}
			if (serialPort.flag == 1 && serialPort.ssb.toString().length() == 7) {
				// 从串口接受信息赋值
				msg = serialPort.ssb.toString() ;
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				// 清空串口缓存信息，并退出
				serialPort.ssb.delete(0, serialPort.ssb.length());
				break ;
			}
		}
        System.out.println("返回前串口返回结果======>"+msg);
        // 关闭串口
        serialPort.closeSerialPort();
        return JsonResult.ok("-1").put("res", msg) ;
	}
	
	/**
	 * 实时查询串口状态串口状态
	 * @param lamp
	 * @return
	 */
	public static JsonResult serialPortHelpState (Lamp lamp) {
		// 实例化串口操作对象
		SerialPortUtils serialPort = SerialPortUtils.getInstance();
		// 创建串口必要参数接收类并赋值，赋值串口号，波特率，校验位，数据位，停止位
        ParamConfig paramConfig = new ParamConfig(lamp.getRemark().trim(), 9600, 0, 8, 1);
        
        // 初始化设置,打开串口，开始监听读取串口数据
        if (serialPort.getSerialPort() == null) {
        	serialPort.init(paramConfig);
		}
        //serialPort.init(paramConfig);
        // 命令地址
     	String cmdurl = lamp.getMemo().trim();
        //查询串口状态
        //serialPort.sendComm(serialPort.bytesToHexString("$066".getBytes())+"0D");
        serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"").getBytes())+"0D");
        // 接收串口返回信息
        String msg = "" ;
        // 发送开始时间
        long startTime = System.currentTimeMillis();
        // 循环等待串口返回结果
        while (true) {
			try {
				// 休眠 100 毫秒
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 5秒为超时时间
			if (System.currentTimeMillis() - startTime > 100*2) {
				// 设置串口状态标记为 0
				serialPort.flag = 0 ;
				// 设置串口返回信息为空
				serialPort.msg = "" ;
				// 清空串口缓存信息
				serialPort.ssb.delete(0, serialPort.ssb.length());
				// 设置返回字符串，并退出循环
				msg = "请求超时" ;
				break ;
			}
			if (serialPort.flag == 1 && serialPort.ssb.toString().length() == 5) {
				// 从串口接受信息赋值
				msg = serialPort.ssb.toString() ;
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				// 清空串口缓存信息，并退出
				serialPort.ssb.delete(0, serialPort.ssb.length());
				break ;
			}
		}
        System.out.println("实时查询串口状态结果======>"+msg);
        // 关闭串口
        serialPort.closeSerialPort();
        return JsonResult.ok("200").put("res", msg) ;
	}
	
	// 判断输出状态和输入状态是否一直，判断开关灯操作是否成功
	// 正常情况输出 + 输入 = F(15)
	// 当输出 和 输入 和 > F(15),证明输出了，输入没反应，没有进行相应的减操作，所有输出无效
	// 当输出 和 输入 和 < F(15),证明没有输出，而输入进行了相应的减操作，代表手动开灯了。
	// 现象说明，当自动开灯之后，手动关不了(实际手动能关)，手动关了之后,出现 和 > 15 情况
	// 当手动开灯之后，自动关灯关不了，应该手动关灯。
	// 结论，手动操作不会出现 和 > 15情况，只能出现小于15情况，和大于15情况，只能是通道无效情况，有输出，没输入，相等正常情况。
	public static String realStateAutoHand(String r) {
		// >000D
		// 查询的时候，只要手动状态，自动状态不能操作
		// 操作的时候状态不一致要进行复位
		String shiliu = "0123456789ABCDEF" ;
		String out = r.substring(2, 3);
		String in = r.substring(4, 5);
		int outValue = shiliu.indexOf(out);
		int inValue = shiliu.indexOf(in);
		int reverseIn = 15 - inValue ;
		if (reverseIn == outValue) {
			return "0000";
			//正常情况
		}else {
			//手动状态
			String channelHelp = StateContrastUtil.channelHelp(Math.abs(outValue - reverseIn));
			return channelHelp;
		}
		 
		//return 0;
	}
	
	public static String realStateHelp(String r) {
		// >000D
		// 查询的时候，只要手动状态，自动状态不能操作
		// 操作的时候状态不一致要进行复位
		String shiliu = "0123456789ABCDEF" ;
		String out = r.substring(2, 3);
		String in = r.substring(4, 5);
		int outValue = shiliu.indexOf(out);
		int inValue = shiliu.indexOf(in);
		int reverseIn = 15 - inValue ;
		if (reverseIn == outValue) {
			return "-1";
			//正常情况
		}else {
			//无效通道
			return shiliu.charAt(reverseIn)+"";
		}
		 
		//return 0;
	}
	
	// 开灯没反应，复位操作
	/**
	 * 开灯没反应复位操作
	 * @param lamp
	 * @return
	 */
	public static JsonResult serialPortHelpReset (Lamp lamp,String state) {
		// 实例化串口操作对象
		SerialPortUtils serialPort = SerialPortUtils.getInstance();
		// 创建串口必要参数接收类并赋值，赋值串口号，波特率，校验位，数据位，停止位
        ParamConfig paramConfig = new ParamConfig(lamp.getRemark().trim(), 9600, 0, 8, 1);
        
        // 初始化设置,打开串口，开始监听读取串口数据
        if (serialPort.getSerialPort() == null) {
        	serialPort.init(paramConfig);
		}
        //serialPort.init(paramConfig);
        // 命令地址
     	String cmdurl = lamp.getMemo().trim();
        //查询串口状态
        //serialPort.sendComm(serialPort.bytesToHexString("$066".getBytes())+"0D");
        serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0"+state).getBytes())+"0D");
        // 接收串口返回信息
        String msg = "" ;
        // 发送开始时间
        long startTime = System.currentTimeMillis();
        // 循环等待串口返回结果
        while (true) {
			try {
				// 休眠 100 毫秒
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 5秒为超时时间
			if (System.currentTimeMillis() - startTime > 100*3) {
				// 设置串口状态标记为 0
				serialPort.flag = 0 ;
				// 设置串口返回信息为空
				serialPort.msg = "" ;
				// 清空串口缓存信息
				serialPort.ssb.delete(0, serialPort.ssb.length());
				// 设置返回字符串，并退出循环
				msg = "请求超时" ;
				break ;
			}
			if (serialPort.flag == 1 && serialPort.ssb.toString().length() > 0) {
				// 从串口接受信息赋值
				msg = serialPort.ssb.toString() ;
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				// 清空串口缓存信息，并退出
				serialPort.ssb.delete(0, serialPort.ssb.length());
				break ;
			}
		}
        System.out.println("复位操作状态结果结果======>"+msg);
        // 关闭串口
        serialPort.closeSerialPort();
        return JsonResult.ok("200").put("res", msg) ;
	}
	//end
}
