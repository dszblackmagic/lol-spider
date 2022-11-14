package cn.dengsz.common.model;

import lombok.Data;

/**
 * @author Deng's
 * 仅仅获取一些有用的相关数据 保存下来。
 */
@Data
public class HeroInfo {

    /**
     * 英雄id
     */
    private String heroId;

    /**
     * 中文名
     */
    private String name;

    /**
     * 别名
     */
    private String alias;

    /**
     * 信息标题
     */
    private String title;

    /**
     * 金币售价
     */
    private String goldPrice;

    /**
     * 点券售价
     */
    private String couponPrice;

    /**
     * 一些关键信息
     */
    private String keywords;


}
