package com.gagan.networkswitcher;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SubscriptionManager;
import java.lang.reflect.Method;
import rikka.shizuku.ShizukuBinderWrapper;
import rikka.shizuku.SystemServiceHelper;

public final class NetworkController {
    private NetworkController() {}
    private static final long PREFERRED_4G = 392191L;
    private static final long PREFERRED_5G = 916479L;

    public static boolean setPreferred4G(Context context) throws Exception { return setMask(context, PREFERRED_4G); }
    public static boolean setPreferred5G(Context context) throws Exception { return setMask(context, PREFERRED_5G); }

    private static boolean setMask(Context context, long mask) throws Exception {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) throw new UnsupportedOperationException("Android 12+ is required for this build.");
        if (!ShizukuCheck()) throw new IllegalStateException("Shizuku is not running or permission is not granted.");

        int subId = SubscriptionManager.getDefaultDataSubscriptionId();
        if (subId < 0) throw new IllegalStateException("No default data SIM was found.");

        org.lsposed.hiddenapibypass.HiddenApiBypass.addHiddenApiExemptions("Lcom/android/internal/telephony/");
        IBinder raw = SystemServiceHelper.getSystemService("phone");
        if (raw == null || !raw.pingBinder()) throw new IllegalStateException("Phone service unavailable.");

        IBinder binder = new ShizukuBinderWrapper(raw);
        Class<?> stub = Class.forName("com.android.internal.telephony.ITelephony$Stub");
        Object phone = stub.getDeclaredMethod("asInterface", IBinder.class).invoke(null, binder);
        Class<?> api = Class.forName("com.android.internal.telephony.ITelephony");

        Method method = null;
        for (Method m : api.getDeclaredMethods()) {
            if (!m.getName().equals("setAllowedNetworkTypesForReason")) continue;
            Class<?>[] p = m.getParameterTypes();
            if (p.length == 3 && p[0] == int.class && p[1] == int.class && p[2] == long.class) { method = m; break; }
            if (p.length == 4 && p[0] == int.class && p[1] == int.class && p[2] == long.class && p[3] == String.class) { method = m; break; }
        }
        if (method == null) throw new NoSuchMethodException("setAllowedNetworkTypesForReason");
        method.setAccessible(true);
        Object result = method.getParameterCount() == 3
                ? method.invoke(phone, subId, 0, mask)
                : method.invoke(phone, subId, 0, mask, context.getPackageName());
        return !(result instanceof Boolean) || (Boolean) result;
    }

    private static boolean ShizukuCheck() {
        return rikka.shizuku.Shizuku.pingBinder()
                && rikka.shizuku.Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED;
    }
}
