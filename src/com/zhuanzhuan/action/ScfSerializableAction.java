package com.zhuanzhuan.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtil;
import com.zhuanzhuan.consts.PluginConstants;
import com.zhuanzhuan.utils.PsiFieldUtils;
import com.zhuanzhuan.write.AnnotationPropertyWriteCommandAction;
import com.zhuanzhuan.write.AnnotationWriteCommandAction;
import com.zhuanzhuan.write.ElementDeleteCommandAction;
import com.zhuanzhuan.write.ImportWriteCommandAction;

import java.util.List;
import java.util.Objects;

public class ScfSerializableAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        //当前工程
        Project project = event.getProject();
        //当前文件
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        //文件创建工厂
        PsiElementFactory psiElementFactory = PsiElementFactory.getInstance(project);
        if (!(psiFile instanceof PsiJavaFile)) {
            Messages.showWarningDialog("Please focus on a java file", PluginConstants.PLUGIN_NAME);
            return;
        }
        List<PsiClass> psiClassList = PsiTreeUtil.getChildrenOfTypeAsList(psiFile, PsiClass.class);
        if (psiClassList == null || psiClassList.isEmpty()) {
            Messages.showWarningDialog(String.format("%s cannot find any java class",PluginConstants.PLUGIN_NAME),PluginConstants.PLUGIN_NAME);
            return;
        }
        //只聚焦当前类
        PsiClass psiClass = psiClassList.get(0);
        //当前类是否为泛型
        PsiClassType currentPsiClassType = PsiTypesUtil.getClassType(psiClass);
        boolean currentPsiClassTypeIsGeneric =  currentPsiClassType.isRaw();
        //为当前类生成ScfSerializable注解
        this.genScfSerializable(project,psiFile,psiClass);
        //为字段生成ScfMember注解
        this.genScfMember(project,psiFile,psiClass,psiElementFactory,currentPsiClassTypeIsGeneric);
    }

    /**
     * 为所有非常量字段加上ScfMember注解
     *
     * @param project
     * @param psiFile
     * @param psiClass
     * @param psiElementFactory
     */
    private void genScfMember(Project project,PsiFile psiFile,PsiClass psiClass,PsiElementFactory psiElementFactory,boolean currentPsiClassTypeIsGeneric) {
        //获取当前类所有字段,不包含父类字段
        PsiField[] psiFields = psiClass.getFields();
        if (psiFields == null || psiFields.length == 0) {
            return;
        }
        for (int i = 0; i < psiFields.length; i++) {
            PsiField psiField = psiFields[i];
            boolean modifyByFinal = psiField.getModifierList().hasModifierProperty(PsiModifier.FINAL);
            if (modifyByFinal) {
                //常量不处理
                continue;
            }
            PsiAnnotation[] psiFieldAnnotations = psiField.getAnnotations();
            //当前字段是否存在ScfMember注解
            PsiAnnotation scfMemberAnnotation = this.findScfMemberAnnotation(psiFieldAnnotations);
            //ScfMember注解存在则执行删除
            if (scfMemberAnnotation != null) {
                ElementDeleteCommandAction elementDeleteCommandAction = new ElementDeleteCommandAction(project,scfMemberAnnotation,psiFile);
                elementDeleteCommandAction.execute();
            }
            //为当前字段生成ScfMember注解
            AnnotationWriteCommandAction annotationWriteCommandAction = new AnnotationWriteCommandAction(project,psiField,PluginConstants.ANNOTATION_NAME_SCF_MEMBER,AnnotationWriteCommandAction.ELEMENT_FIELD,psiFile);
            annotationWriteCommandAction.execute();
            //导入ScfMember注解包
            ImportWriteCommandAction importWriteCommandAction = new ImportWriteCommandAction(project,psiClass,PluginConstants.SCF_MEMBER_TOTAL_PACKAGE_NAME,psiFile);
            importWriteCommandAction.execute();
            //重新获取刚才创建的ScfMember注解
            PsiAnnotation[] reGetPsiAnnotations = psiField.getAnnotations();
            //设置当前ScfMember注解的顺序
            PsiAnnotation existsScfMemberAnnotation = this.findScfMemberAnnotation(reGetPsiAnnotations);
            if (existsScfMemberAnnotation == null) {
                Messages.showWarningDialog("Generated scfMember for field error,please connect admin(xiexing01)",PluginConstants.PLUGIN_NAME);
                return;
            }
            //设置orderId为当前序值
            AnnotationPropertyWriteCommandAction orderIdWriteCommandAction =
                    new AnnotationPropertyWriteCommandAction(project,psiElementFactory,existsScfMemberAnnotation,
                            PluginConstants.ANNOTATION_PROPERTY_ORDER_ID,String.valueOf(i + 1),psiFile);
            orderIdWriteCommandAction.execute();
            //判断当前字段是否为泛型
            boolean isGeneric = PsiFieldUtils.isGeneric(psiField,currentPsiClassTypeIsGeneric);
            if (isGeneric) {
                //设置generic为当前序值
                AnnotationPropertyWriteCommandAction genericWriteCommandAction =
                        new AnnotationPropertyWriteCommandAction(project,psiElementFactory,existsScfMemberAnnotation,
                                PluginConstants.ANNOTATION_PROPERTY_GENERIC,PluginConstants.ANNOTATION_PROPERTY_GENERIC_TRUE,psiFile);
                genericWriteCommandAction.execute();
            }
        }
    }

    /**
     * 为当前类生成ScfSerializable注解
     *
     * @param project
     * @param psiFile
     * @param psiClass
     */
    private void genScfSerializable(Project project,PsiFile psiFile,PsiClass psiClass) {
        //当前类所有注解
        PsiAnnotation[] annotations = psiClass.getAnnotations();
        //为当前类生成SCFSerializable注解
        if (this.needGenAnnotation(annotations,PluginConstants.SCF_SERIALIZABLE_TOTAL_PACKAGE_NAME)) {
            AnnotationWriteCommandAction annotationWriteCommandAction =
                    new AnnotationWriteCommandAction(project,psiClass,PluginConstants.ANNOTATION_NAME_SCF_SERIALIZABLE,AnnotationWriteCommandAction.ELEMENT_CLASS,psiFile);
            annotationWriteCommandAction.execute();
            //并导入SCFSerializable包
            ImportWriteCommandAction importWriteCommandAction = new ImportWriteCommandAction(project,psiClass,PluginConstants.SCF_SERIALIZABLE_TOTAL_PACKAGE_NAME,psiFile);
            importWriteCommandAction.execute();
        }
    }

    /**
     * 查找ScfMember注解
     *
     * @param psiFieldAnnotations
     * @return
     */
    private PsiAnnotation findScfMemberAnnotation(PsiAnnotation[] psiFieldAnnotations) {
        if (psiFieldAnnotations == null || psiFieldAnnotations.length == 0) {
            return null;
        }
        for (int i = 0; i < psiFieldAnnotations.length; i++) {
            PsiAnnotation psiAnnotation = psiFieldAnnotations[i];
            if (Objects.equals(psiAnnotation.getQualifiedName(),PluginConstants.SCF_MEMBER_TOTAL_PACKAGE_NAME)) {
                return psiAnnotation;
            }
        }
        return null;
    }

    /**
     * 判断是否需要为目标对象生成注解
     *
     * @param annotations
     * @param targetAnnotationName
     * @return
     */
    private boolean needGenAnnotation(PsiAnnotation[] annotations,String targetAnnotationName) {
        if (annotations == null || annotations.length == 0) {
            return true;
        }
        for (int i = 0; i < annotations.length; i++) {
            PsiAnnotation psiAnnotation = annotations[i];
            if (Objects.equals(psiAnnotation.getQualifiedName(),targetAnnotationName)) {
                return false;
            }
        }
        return true;
    }
}
