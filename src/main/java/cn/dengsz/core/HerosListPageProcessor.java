package cn.dengsz.core;

import cn.dengsz.common.Constants;
import cn.dengsz.common.model.HeroInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author Deng's
 * 去获取页面的进程
 */

public class HerosListPageProcessor implements PageProcessor {


    /**
     * 核心程序部分
     */
    @Override
    public void process(Page page) {
        System.out.println(page.getUrl());
        // 获取页面内容
        String jsonResult = page.getJson().toString();
        // 利用fastjson解析json内容(根据返回内容决定获取key:hero的内容)
        JSONObject jsonObject = JSONObject.parseObject(jsonResult);
        // 将内容转换成数组
        JSONArray heros = jsonObject.getJSONArray(Constants.HERO_KEY);
        // 版本信息、更新时间
        String version = jsonObject.getString(Constants.VERSION);
        String updateFileTime = jsonObject.getString(Constants.UPDATE_TIME);
        // 获取到数据数组 判断数组内容是否为null
        if (heros.size() == 0) {
            return;
        }
        // 将处理好的信息存入Pipeline中
        page.putField(Constants.HERO_KEY, heros.toJavaList(HeroInfo.class));
        page.putField(Constants.VERSION, version);
        page.putField(Constants.UPDATE_TIME, updateFileTime);
    }

    @Override
    public Site getSite() {
        // 设置相关的请求头信息，防止反爬虫或者无效访问被拒绝
        return Site.me().setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/" +
                        "537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                .addHeader("accept-encoding", "gzip, deflate, br")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .addHeader("origin", "https://101.qq.com")
                .setCharset("utf-8")
                .setRetryTimes(3).setSleepTime(1000);
    }
}
