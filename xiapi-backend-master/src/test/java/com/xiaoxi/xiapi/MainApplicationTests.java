//package com.xiaoxi.xiapi;
//
//import cn.hutool.json.JSONUtil;
////import com.xiaoxi.xiapi.config.WxOpenConfig;
//
//import javax.annotation.Resource;
//
//import com.xiaoxi.xiapi.model.vo.analyze.CategoryDataType;
//import com.xiaoxi.xiapi.model.vo.analyze.PieDataType;
//import com.xiaoxi.xiapi.utils.SnowFlakeCompone;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
///**
// * 主类测试
// */
//@SpringBootTest
//class MainApplicationTests {
//
//    @Resource
////    private WxOpenConfig wxOpenConfig;
//
//    @Resource
//    private SnowFlakeCompone snowFlakeCompone;
//
////    @Test
////    void contextLoads() {
////        System.out.println(wxOpenConfig);
////    }
//
//    @Test
//    void test() {
//        PieDataType pieDataType = new PieDataType();
//        pieDataType.setName("xiaoxi");
//        pieDataType.setValue(123);
//        PieDataType pieDataType2 = new PieDataType();
//        pieDataType2.setName("xiaoxi2");
//        pieDataType2.setValue(432);
//
//        List<PieDataType> list = new ArrayList<>();
//        list.add(pieDataType);
//        list.add(pieDataType2);
//        String jsonStr1 = JSONUtil.toJsonStr(list);
//        System.out.println(jsonStr1);
//    }
//
//    @Test
//    void test2() {
//        List<String> names = new ArrayList<>();
//        names.add("xiaoxi");
//        names.add("xiaoxi2");
//        names.add("xiaoxi3");
//
//        List<Integer> values = new ArrayList<>();
//        values.add(12);
//        values.add(22);
//        values.add(32);
//
//        CategoryDataType categoryDataType = new CategoryDataType();
//        categoryDataType.setInterfaceInfoName(names);
//
//        String jsonStr = JSONUtil.toJsonStr(categoryDataType);
//        System.out.println(jsonStr);
//    }
//
//    @Test
//    void testData() {
//        // 创建 Calendar 实例
//        Calendar calendar = Calendar.getInstance();
//
//        // 获取当前日期
//        Date currentDate = calendar.getTime();
//
//        // 获取近七天的日期
//        for (int i = 0; i < 7; i++) {
//            calendar.add(Calendar.DAY_OF_YEAR, -1); // 逐步减少一天
//            Date previousDate = calendar.getTime();
//            System.out.println(previousDate);
//        }
//
//    }
//
//    @Test
//    void testSnowId() {
//        for (int i = 0; i < 10; i++) {
//            long nextId = snowFlakeCompone.getInstance().nextId();
//            System.out.println(nextId);
//
//        }
//    }
//}
