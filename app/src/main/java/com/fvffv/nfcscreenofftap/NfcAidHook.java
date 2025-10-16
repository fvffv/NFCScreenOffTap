package com.fvffv.nfcscreenofftap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NfcAidHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // 过滤NFC相关包（通常为系统NFC包，如com.android.nfc）
        if (!lpparam.packageName.equals("com.android.nfc")) {
            return;
        }
        try {

            XposedHelpers.findAndHookMethod(
                    "com.android.nfc.ScreenStateHelper",
                    lpparam.classLoader,
                    "checkScreenState",
                    boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            // 强制返回 ON_UNLOCKED（8），绕过息屏限制
                            param.setResult(8);
                        }
                    }
            );

        } catch (Throwable e) {
            // 打印Hook失败日志（可通过LSPosed日志查看）
            e.printStackTrace();
        }
    }
}
