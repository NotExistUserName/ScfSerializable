package com.zhuanzhuan.write;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.zhuanzhuan.consts.PluginConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Description: 为注解属性设置值
 * @author: xiexing01
 * @date: 2021/11/28 14:47
 */
public class AnnotationPropertyWriteCommandAction extends WriteCommandAction {

    /**
     * 创建节点工厂
     */
    private PsiElementFactory psiElementFactory;

    /**
     * 需要设置属性的注解
     */
    private PsiAnnotation psiAnnotation;

    /**
     * 需要设置的属性名称
     */
    private String propertyName;

    /**
     * 需要设置的值
     */
    private String settingValue;

    public AnnotationPropertyWriteCommandAction(@Nullable Project project,@NotNull PsiElementFactory psiElementFactory,@NotNull PsiAnnotation psiAnnotation,@NotNull String propertyName,@NotNull String settingValue, @NotNull PsiFile... files) {
        super(project, files);
        this.psiElementFactory = psiElementFactory;
        this.psiAnnotation = psiAnnotation;
        this.propertyName = propertyName;
        this.settingValue = settingValue;
    }

    @Override
    protected void run(@NotNull Result result) throws Throwable {
        psiAnnotation.setDeclaredAttributeValue(propertyName,
                psiElementFactory.createExpressionFromText(settingValue,psiAnnotation));
    }
}
