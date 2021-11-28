package com.zhuanzhuan.consts;

/**
 * @Description: 插件常量类
 * @author: xiexing01
 * @date: 2021/11/28 12:28
 */
public final class PluginConstants {

    private PluginConstants(){};

    /**
     * 插件名称
     */
    public static final String PLUGIN_NAME = "ScfSerializable Plugin";

    /**
     * SCFSerializable注解
     */
    public static final String ANNOTATION_NAME_SCF_SERIALIZABLE = "SCFSerializable";

    /**
     * SCFMember 注解
     */
    public static final String ANNOTATION_NAME_SCF_MEMBER = "SCFMember";

    /**
     * SCFMember 注解属性 orderId
     */
    public static final String ANNOTATION_PROPERTY_ORDER_ID = "orderId";

    /**
     * SCFMember 注解属性 generic
     */
    public static final String ANNOTATION_PROPERTY_GENERIC = "generic";

    /**
     * SCFMember 注解属性 generic.true
     */
    public static final String ANNOTATION_PROPERTY_GENERIC_TRUE = "true";

    /**
     * SCFSerializable 全例名
     */
    public static final String SCF_SERIALIZABLE_TOTAL_PACKAGE_NAME = "com.bj58.spat.scf.serializer.component.annotation.SCFSerializable";

    /**
     * SCFMember 全例名
     */
    public static final String SCF_MEMBER_TOTAL_PACKAGE_NAME = "com.bj58.spat.scf.serializer.component.annotation.SCFMember";

}
