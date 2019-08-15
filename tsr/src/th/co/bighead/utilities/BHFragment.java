package th.co.bighead.utilities;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public abstract class BHFragment extends Fragment {
	protected final static String BH_FRAGMENT_DEFAULT_DATA = "th.co.bighead.data.default";
	private final static String BH_FRAGMENT_INPUT_DATA = "th.co.bighead.data.input";

	protected BHFragmentCallback fragmentCallback = null;

	protected static MainActivity activity;

	protected abstract int fragmentID();

	protected abstract int[] processButtons();

	protected int titleID() {
		return 0;
	}

	public String fragmentTag() {
		return null;
	}

	public String backToFragmentTag() {
		return null;
	}

	protected void onFragmentResult(BHParcelable data) {
	}

    private Exception exception = null;

	public boolean bOrentation=false;
    public boolean enableLandscape() {
        return bOrentation;
    }

	protected abstract void onCreateViewSuccess(Bundle savedInstanceState);

	protected <T extends BHParcelable> T getData() {
		return getData(BH_FRAGMENT_INPUT_DATA);
	}

	private <T extends BHParcelable> T getData(String dataKey) {
		Bundle args = this.getArguments();
		if (args != null) {
			return args.getParcelable(dataKey);
		}

		return null;
	}

	protected void showNextView(BHFragment fragment) {

		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH);
//		if (month < 5)
//		{
			activity.showNextView(fragment);
//		} else {
//			activity.showMessage("Application can not be use at this time, Please contact Administrator");
//		}

	}

	protected void showLastView() {
		showLastView(null, null);
	}

	protected void showLastView(String tag) {
		showLastView(tag, null);
	}

	protected void showLastView(BHParcelable data) {
		showLastView(null, data);
	}

	protected void showLastView(String tag, BHParcelable data) {
		activity.showLastView(tag, data);

		if (data != null) {
			setResult(data);
		}
	}

	public void setResult(BHParcelable data) {
		if (fragmentCallback != null) {
			fragmentCallback.onResult(data);
		}
	}

	protected static void showMessage(int resID) {
		activity.showMessage(resID);
	}

	protected static void showMessage(String message) {
		activity.showMessage(message);
	}

	protected static void showDialog(final String title, final String message) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(activity);
		setupAlert.setTitle(title);
		setupAlert.setMessage(message);
		setupAlert.show();
	}

	protected void log(String message) {
		if (!TextUtils.isEmpty(message)) {
			log("BIGHEAD", message);
		}
	}

	protected void log(String tag, String message) {
		Log.d(tag, message);
	}

	protected void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(
				activity.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void onProcessButtonClicked(int buttonID) {
	}

	public static <T extends BHFragment> T newInstance(Class<T> cls) {
		return newInstance(cls, null, null);
	}

	public static <T extends BHFragment> T newInstance(Class<T> cls,
			BHParcelable data) {
		return newInstance(cls, data, null);
	}

	public static <T extends BHFragment> T newInstance(Class<T> cls,
			BHFragmentCallback callback) {
		return newInstance(cls, null, callback);
	}

	public static <T extends BHFragment> T newInstance(Class<T> cls,
			BHParcelable data, BHFragmentCallback callback) {
		try {
			BHFragment fragment = cls.newInstance();
			fragment.fragmentCallback = callback;

			if (data != null) {
				Bundle args = new Bundle();
				args.putParcelable(BH_FRAGMENT_INPUT_DATA, data);
				fragment.setArguments(args);
			}

			return (T) fragment;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            throw new RuntimeException(e);
		}
	}

	public static void setActivity(Activity activity) {
		BHFragment.activity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		try {
			Log.d("BHView", getClass().getName());
		} catch (Exception ex) {}


		if (!BHPagerFragment.class.isAssignableFrom(getClass())) {
			activity.setupProcessButtons(this, processButtons());
			activity.setupTitle(titleID());
		}

		View vw = inflater.inflate(fragmentID(), container, false);

		bindControls(vw);

        if (exception != null) {
            showWarningDialog(exception);
        } else {
            onCreateViewSuccess(savedInstanceState);

            if (activity.isFragmentBackStack()) {
                onFragmentResult(activity.getFragmentResult());
            }
        }

		return vw;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		activity.setRequestedOrientation(enableLandscape() ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //activity.setRequestedOrientation(enableLandscape() ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	}

	private void bindControls(View vw) {
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			InjectView iv = field.getAnnotation(InjectView.class);
			if (iv != null) {
				field.setAccessible(true);
				String name = field.getName();
				try {
					if (iv.value() >= 0) {
						field.set(this, vw.findViewById(iv.value()));
					} else {
						Field r;
						r = R.id.class.getDeclaredField(name);
						int id = r.getInt(null);
						field.set(this, vw.findViewById(id));
					}
				} catch (Exception ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
                    this.exception = ex;
				}
			}
		}
	}

	protected static abstract class BHFragmentCallback {
		public abstract void onResult(BHParcelable data);
	}

	public static void showWarningDialog(final String title, final String message) {
        activity.showWarningDialog(title, message);
	}

    public static void showWarningDialog(final Exception ex)
    {
        activity.showWarningDialog(ex);
    }
}
