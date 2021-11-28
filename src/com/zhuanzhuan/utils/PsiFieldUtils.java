package com.zhuanzhuan.utils;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtil;

import java.util.Objects;

/**
 * @Description: idea字段处理工具类
 * @author: xiexing01
 * @date: 2021/11/28 12:37
 */
public final class PsiFieldUtils {

    private PsiFieldUtils(){}

    /**
     * 判断当前字段是否为泛型
     *
     * @param psiField
     * @return
     */
    public static boolean isGeneric(PsiField psiField,boolean currentPsiClassTypeIsGeneric) {
        if (!currentPsiClassTypeIsGeneric || psiField == null) {
            return false;
        }
        PsiType psiType = psiField.getType();
        PsiClass resolveClassInType = PsiUtil.resolveClassInType(psiType);
        if (resolveClassInType instanceof PsiTypeParameter) {
            return true;
        }
        if (Objects.isNull(resolveClassInType)) {
            return false;
        }
        PsiClassType psiClassType = PsiTypesUtil.getClassType(resolveClassInType);
        return psiClassType.isRaw();
    }
}
