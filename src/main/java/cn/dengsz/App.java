package cn.dengsz;

import cn.dengsz.common.Constants;
import cn.dengsz.core.HerosListPageProcessor;
import cn.dengsz.core.LolHeroPipeline;
import us.codecraft.webmagic.Spider;

/**
 * @author Deng's
 */
public class App
{

    public static void main( String[] args ) {
        // 调用数据爬虫进程
        // 可以增加线程来提高运行效率(thread)
        long beginTime = System.currentTimeMillis();
        Spider.create(new HerosListPageProcessor())
                .addUrl(Constants.HERO_URL)
                .addPipeline(new LolHeroPipeline())
                .thread(5)
                .run();
        System.out.printf("用时 %d ms",System.currentTimeMillis()-beginTime);
    }
}
