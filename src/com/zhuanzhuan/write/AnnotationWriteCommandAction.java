package com.zhuanzhuan.write;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @Description: 注解生成执行类
 * @author: xiexing01
 * @date: 2021/11/28 11:00
 */
public class AnnotationWriteCommandAction extends WriteCommandAction {

    /**
     * 为类生成注解
     */
    public static final String ELEMENT_CLASS = "class";

    /**
     * 为字段生成注解
     */
    public static final String ELEMENT_FIELD = "field";

    /**
     * 需要生成注解的对象
     */
    private PsiElement psiElement;

    /**
     * 需要添加的注解名称
     */
    private String annotationName;

    /**
     * 需要生成注解的对象类型
     */
    private String type;

    public AnnotationWriteCommandAction(@Nullable Project project,@NotNull PsiElement psiElement,@NotNull String annotationName,@NotNull String type, @NotNull PsiFile... files) {
        super(project, files);
        this.psiElement = psiElement;
        this.annotationName = annotationName;
        this.type = type;
    }

    @Override
    protected void run(@NotNull Result result) throws Throwable {
        switch (type) {
            case ELEMENT_CLASS:
                PsiClass psiClass = (PsiClass) psiElement;
                psiClass.getModifierList().addAnnotation(annotationName);
                break;
            case ELEMENT_FIELD:
                PsiField psiField = (PsiField) psiElement;
                psiField.getModifierList().addAnnotation(annotationName);
                break;
            default:
                //do nothing
                break;
        }
    }

}
