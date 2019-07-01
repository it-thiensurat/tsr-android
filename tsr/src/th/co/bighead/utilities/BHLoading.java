package th.co.bighead.utilities;

import th.co.thiensurat.R;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import java.util.concurrent.atomic.AtomicInteger;

public class BHLoading extends Dialog {

    private static BHLoading dialog;
    private AtomicInteger counter = new AtomicInteger();

	public static BHLoading show(Context context) {
		return show(context, false, null);
	}

	private static BHLoading show(Context context, boolean cancelable) {
		return show(context, cancelable, null);
	}

	private static synchronized BHLoading show(Context context, boolean cancelable, OnCancelListener cancelListener) {
        if (dialog == null) {
            dialog = new BHLoading(context);
            dialog.counter.incrementAndGet();
            dialog.setCancelable(cancelable);
            dialog.setOnCancelListener(cancelListener);
            dialog.addContentView(new ProgressBar(context), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            dialog.show();
        }

        return dialog;
	}

	private BHLoading(Context context) {
		super(context, R.style.BHDialog);
	}

    public static synchronized void close() {
        if (dialog != null && dialog.isShowing() && dialog.counter.decrementAndGet() == 0) {
            dialog.dismiss();
            dialog = null;
        }
    }

//    public static synchronized void dismiss() {
//        if (!isDismiss) {
//            isDismiss = true;
//            super.dismiss();
//        }
//    }
}