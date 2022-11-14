package cn.dengsz.common.model;

import lombok.Data;

/**
 * @author Deng's
 * 英雄的皮肤信息实体类(这里的内容可以根据返回的json信息自己进行需要的属性定义)
 */
@Data
public class HeroSkin {

    /**
     * 皮肤id
     */
    private String skinId;

    /**
     * 英雄id
     */
    private String heroId;

    /**
     * 英雄名
     */
    private String heroName;

    /**
     * 皮肤名
     */
    private String name;

    /**
     * 主图
     */
    private String mainImg;

    /**
     * 图标
     */
    private String iconImg;

    /**
     * 炫彩皮肤
     */
    private String chromaImg;
}
