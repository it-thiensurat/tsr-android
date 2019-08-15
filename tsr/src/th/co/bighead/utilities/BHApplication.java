package th.co.bighead.utilities;

//import org.acra.ACRA;
//import org.acra.annotation.ReportsCrashes;
//import org.acra.sender.HttpSender.Type;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

// Development
//@ReportsCrashes(formKey = "", formUri = "http://192.168.1.243/sit/crashreport.ashx", reportType = Type.JSON)
//@ReportsCrashes(formKey = "", formUri = "http://bigheadcreative.dyndns.org:24380/sit/crashreport.ashx", reportType = Type.JSON)


// UAT
@ReportsCrashes(formKey = "", formUri = "http://203.144.154.17/bighead/crashreport.ashx", reportType = HttpSender.Type.JSON)

// Production
//@ReportsCrashes(formKey = "", formUri = "http://110.170.185.76/bighead/crashreport.ashx", reportType = Type.JSON)

public class BHApplication extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
		BHApplication.context = getApplicationContext();
		FirebaseApp.initializeApp(this);
	}

	public static Context getContext() {
		return BHApplication.context;
	}
}
