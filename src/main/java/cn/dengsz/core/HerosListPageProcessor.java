package cn.dengsz.core;

import cn.dengsz.common.Constants;
import cn.dengsz.common.model.HeroInfo;
import cn.dengsz.common.model.HeroSkin;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.stream.Collectors;

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
        // 处理英雄列表信息
        if (page.getUrl().get().equals(Constants.HERO_URL)) {
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
            List<HeroInfo> heroInfoList = heros.toJavaList(HeroInfo.class);
            page.putField(Constants.HERO_KEY, heroInfoList);
            page.putField(Constants.VERSION, version);
            page.putField(Constants.UPDATE_TIME, updateFileTime);
            // 下载英雄图片信息，经过分析得到英雄信息详情的json信息路径（写在Constants中）
            // 需要根据每个heroId来查询对应的信息 最终返回每张图片的下载地址
            this.getImgReqList(page, heroInfoList);
        }
        // 判断当前url路径是否是英雄信息详情
        if (page.getUrl().get().contains(Constants.PIC_URL)) {
            // 处理单只英雄详情(先获取json数据对象)
            JSONObject heroDetail = JSONObject.parseObject(page.getJson().toString());
            // 从英雄详情中获取到skins这个属性
            JSONArray skins = heroDetail.getJSONArray("skins");
            List<HeroSkin> heroSkins = skins.toJavaList(HeroSkin.class);
            // 不要炫彩皮肤，我们筛选出有主皮肤图的数据即可
            List<HeroSkin> screenSkins = heroSkins.stream().filter(item -> !item.getMainImg().isEmpty()).collect(Collectors.toList());
            // 存入页面空间中待pipeLine处理
            page.putField("skins", screenSkins);
        }

    }

    /**
     * 批量去添加所有英雄的详细信息的请求路径
     */
    private void getImgReqList(Page page, List<HeroInfo> heroInfoList) {
        // 根据heroId去请求不同的英雄信息
        for (HeroInfo heroInfo : heroInfoList) {
            // 拼接图片请求路径（添加目标链接）
            page.addTargetRequest(Constants.PIC_URL+heroInfo.getHeroId()+".js");
            System.out.println(page.getJson().toString());
        }
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
