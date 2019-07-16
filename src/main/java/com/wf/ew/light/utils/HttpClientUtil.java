package com.wf.ew.light.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wf.ew.light.model.Lamp;

/**
 * Http请求工具类
 * @author Administrator
 *
 */
public class HttpClientUtil {

	/**
	 * GET无参
	 * @param url
	 */
	public static ResponseResult sendGet(String url) {
		ResponseResult result = new ResponseResult();
		result.setCode(500);
		result.setMsg("请求失败");
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 创建Get请求
		HttpGet httpGet = new HttpGet(url);
 
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 由客户端执行(发送)Get请求
			response = httpClient.execute(httpGet);
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				System.out.println("响应内容长度为:" + responseEntity.getContentLength());
				System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
				String json = EntityUtils.toString(responseEntity);
				result = JSONObject.parseObject(json, ResponseResult.class);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
		//end
	}
	
	/**
	 * GET有参
	 * @param url
	 * @param param
	 */
	public static ResponseResult sendGet(String url,String param) {
		ResponseResult result = new ResponseResult();
		result.setCode(500);
		result.setMsg("请求失败");
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
 
		// 参数
		StringBuffer params = new StringBuffer();
		try {
			// 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
			params.append("name=" + URLEncoder.encode("&", "utf-8"));
			params.append("&");
			params.append("age=24");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
 
		// 创建Get请求
		HttpGet httpGet = new HttpGet(url + "?" + params);
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 配置信息
			RequestConfig requestConfig = RequestConfig.custom()
					// 设置连接超时时间(单位毫秒)
					.setConnectTimeout(5000)
					// 设置请求超时时间(单位毫秒)
					.setConnectionRequestTimeout(5000)
					// socket读写超时时间(单位毫秒)
					.setSocketTimeout(5000)
					// 设置是否允许重定向(默认为true)
					.setRedirectsEnabled(true).build();
 
			// 将上面的配置信息 运用到这个Get请求里
			httpGet.setConfig(requestConfig);
 
			// 由客户端执行(发送)Get请求
			response = httpClient.execute(httpGet);
 
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				System.out.println("响应内容长度为:" + responseEntity.getContentLength());
				System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
				String json = EntityUtils.toString(responseEntity);
				result = JSONObject.parseObject(json, ResponseResult.class);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
		//end
	}
	
	
	/**
	 * GET有参
	 * @param url
	 * @param param
	 */
	public static ResponseResult sendGet(String ip,int port,String resource,Map<String, String> param) {
		ResponseResult result = new ResponseResult();
		result.setCode(500);
		result.setMsg("请求失败");
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
 
		// 参数
		URI uri = null;
		try {
			// 将参数放入键值对类NameValuePair中,再放入集合中
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("name", "&"));
			params.add(new BasicNameValuePair("age", "18"));
			// 设置uri信息,并将参数集合放入uri;
			// 注:这里也支持一个键值对一个键值对地往里面放setParameter(String key, String value)
			uri = new URIBuilder().setScheme("http").setHost(ip)
					              .setPort(port).setPath("resource")
					              .setParameters(params).build();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		// 创建Get请求
		HttpGet httpGet = new HttpGet(uri);
 
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 配置信息
			RequestConfig requestConfig = RequestConfig.custom()
					// 设置连接超时时间(单位毫秒)
					.setConnectTimeout(5000)
					// 设置请求超时时间(单位毫秒)
					.setConnectionRequestTimeout(5000)
					// socket读写超时时间(单位毫秒)
					.setSocketTimeout(5000)
					// 设置是否允许重定向(默认为true)
					.setRedirectsEnabled(true).build();
 
			// 将上面的配置信息 运用到这个Get请求里
			httpGet.setConfig(requestConfig);
 
			// 由客户端执行(发送)Get请求
			response = httpClient.execute(httpGet);
 
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				System.out.println("响应内容长度为:" + responseEntity.getContentLength());
				System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
				String json = EntityUtils.toString(responseEntity);
				result = JSONObject.parseObject(json, ResponseResult.class);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
		//end
	}
	
	/**
	 * POST无参
	 * @param url
	 */
	public static ResponseResult sendPost(String url) {
		ResponseResult result = new ResponseResult();
		result.setCode(200);
		result.setMsg("请求失败");
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
 
		// 创建Post请求
		HttpPost httpPost = new HttpPost(url);
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 由客户端执行(发送)Post请求
			response = httpClient.execute(httpPost);
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
 
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				System.out.println("响应内容长度为:" + responseEntity.getContentLength());
				System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
				String json = EntityUtils.toString(responseEntity);
				result=JSONObject.parseObject(json, ResponseResult.class);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
		//end
	}
	
	/**
	 * POST有参 
	 * @param url
	 * @param param
	 */
	public static ResponseResult sendPost(String url,String param) {
		ResponseResult result = new ResponseResult();
		result.setCode(200);
		result.setMsg("请求失败");
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
 
		// 参数
		StringBuffer params = new StringBuffer();
		try {
			// 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
			params.append("name=" + URLEncoder.encode("&", "utf-8"));
			params.append("&");
			params.append("age=24");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
 
		// 创建Post请求
		HttpPost httpPost = new HttpPost(url + "?" + params);
 
		// 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
		httpPost.setHeader("Content-Type", "application/json;charset=utf8");
 
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 由客户端执行(发送)Post请求
			response = httpClient.execute(httpPost);
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
 
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				System.out.println("响应内容长度为:" + responseEntity.getContentLength());
				System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
				String json = EntityUtils.toString(responseEntity);
				result = JSONObject.parseObject(json, ResponseResult.class);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
		//end
	}
	
	/**
	 * 对象参数POST
	 * @param url
	 * @param lamp
	 */
	public static ResponseResult sendPost(String url,Lamp lamp,boolean state) {
		ResponseResult result = new ResponseResult();
		result.setCode(500);
		result.setMsg("请求失败");
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		Integer zhuangtai = state == true ? 1:0;
		// 参数
		StringBuffer params = new StringBuffer();
		
		try {
			
			// 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
			/** ip:ip地址,channel:通道,flag:开灯(1)/关灯(0) */
			params.append("ip=" + URLEncoder.encode(lamp.getRemark(), "utf-8"));
			params.append("&");
			params.append("channel="+URLEncoder.encode(lamp.getChannel().trim(), "utf-8"));
			params.append("&");
			params.append("flag="+URLEncoder.encode(zhuangtai+"", "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		// 创建Post请求
		HttpPost httpPost = new HttpPost(url + "?" + params);
		RequestConfig requestConfig = RequestConfig.custom()
		        .setConnectTimeout(3000).setConnectionRequestTimeout(1000)
		        .setSocketTimeout(3000).build();
		httpPost.setConfig(requestConfig);
		/*Lamp user = new Lamp();
		user.setNickName("潘晓婷");*/
		/*user.setAge(18);
		user.setGender("女");
		user.setMotto("姿势要优雅~");*/
		RequestParam param = new RequestParam();
		param.setIp(lamp.getRemark());
		param.setChannel(Integer.parseInt(lamp.getChannel().trim()));
		//int state = lamp.getLampBefore() == null ? 0 :1;
		//param.setState(state);
		// 我这里利用阿里的fastjson，将Object转换为json字符串;
		// (需要导入com.alibaba.fastjson.JSON包)
		String jsonString = JSON.toJSONString(param);
 
		StringEntity entity = new StringEntity(jsonString, "UTF-8");
 
		// post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
		//httpPost.setEntity(entity);
 
		//httpPost.setHeader("Content-Type", "application/json;charset=utf8");
 
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 由客户端执行(发送)Post请求
			response = httpClient.execute(httpPost);
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
 
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				String json = EntityUtils.toString(responseEntity);
				System.out.println("响应内容长度为:" + responseEntity.getContentLength());
				//System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
				System.out.println("响应内容为:" + json);
				//String json = EntityUtils.toString(responseEntity);
				result = JSONObject.parseObject(json, ResponseResult.class);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
		//end
	}
	
	/**
	 * 普通参数和对象参数
	 * @param url
	 * @param param
	 * @param lamp
	 */
	public static ResponseResult sendPost(String ip,Integer port,String resource,Lamp lamp) {
		ResponseResult result = new ResponseResult();
		result.setCode(500);
		result.setMsg("请求失败");
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
 
		// 创建Post请求
		// 参数
		URI uri = null;
		try {
			// 将参数放入键值对类NameValuePair中,再放入集合中
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("flag", "4"));
			params.add(new BasicNameValuePair("meaning", "这是什么鬼？"));
			// 设置uri信息,并将参数集合放入uri;
			// 注:这里也支持一个键值对一个键值对地往里面放setParameter(String key, String value)
			uri = new URIBuilder().setScheme("http").setHost(ip).setPort(port)
					.setPath(resource).setParameters(params).build();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
 
		HttpPost httpPost = new HttpPost(uri);
		// HttpPost httpPost = new
		// HttpPost("http://localhost:12345/doPostControllerThree1");
 
		// 创建user参数
		Lamp user = new Lamp();
		user.setNickName("潘晓婷");
		/*user.setAge(18);
		user.setGender("女");
		user.setMotto("姿势要优雅~");*/
 
		// 将user对象转换为json字符串，并放入entity中
		StringEntity entity = new StringEntity(JSON.toJSONString(user), "UTF-8");
 
		// post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
		httpPost.setEntity(entity);
 
		httpPost.setHeader("Content-Type", "application/json;charset=utf8");
 
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 由客户端执行(发送)Post请求
			response = httpClient.execute(httpPost);
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
 
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				System.out.println("响应内容长度为:" + responseEntity.getContentLength());
				System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
				String json=EntityUtils.toString(responseEntity);
				result = JSONObject.parseObject(json, ResponseResult.class);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
		//end
	}
	
	//end
	
}
