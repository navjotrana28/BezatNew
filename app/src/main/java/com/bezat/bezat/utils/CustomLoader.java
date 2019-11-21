package com.bezat.bezat.utils;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomLoader extends Dialog {
    public CustomLoader(@NonNull Context context) {
        super(context);
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    public CustomLoader(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomLoader(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
