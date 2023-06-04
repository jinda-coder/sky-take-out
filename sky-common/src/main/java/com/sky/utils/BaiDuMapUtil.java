package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import com.sky.exception.BaseException;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BaiDuMapUtil {
    static String ak = "dbGNY4okjd5ZFI8nn9QonTVGiQ6kMa0H";
    //https://api.map.baidu.com/routematrix/v2/driving?output=json&origins=40.45,116.34|40.54,116.35&destinations=40.34,116.45|40.35,116.46&ak=dbGNY4okjd5ZFI8nn9QonTVGiQ6kMa0H
    //https://api.map.baidu.com/geocoding/v3/?address=北京市海淀区上地十街10号&output=json&ak=您的ak&callback=showLocation
    public static List<String> getJingWeiDU(String address) {
        String url = "https://api.map.baidu.com/geocoding/v3/?address=" + address + "&output=json&ak=" + ak;
        HttpGet req = new HttpGet(url);
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        map.put("output", "json");
        map.put("ak", "dbGNY4okjd5ZFI8nn9QonTVGiQ6kMa0H");
        String s = HttpClientUtil.doGet(url, map);
        JSONObject parse = (JSONObject) JSONObject.parse(s);
        String result = parse.getString("result");
        String jingDu = StringUtils.substringBetween(result, "{\"lng\":", ",\"lat\":");
        String weiDu = StringUtils.substringBetween(result, "\"lat\":", "},\"precise\"");
        List<String> list = new ArrayList<>();
        list.add(jingDu);
        list.add(weiDu);
        return list;
    }
    public static Double getDistance(List<String> list) {
        String url = "https://api.map.baidu.com/routematrix/v2/riding";
        Map<String, String> map = new HashMap<>();
        map.put("origins", "36.67216295004278,117.08256731389553");
        map.put("destinations", list.get(1) + "," + list.get(0));
        map.put("ak", ak);
        String s = HttpClientUtil.doGet(url, map);
        JSONObject parse = (JSONObject) JSONObject.parse(s);
        String result = parse.getString("result");
        String distance = StringUtils.substringBetween(result,"\"distance\":{\"text\":\"","公里");
        if (distance != null) {
            System.out.println("当前距离为" + distance + "公里");
            return Double.parseDouble(distance);
        }else {
            throw new BaseException("当前地址不存在，请重新输入!");
        }
    }
}
