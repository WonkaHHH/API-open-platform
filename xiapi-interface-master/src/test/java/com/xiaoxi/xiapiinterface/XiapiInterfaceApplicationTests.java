//package com.xiaoxi.xiapiinterface;
//
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpResponse;
//import cn.hutool.json.JSONUtil;
//import com.xiaoxi.xiapiclientsdk.client.ApiClient;
//import com.xiaoxi.xiapiclientsdk.model.User;
//import com.xiaoxi.xiapiclientsdk.model.paramtype.GetUserNameByPostParam;
//import com.xiaoxi.xiapiclientsdk.model.paramtype.ParamKeyValue;
//import com.xiaoxi.xiapiclientsdk.paramutils.GetUserNameByPostUtils;
//import com.yupi.yucongming.dev.client.YuCongMingClient;
//import com.yupi.yucongming.dev.common.BaseResponse;
//import com.yupi.yucongming.dev.model.DevChatRequest;
//import com.yupi.yucongming.dev.model.DevChatResponse;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//@SpringBootTest
//class XiapiInterfaceApplicationTests {
//
//	@Resource
//	private ApiClient  apiClient;
//
//	@Resource
//	private YuCongMingClient client;
//
//	@Test
//	void test() {
//		String result = apiClient.getAnswerByQuestion("如何学习Java");
//		System.out.println(result);
//	}
//
//	@Test
//	void testInvoke() {
//		String result = apiClient.getNameByGet("xiaoxi");
//		System.out.println(result);
//	}
//
//	@Test
//	void testInvoke2() {
//		String post = apiClient.getNameByPost("xiaoxi");
//		System.out.println(post);
//	}
//
//	@Test
//	void testInvoke3() {
//		User user = new User();
//		user.setUserName("xiaoxi");
//
//		String result = apiClient.getUserNameByPost(user);
//		System.out.println(result);
//	}
//
//	@Test
//	void test4(){
//		String json = "{\"params\":[{\"paramKey\":\"userName\",\"paramValue\":\"xiaoxi\"}]}";
//		String json2 = "{\"params\":[{\"paramKey\":\"userName\",\"paramValue\":\"xiaoxi\"}]}";
//		User user = GetUserNameByPostUtils.getUser(json);
//		System.out.println(user);
//	}
//
//	@Test
//	void test5(){
//		GetUserNameByPostParam getUserNameByPostParam = new GetUserNameByPostParam();
//		List<ParamKeyValue> paramKeyValues = new ArrayList<>();
//		ParamKeyValue paramKeyValue = new ParamKeyValue();
//		paramKeyValue.setParamKey("userName");
//		paramKeyValue.setParamValue("xiaoxi");
//		paramKeyValues.add(paramKeyValue);
//		getUserNameByPostParam.setParams(paramKeyValues);
//
//		String jsonStr = JSONUtil.toJsonStr(getUserNameByPostParam);
//		System.out.println(jsonStr);
//	}
//
//	@Test
//	void testAi() {
//		DevChatRequest devChatRequest = new DevChatRequest();
//		devChatRequest.setModelId(1654785040361893889L);
//		devChatRequest.setMessage("你的名字是什么");
//
//		BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);
//		System.out.println(response.getData());
//		System.out.println(response.getData().getContent());
//		System.out.println(response.getCode());
//		System.out.println(response.getMessage());
//	}
//
//	@Test
//	public void getAnswerByQuestion(){
//		HashMap<String, Object> paramMap = new HashMap<>();
//		paramMap.put("question", "你是谁");
//
//		HttpResponse httpResponse = HttpRequest.get("127.0.0.1:8123" + "/api/ai/getanswer").form(paramMap).header("api_source", "xiaoxi_api_gateway").execute();
//
//		System.out.println(httpResponse.getStatus());
//		String body = httpResponse.body();
//		System.out.println(body);
//	}
//}
