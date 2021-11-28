package com.zhuanzhuan.write;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @Description: 节点删除执行类
 * @author: xiexing01
 * @date: 2021/11/28 11:23
 */
public class ElementDeleteCommandAction extends WriteCommandAction {

    /**
     *需要删除的节点
     */
    private PsiElement psiElement;

    public ElementDeleteCommandAction(@Nullable Project project, @NotNull PsiElement psiElement, @NotNull PsiFile... files) {
        super(project, files);
        this.psiElement = psiElement;
    }

    @Override
    protected void run(@NotNull Result result) throws Throwable {
        psiElement.delete();
    }
}
