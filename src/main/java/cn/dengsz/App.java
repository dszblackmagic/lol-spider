package cn.dengsz;

import cn.dengsz.core.HerosListPageProcessor;
import cn.dengsz.core.LolHeroPipeline;
import cn.dengsz.core.LolPicPageProcessor;
import us.codecraft.webmagic.Spider;

/**
 * @author Deng's
 */
public class App
{
    private static final String HERO_URL = "https://game.gtimg.cn/images/lol/act/img/js/heroList/hero_list.js?ts=2780565";
    private static final String PIC_URL = "https://game.gtimg.cn/images/lol/act/img/js/hero/103.js?ts=2780670";

    public static void main( String[] args ) {
        // 调用数据爬虫进程
        Spider.create(new HerosListPageProcessor())
                .addUrl(HERO_URL)
                .addPipeline(new LolHeroPipeline())
                .run();
        // 调用爬取图片的爬虫
        Spider.create(new LolPicPageProcessor())
                .addUrl(PIC_URL)
                .run();
    }
}
