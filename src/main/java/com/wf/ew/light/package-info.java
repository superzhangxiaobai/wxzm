/**
 * 
 */
/**
 * @author Administrator
 *
 */
package com.wf.ew.light;

import com.wf.ew.common.JsonResult;
import com.wf.ew.common.utils.ParamConfig;
import com.wf.ew.common.utils.SerialPortUtils;
import com.wf.ew.light.model.Lamp;

/**
 * 代码变更记录
 * 2019-05-14
 * 
 * private JsonResult serialPortHelp (Lamp lamp,String bstate,Integer type) {
		System.out.println("前:"+lamp.getLampBefore());//1#通道
		System.out.println("后:"+lamp.getLampAfter());//2#通道
		System.out.println("左:"+lamp.getLampLeft());//3#通道
		System.out.println("右:"+lamp.getLampRight());//4#通道
		// 实例化串口操作对象
		//SerialPortUtils serialPort = new SerialPortUtils();
		SerialPortUtils serialPort = SerialPortUtils.getInstance();
		// 创建串口必要参数接收类并赋值，赋值串口号，波特率，校验位，数据位，停止位
        //ParamConfig paramConfig = new ParamConfig("COM3", 9600, 0, 8, 1);
        ParamConfig paramConfig = new ParamConfig(lamp.getRemark().trim(), 9600, 0, 8, 1);
        String cmdurl = lamp.getMemo().trim();
        // 初始化设置,打开串口，开始监听读取串口数据
        serialPort.init(paramConfig);
        //serialPort.sendComm(serialPort.bytesToHexString("$066".getBytes())+"0D");//查询状态
        //serialPort.closeSerialPort();
        //----------------------------------------------------------发送数据之前查询状态开始
        /*String qianmsg = "" ;
        long qianstartTime = System.currentTimeMillis();
        //请求之前发送命令
        //$066\CR
        serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"08").getBytes())+"0D");// 读取前一次装填
        while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - qianstartTime > 1000*15) {
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				qianmsg = "请求超时" ;
				break ;
			}
			if (serialPort.flag == 1) {
				qianmsg = serialPort.msg ;
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				break ;
			}
		}
        System.out.println("发送之前串口返回结果======>"+qianmsg);
        if (qianmsg.equals("请求超时")) {
        	return JsonResult.ok("-1").put("res", qianmsg) ;
		}
       
        //serialPort.closeSerialPort();
        //return JsonResult.ok(code).put("res", msg) ;
        //----------------------------------------------------------发送数据之前查询状态结束
        StringBuilder sb = new StringBuilder() ;
        Integer panduantiaojian = null ;
        if (type == null) {
        	//跟以前一模一样逻辑
			panduantiaojian = lamp.getLampBefore() ;
		}else {
			if (type == 0) {
				panduantiaojian = lamp.getLampBefore() ;
			} else {
				panduantiaojian = lamp.getLampSecond() ;
			}
		}
        String shiliu = "0123456789ABCDEF" ;
        if (panduantiaojian == null) {
        	//serialPort.sendComm(serialPort.bytesToHexString("@0600".getBytes())+"0D");//设置关闭所有通道
        	//serialPort.sendComm(serialPort.bytesToHexString("@061E".getBytes())+"0D");//关闭1通道
        	//关闭所有通道
        	if (lamp.getCategory().startsWith("路灯") || lamp.getCategory().startsWith("顶灯")) {
        		sb.append("0000");
        		serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"00").getBytes())+"0D");// 关闭1通道路灯
			}else {
				int parseInt = shiliu.indexOf(bstate);
				int parseInt2 = Integer.parseInt(lamp.getChannel());
				if (type != null && type == 1) {
					parseInt2 = Integer.parseInt(lamp.getSecondchannel());
				}
				double pow = Math.pow(2, parseInt2-1);
				int i = parseInt - (int) pow;
				if (i<0) {
					serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0"+bstate).getBytes())+"0D");// 开启4通道
				} else {
					serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0"+shiliu.charAt(i)).getBytes())+"0D");// 开启4通道
				}
				//serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0"+i).getBytes())+"0D");// 开启4通道
				sb.append("0000");
			}
        	//sb.append("0000");
		} else {
			//serialPort.sendComm(serialPort.bytesToHexString("@061F".getBytes())+"0D");//设置所有通道输出
			//serialPort.sendComm(serialPort.bytesToHexString("@0601".getBytes())+"0D");//开启1通道
			//开启所有通道
			if (lamp.getCategory().startsWith("路灯") || lamp.getCategory().startsWith("顶灯")) {
        		sb.append("1111");
        		serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"01").getBytes())+"0D");// 开启1通道路灯
			}else {
				int parseInt = shiliu.indexOf(bstate);
				int parseInt2 = Integer.parseInt(lamp.getChannel());
				if (type != null && type == 1) {
					parseInt2 = Integer.parseInt(lamp.getSecondchannel());
				}
				double pow = Math.pow(2, parseInt2-1);
				int i = parseInt + (int) pow;
				serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0"+shiliu.charAt(i)).getBytes())+"0D");// 开启4通道
				sb.append("1111");
			}
			//sb.append("1111");
		}
        /*if (lamp.getLampAfter() == null) {
        	//serialPort.sendComm(serialPort.bytesToHexString("@061D".getBytes())+"0D");//关闭2通道
        	sb.append("0");
		} else {
			//serialPort.sendComm(serialPort.bytesToHexString("@0602".getBytes())+"0D");// 开启2通道
			sb.append("1");
		}
        if (lamp.getLampLeft() == null) {
        	//serialPort.sendComm(serialPort.bytesToHexString("@061B".getBytes())+"0D");//关闭3通道
        	sb.append("0");
		} else {
			//serialPort.sendComm(serialPort.bytesToHexString("@0604".getBytes())+"0D");// 开启3通道
			sb.append("1");
		}
        if (lamp.getLampRight() == null) {
        	//serialPort.sendComm(serialPort.bytesToHexString("@0617".getBytes())+"0D");//关闭4通道
        	sb.append("0");
		} else {
			//serialPort.sendComm(serialPort.bytesToHexString("@0608".getBytes())+"0D");// 开启4通道
			sb.append("1");
		}
        String code = sb.toString() ;
        /*if (code.equals("0001")) {//开启4通道
        	serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"08").getBytes())+"0D");// 开启4通道
		}else if (code.equals("1000")) {//开启1通道
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"01").getBytes())+"0D");//开启1通道
		}else if (code.equals("0100")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"02").getBytes())+"0D");// 开启2通道
		}
		else if (code.equals("0010")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"04").getBytes())+"0D");// 开启3通道
		}else if (code.equals("1100")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"03").getBytes())+"0D");// 开启12通道
		}else if (code.equals("1110")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"07").getBytes())+"0D");// 开启123通道
		}else if (code.equals("1111")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0F").getBytes())+"0D");// 开启1234通道
		}else if (code.equals("0110")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"06").getBytes())+"0D");// 开启23通道
		}else if (code.equals("0111")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0E").getBytes())+"0D");// 开启234通道
		}else if (code.equals("0011")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0C").getBytes())+"0D");// 开启34通道
		}else if (code.equals("1010")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"05").getBytes())+"0D");// 开启13通道
		}else if (code.equals("1001")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"09").getBytes())+"0D");// 开启14通道
		}else if (code.equals("0101")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0A").getBytes())+"0D");// 开启24通道
		}else if (code.equals("0000")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"00").getBytes())+"0D");// 关闭所有通道
		}else if (code.equals("1101")) {
			serialPort.sendComm(serialPort.bytesToHexString(("@"+cmdurl+"0B").getBytes())+"0D");// 开启124通道
		}
        System.out.println("code==============="+code);
        //serialPort.sendComm(serialPort.bytesToHexString("$066".getBytes())+"0D");//查询状态
        //serialPort.sendComm(serialPort.bytesToHexString("@0600".getBytes())+"0D");//设置关闭所有通道
        //serialPort.sendComm(serialPort.bytesToHexString("@061F".getBytes())+"0D");//设置所有通道输出
        String msg = "" ;
        long startTime = System.currentTimeMillis();
        while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - startTime > 1000*15) {
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				serialPort.ssb.delete(0, serialPort.ssb.length());
				msg = "请求超时" ;
				break ;
			}
			if (serialPort.flag == 1) {
				msg = serialPort.msg ;
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				serialPort.ssb.delete(0, serialPort.ssb.length());
				break ;
			}
		}
        System.out.println("串口返回结果======>"+msg);
        serialPort.closeSerialPort();
        return JsonResult.ok(code).put("res", msg) ;
	}
 * 
 * 
 * 
 * 
 * private JsonResult serialPortHelpBefore (Lamp lamp) {
		// 实例化串口操作对象
		SerialPortUtils serialPort = SerialPortUtils.getInstance();
		// 创建串口必要参数接收类并赋值，赋值串口号，波特率，校验位，数据位，停止位
        //ParamConfig paramConfig = new ParamConfig("COM3", 9600, 0, 8, 1);
        ParamConfig paramConfig = new ParamConfig(lamp.getRemark().trim(), 9600, 0, 8, 1);
        String cmdurl = lamp.getMemo().trim();
        // 初始化设置,打开串口，开始监听读取串口数据
        serialPort.init(paramConfig);
        StringBuilder sb = new StringBuilder() ;
        
        serialPort.sendComm(serialPort.bytesToHexString("$066".getBytes())+"0D");//查询状态
       
        String msg = "" ;
        long startTime = System.currentTimeMillis();
        while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - startTime > 1000*15) {
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				serialPort.ssb.delete(0, serialPort.ssb.length());
				msg = "请求超时" ;
				break ;
			}
			if (serialPort.flag == 1) {
				msg = serialPort.ssb.toString() ;
				serialPort.flag = 0 ;
				serialPort.msg = "" ;
				serialPort.ssb.delete(0, serialPort.ssb.length());
				break ;
			}
		}
        System.out.println("返回前串口返回结果======>"+msg);
        serialPort.closeSerialPort();
        return JsonResult.ok("-1").put("res", msg) ;
	}
 * 
 * 
 * 
 * /*RedisUtil redisUtil = new RedisUtil();
    	redisUtil.setRedisTemplate(redisTemplate);
    	try {
    		//redisUtil.set("carno", "苏N6RM96");
    		String carno = redisUtil.get("carno");
        	System.out.println("车牌号是===================================>"+carno);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	//return JsonResult.ok();
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */