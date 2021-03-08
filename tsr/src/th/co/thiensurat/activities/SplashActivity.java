package th.co.thiensurat.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import th.co.bighead.utilities.BHActivity;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.BuildConfig;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.service.data.PlatformVersionInputInfo;
import th.co.thiensurat.service.data.PlatformVersionOutputInfo;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BHActivity {

	private ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (!BuildConfig.DEBUG) {
			Fabric.with(this, new Crashlytics());
			/*final Fabric fabric = new Fabric.Builder(this)
					.kits(new Crashlytics())
					.debuggable(true)
					.build();
			Fabric.with(fabric);*/
		}

		setContentView(R.layout.activity_splash);

		// If a notification message is tapped, any data accompanying the notification
		// message is available in the intent extras. In this sample the launcher
		// intent is fired when the notification is tapped, so any accompanying data would
		// be handled here. If you want a different intent fired, set the click_action
		// field of the notification message to the desired intent. The launcher intent
		// is used when no click_action is specified.
		//
		// Handle possible data accompanying notification message.
		// [START handle_data_extras]
		if (getIntent().getExtras() != null) {
			for (String key : getIntent().getExtras().keySet()) {
				Object value = getIntent().getExtras().get(key);
				Log.d("MyFirebaseMsgService", "Key: " + key + " Value: " + value);
			}
		}
		// [END handle_data_extras]

		imageView = (ImageView) findViewById(R.id.img);

//		ObjectAnimator fadeOut = ObjectAnimator.ofFloat(imageView, "alpha",  1f, .0f);
//		fadeOut.setDuration(500);
		ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", .0f, 1f);
		fadeIn.setDuration(1500);

		final AnimatorSet mAnimationSet = new AnimatorSet();
		mAnimationSet.play(fadeIn);

		mAnimationSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				final Handler handler = new Handler();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Intent intent;

								/*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
								if ((BHPreference.userID() == null) || !BHPreference.serviceMode().equals(BHGeneral.SERVICE_MODE.toString()) || (BHPreference.serviceMode() == null)) {
									//intent = new Intent(getApplicationContext(), LoginActivity.class);
									CheckAppVersion();
								} else {
									intent = new Intent(getApplicationContext(), MainActivity.class);
									startActivity(intent);
									finish();
								}
								/*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
							}
						});

					}
				}).start();
			}
		});
		mAnimationSet.start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		final Handler handler = new Handler();
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				handler.post(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						Intent intent;
//
//						/*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
//						if ((BHPreference.userID() == null) || !BHPreference.serviceMode().equals(BHGeneral.SERVICE_MODE.toString()) || (BHPreference.serviceMode() == null)) {
//							//intent = new Intent(getApplicationContext(), LoginActivity.class);
//							CheckAppVersion();
//						} else {
//							intent = new Intent(getApplicationContext(), MainActivity.class);
//							startActivity(intent);
//							finish();
//						}
//						/*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
//					}
//				});
//
//			}
//		}).start();
	}

	/*private class CheckAppVersion extends AsyncTask<String, Integer, Boolean> {

		protected Boolean doInBackground(String... url) {
			try {
				try {
					PackageManager pm = getPackageManager();
					PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);

					String curVersion = packageInfo.versionName;
					String newVersion = curVersion;
					newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageInfo.packageName)
							.timeout(3000)
							.get()
							.select("div[itemprop=softwareVersion]")
							.first()
							.ownText();

					if(!curVersion.equals(newVersion)){
						return true;
					} else {
						return false;
					}
				} catch (PackageManager.NameNotFoundException e) {
					Log.i("CheckAppVersion", e.getMessage());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		protected void onPostExecute(Boolean result) {
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			intent.putExtra("IsUpdate", result);
			startActivity(intent);
			finish();
		}
	}*/

	private void CheckAppVersion() {

		(new BackgroundProcess(this) {

			private PlatformVersionInputInfo input;
			private PlatformVersionOutputInfo result;

			@Override
			protected void before() {
				input = new PlatformVersionInputInfo();
				input.CurrentVersionName = BHPreference.appVersionName();
			}

			@Override
			protected void calling() {
				try {
					result = TSRController.ValidateVersion(input);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			protected void after() {
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				intent.putExtra("PlatformVersionOutputInfo", result);
				startActivity(intent);
				finish();
			}
		}).start();
	}
}
