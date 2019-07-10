package th.co.thiensurat.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BHActivity {

	private Context context;
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

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		context = this;
//		final Handler handler = new Handler();
//		// ProgressDialog dialog = ProgressDialog.show(getApplicationContext(),
//		// "TEST", "Loading...");
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
//						//ปิดก่อนงานรีบ
//						/*if ((BHPreference.userID() == null) || !BHPreference.serviceMode().equals(BHGeneral.SERVICE_MODE.toString()) || (BHPreference.serviceMode() == null)) {
//							//intent = new Intent(getApplicationContext(), LoginActivity.class);
//							new CheckAppVersion().execute();
//						} else {
//							intent = new Intent(getApplicationContext(), MainActivity.class);
//							startActivity(intent);
//							finish();
//						}*/
//						/*if ((BHPreference.userID() == null) || !BHPreference.serviceMode().equals(BHGeneral.SERVICE_MODE.toString()) || (BHPreference.serviceMode() == null)) {
//							intent = new Intent(getApplicationContext(), LoginActivity.class);
//							//new CheckAppVersion().execute();
//						} else {
//							intent = new Intent(getApplicationContext(), MainActivity.class);
//						}
//						startActivity(intent);
//						finish();*/
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
//
//
//						//startActivity(intent);
//						//finish();
//					}
//				});
//
//			}
//		}).start();

		try {
			currentVersion = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			currentversion = Integer.parseInt(currentVersion.substring(Math.max(currentVersion.length() - 2, 0)));
			Log.e("Current Version","::"+ currentVersion.substring(Math.max(currentVersion.length() - 2, 0)));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		new FetchAppVersionFromGooglePlayStore().execute();
	}

	String newVersion;
	String currentVersion="";

	private int newversion;
	private int currentversion;
	class FetchAppVersionFromGooglePlayStore extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			String version = "";
			try {
				//return //https://play.google.com/store/apps/details?id=th.co.thiensurat
				Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "th.co.thiensurat" + "&hl=en")
								.timeout(10000)
								.userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
								.referrer("http://www.google.com")
								.get();
				if (document != null) {
					Elements element = document.getElementsContainingOwnText("Current Version");
					for (Element ele : element) {
						if (ele.siblingElements() != null) {
							Elements sibElemets = ele.siblingElements();
							for (Element sibElemet : sibElemets) {
								version = sibElemet.text();
							}
						}
					}
				}

			} catch (Exception e) {
				return "";
			}
			return version;
		}

		protected void onPostExecute(String string) {
			newVersion = string;
			newversion = Integer.parseInt(newVersion.substring(Math.max(newVersion.length() - 2, 0)));
			Log.e("new Version", newVersion.substring(Math.max(newVersion.length() - 2, 0)));
//			Toast.makeText(SplashActivity.this, "new Version: " + newVersion, Toast.LENGTH_LONG).show();
			if (newVersion != null && !newVersion.isEmpty()) {
				if (newversion > currentversion) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
					alertDialogBuilder.setTitle("มีเวอร์ชั่นใหม่!");
					alertDialogBuilder.setIcon(R.drawable.ic_launcher);
					alertDialogBuilder
							.setMessage("แอพฯ ของคุณเก่าเกินไป กรุณาอัพเดท")
							.setCancelable(false)
							.setPositiveButton("อัพเดท",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									try {
										startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
									} catch (android.content.ActivityNotFoundException anfe) {
										startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
									}
								}
							})
							.setNegativeButton("ไม่ ขอบคุณ",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.dismiss();
									finish();
								}
							});

					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				} else {
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
									if ((BHPreference.userID() == null) || !BHPreference.serviceMode().equals(BHGeneral.SERVICE_MODE.toString()) || (BHPreference.serviceMode() == null)) {
										intent = new Intent(getApplicationContext(), LoginActivity.class);
										startActivity(intent);
										finish();
									} else {
										intent = new Intent(getApplicationContext(), MainActivity.class);
										startActivity(intent);
										finish();
									}
								}
							});

						}
					}).start();
				}

//				if (newversion == currentversion) {
//
//				} else {
//
//				}
			}
		}
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
