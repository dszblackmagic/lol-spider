package cn.dengsz.core;

import cn.dengsz.common.Constants;
import cn.dengsz.common.model.HeroInfo;
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
        // 根据Processor传递过来参数做下一步处理
        List<HeroInfo> heroInfoList = resultItems.get(Constants.HERO_KEY);
        System.out.println(heroInfoList);
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
}
