package cn.dengsz.core;

import cn.dengsz.common.Constants;
import cn.dengsz.common.model.HeroInfo;
import cn.dengsz.common.model.HeroSkin;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author Deng's
 * 处理由pageProcessor处理好后 塞过来的英雄数据（当然你可以在这里改造成存入数据库）
 */
@Slf4j
public class LolHeroPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        // 判断当前请求路径是什么 再决定做什么事情
        if (resultItems.getRequest().getUrl().equals(Constants.HERO_URL)) {
            // 根据Processor传递过来参数做下一步处理
            List<HeroInfo> heroInfoList = resultItems.get(Constants.HERO_KEY);
            // 利用hutool可以将内容快速输出成文件
            try {
                //Constants.HERO_INFO_FILE 为文件输出的地址
                FileWriter fileWriter = new FileWriter(Constants.HERO_INFO_FILE);
                fileWriter.write(JSONObject.toJSONString(heroInfoList));
                fileWriter.close();
            } catch (IOException e) {
                log.error("写出英雄信息出现问题,请查看:{}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        // 判断路径如果是英雄详情路径则开始下载图片文件
        if (resultItems.getRequest().getUrl().contains(Constants.PIC_URL)) {
            // 根据捕获到的信息下载图片
            List<HeroSkin> skins = resultItems.get("skins");
            // 创建文件夹存储皮肤(采用默认路径+英雄名 来作为文件夹路径)
            String saveFilePath = Constants.HERO_PIC_FILE + skins.get(0).getHeroName();
            FileUtil.mkdir(saveFilePath);
            for (HeroSkin skin : skins) {
                // 利用hutool工具下载文件  参数一：下载地址 参数二：保存路径
                long size = HttpUtil.downloadFile(skin.getMainImg(), FileUtil.file(saveFilePath));
                log.info("下载 {} 图片成功，大小为 {}, 存储地址为{}",skin.getName(),size,saveFilePath);
            }
        }
    }
}
