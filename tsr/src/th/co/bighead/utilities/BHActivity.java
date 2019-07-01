package th.co.bighead.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import th.co.thiensurat.BuildConfig;
import th.co.thiensurat.R;

public abstract class BHActivity extends Activity implements UncaughtExceptionHandler {

	private static TextView tvDetail = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		boolean show = false;

		if (BHGeneral.DEVELOPER_MODE && show) {
			if (BHActivity.tvDetail == null) {
				tvDetail = new TextView(getApplicationContext());
				tvDetail.setBackgroundColor(Color.TRANSPARENT);
				tvDetail.setTextColor(Color.RED);
				tvDetail.setText(showDeviceDetail(getApplicationContext()));
			}

			WindowManager.LayoutParams params = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_TOAST, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
							| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);
			params.gravity = Gravity.TOP | Gravity.RIGHT;
			// final WindowManager mWindowManager =
			// (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

			getWindowManager().addView(tvDetail, params);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (BHActivity.tvDetail != null) {
			getWindowManager().removeView(tvDetail);
		}
	}

	protected void showMessage(int resID) {
		showMessage(getResources().getString(resID));
	}

	protected void showMessage(final String message) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(BHActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private String showDeviceDetail(Context context) {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		int config = context.getResources().getConfiguration().screenLayout;
		int screenLayout = config & Configuration.SCREENLAYOUT_SIZE_MASK;
		int screenLong = config & Configuration.SCREENLAYOUT_LONG_MASK;

		// View content = getWindow().findViewById(android.R.id.content);

		String layout = "";
		switch (screenLayout) {
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			layout = "small";
			break;

		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			layout = "normal";
			break;

		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			layout = "large";
			break;

		case Configuration.SCREENLAYOUT_SIZE_XLARGE:
			layout = "xlarge";
			break;

		default:
			layout = "undefined";
			break;
		}

		String wide = "";
		switch (screenLong) {
		case Configuration.SCREENLAYOUT_LONG_YES:
			wide = "wide screen";
			break;

		case Configuration.SCREENLAYOUT_LONG_NO:
			wide = "normal screen";
			break;

		default:
			wide = "undefined screen";
			break;
		}

		return String.format("%s - %s - %dx%d", layout, wide, width, height);
		// return layout;
	}

	protected void showWarningDialog(String title, String message) {
		final Builder setupAlert;
		setupAlert = new AlertDialog.Builder(this);
		setupAlert.setTitle(title);
		setupAlert.setMessage(message);
		setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setupAlert.show();
			}
		});
	}

    protected void showWarningDialog(final Exception ex)
    {
        Exception e = null;
        if (ex instanceof RuntimeException) {
			e = (Exception)ex.getCause();

        } else {
            e = ex;
        }

        if (e instanceof SocketTimeoutException || e instanceof ConnectException || e instanceof UnknownHostException) {
            showWarningDialog("Network Error", "Can't connect to server!\n\nPlease check your internet connection.");
        } else {
			if(ex.getMessage().equals(getResources().getString(R.string.error_synch))){
				showWarningDialog("เกิดข้อผิดพลาดระหว่างการปรับปรุงข้อมูล", ex.getMessage());
			} else {
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));

				showWarningDialog("Uncaught Exception", errors.toString());
				if (!BuildConfig.DEBUG) {
					Crashlytics.logException(ex);
				}
			}


        }
    }

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		
		ex.printStackTrace();
        showWarningDialog((Exception)ex);

	}

}
