package com.zhuanzhuan.write;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.zhuanzhuan.consts.PluginConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Description: 导包执行类
 * @author: xiexing01
 * @date: 2021/11/28 11:10
 */
public class ImportWriteCommandAction extends WriteCommandAction {

    /**
     * 需要导入的类
     */
    private PsiClass psiClass;

    /**
     * 需要导入的包的全例名
     */
    private String totalPackageName;

    public ImportWriteCommandAction(@Nullable Project project,@NotNull PsiClass psiClass,@NotNull String totalPackageName, @NotNull PsiFile... files) {
        super(project, files);
        this.psiClass = psiClass;
        this.totalPackageName = totalPackageName;
    }

    @Override
    protected void run(@NotNull Result result) throws Throwable {
        //判断当前类是否已经导入该包
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiClass.getContainingFile();
        //当前工程
        Project project = psiClass.getProject();
        // 根据类的全限定名查询PsiClass，下面这个方法全局搜
        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(totalPackageName, GlobalSearchScope.allScope(project));
        if (psiClass == null) {
            throw new RuntimeException(String.format("%s cannot find dependency of %s, please check",PluginConstants.PLUGIN_NAME,totalPackageName));
        }
        PsiImportStatement singleClassImportStatement = psiJavaFile.getImportList().findSingleClassImportStatement(psiClass.getQualifiedName());
        if (singleClassImportStatement != null) {
            //已经导入该类无需重复导入
            return;
        }
        PsiElementFactory psiElementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
        PsiImportStatement psiImportStatement = psiElementFactory.createImportStatement(psiClass);
        psiJavaFile.getImportList().add(psiImportStatement);
    }
}
