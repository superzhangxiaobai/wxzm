/**
 * 
 */
/**
 * @author Administrator
 *
 */
package com.wf.ew.screen.utils.client6;

/**
 * SDK版本   Bx06
 * 
 * BX-6Q ， BX-6E ， BX-6M ， BX-6U,BX-6A,BX-6W,BX-6S 全系列六代控制器
 * 
 * 相关工具 Ledshow TW 2018   五 / 六代单双色与全彩门楣控制器的配套 PC 软件。此软件可以用于对控制器硬件进行调试。
 * JAVA SDK   Java 版本的 二次开包
 * 
 * 通讯模式  网络（ Client ）此模式用于局域网内，控制器 IP 固定的应用场合。此时 PC 端为 TCP Client 。
 * 4.1 SDK 初始化
 * Bx6GEnv.initial("log.properties", 30000);
 * 4.2 Screen 类 与控制器的所有交互都需要通过
 * //  六代控制器创建 screen 对象方法，以 BX-6M 系列为例  Bx6GScreenClient screen = new Bx6GScreenClient(“MyScreen”,new Bx6M());
 * 4.3  屏幕连接  在对控制器交互之前，需要先与控制器建立连接
 * //  连接控制器
	//  其中， 192.168.88.199 为控制器的实际 IP 地址，请根据实际情况填写。
	//  如果你不知道控制器的 IP 地址是多少，请先使用 LedshowTW 软件对控制器进行 IP 设置
	//  端口号默认为 5005
	if (!screen.connect(“192.168.88.198”,5005))
	{
	System.out.print(“connected failed!”);
		return;
	}
 * 与控制器交互完成后，需断开与控制器之间的连接
 * //  断开与控制器之间的链接
	Screen.disconnect();
 * 4.4  屏幕控制
 * screen.turnOff(); //  关机
	screen.turnOn(); //  开机
	//  校时
	screen.syncTime();
	//  调整亮度
	//  亮度值 为 1- 16 ， 16 级为最高亮度
	screen.manualBrightness((byte)8); //  将屏幕亮度调整至 8
	screen.manualBrightness((byte)16); //  将屏幕亮度调整到 16
 * 4.5  参数回读
		 * // 可以通过以下接口，回读控制器状态
		Bx5GScreen.Result <ReturnControllerStatus> result1 =
		screen.checkControllerStatus();
		if (result1.isOK())
		{
		ReturnControllerStatus status = result1.reply;
		status.getBrightness();
		status.getRtcDay();
		status.getScreenOnOff();
		//
		// status 还有很对其他接口，可以根据实际需求再次调用以获取相应状态
		}
		else
		{
		ErrorType error = result1.getError();
		}
 * 
 * 4.6  节目与区域
 * 节目主要用于组合屏上显示的内容，它由多个区域组成。控制器同一时间只能播放一个节目，它是控制器显示内容可以单独更新的最小单位。
 * 节目(时间区（ DateTimeBxArea ）,图文区（ TextCaptionBxArea),表盘区（ ClockBxArea ）,传感器区（ TemperatureBxArea ，
HumidityBxArea 等）,其它区域 （ CounterBxArea 等）)
 * 图文区  数据页（ ImageBxPage,
	ImageFileBxPage, SimpleBxPage,
	TextBxPage, TextFileBxPage ）
 * 
 * 以下，我们将按步骤创建一个节目（将包括一个图文区和一个时间区），并将其发送到控制器进行
	显示。其步骤如下：
  创建节目文件
  创建图文区，并将其添加到节目中
  创建时间区，并将其添加到节目中
  将节目发送至控制器显示
 * 4.6.1 创建节目文件
 * //
		//  下面开始创建第一个节目， P000
		//  此节目包括只包括一个图文区 ,  图文区中包括两个数据页：一页文本，一页图片
		//  显示节目边框
		//  区域显示边框
		//  创建节目文件
		ProgramBxFile p0 = new ProgramBxFile("P000", profile);
		//  是否显示节目边框
		p0.setFrameShow(true);
		//  节目边框的移动速度
		p0.setFrameSpeed(20);
		//  使用第几个内置边框
		p0.loadFrameImage(13);
 * 4.6.2 创建图文区域
 * 控制器支持的区域有很多种，例如：图文区、时间区、传感器区等。其中，最常用的图文区
	（ TextCaptionBxArea ）。图文区可以用于显示文本和图片，文字或图片可以按数据页依次添加到区
	域中，每页数据均可设置特技方式，停留时间等属性。
	因此， 创建图文区的步骤大致如下：
	  创建 TextCaptionBxArea 对象
	  创建 TextBxPage  或 ImageFileBxPage  对象
	  将创建好的 Page 对象添加到 TextCaptionBxArea 中
	如下代码，创建了一个文本区，并向这个区域中，添加一个文本页，一个图片页。
 * 4.6.3 创建时间区
 * 时间区的创建过程大致如下：
  创建 DateTimeBxArea  对象
  设置各时间单元显示方式
  将 DateTimeBxArea 添加到节目中
 * 4.6.4 发送节目
 * 针对节目发送，我们提供了多组接口，其可分为两类。一类为同步接口，一类为异步接口。同步接
	口发送时，会阻塞主线程。异步接口会新建一个线程来发送节目，不会阻塞主线程。
	同步接口包括 writeProgram(),writePrograms()
	异步接口包括 writeProgramAsync(),writeProgramsAsync()
 * 注：发送节目时，如果控制器上之前存储有相同文件名的节目，则老节目会被自动覆盖。因此，通
	常无需手动调用删除节目命令。
 * 4.7  动态区
 * 动态区是一种比较特殊的区域，其有以下几个主要特点：
  更新次数没有限制
  内容掉电不保存
  独立于节目进行编辑
  可以支持多个区域，且每个区域可以进行单独更新
  可以和单个或多个节目绑定显示，即作为节目的一个区域进行显示
  可以作为单独的一个节目进行独立播放
  灵活的控制方式：超时时间控制、是否立即显示等
	其创建过程通常如下：
	  创建 DynamicBXAreaRule 对象
	  创建 TextCaptionBXArea 对象，并向此对象中添加相应的数据页
  发送动态区
 * 4.7.1  创建动态区
 * 
 * DynamicBxAreaRule 类的构造函数，如下所示：
	DynamicBxAreaRule(int id,byte runmode,byte immediatePlay,int timeout)
	参数说明：
	Id –  动态区的 ID 号，用于对多个动态区进行区分
	RunMode –  表示动态区的运行模式，其具体定义如下：
	0x00 ：多个数据页循环显示
	0x01 ：显示完成后静止显示最后一页数据
	0x02 ：循环显示，超过设定时间后数据仍未更新时不再显示
	0x03 ：循环显示，超过设定时间后数据仍未更新时显示 Logo 信息
	0x04 ：循环显示，显示完最后一页后就不再显示
	immediatePlay –  表示类容更新后动态区播放方式，其具体定义如下：
	0x00 ：与异步节目一起播放，即可以作为某一个或多个节目的一个区域进行显示
	0x01 ：异步节目停止播放，仅播放动态区，可以理解成此时，动态区是最为单独的一个节目来播放。
	且更新后会立即播放。
 * 0x02 ：当播放完节目编号最高的异步节目后播放该动态区。此时动态区也是作为单独节目播放，但
	会等普通节目播放完成再播放。
	Timeout –  如果需要超时处理，可设置此超时时间，其对应于 runMode 中的 0x01,0x02 模式。
	注：如果 immediatePlay 选用了模式 0x01 ，可以使用 addProgram() 接口与节目进行绑定。
	以下代码创建了一个动态区， ID 为 0 ， runMode 为 0 ， immediatePlay 为 0x00 。且此动态区与节目 0
	和节目 1 进行了绑定。具体代码如下：
 * 
 * 4.7.2  创建显示内容动态区的显示内容与普通图文区一致，如下所示：
 * 4.7.3  发送动态区
 * 
 * 
 * 192.168.101.199
 * 5005
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
