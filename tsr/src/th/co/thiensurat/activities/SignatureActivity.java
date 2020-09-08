package th.co.thiensurat.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;

import th.co.bighead.utilities.BHActivity;
import th.co.thiensurat.R;
public class SignatureActivity extends Activity {

    private static Context context;
    private String empid;
    private File signPath;

    private String key;
    private Bitmap bitmap;
    private Bitmap bitmap2;
    private String contractNo;
    private File mergeSignPath;
    private File customerSignPath;
    private File installationPath;
//    private ImageConfiguration imageConfiguration;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

//    public static SignatureActivity getsInstance() {
//        return new SignatureActivity();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
        getWindow().setLayout(((WindowManager.LayoutParams) params).WRAP_CONTENT, ((WindowManager.LayoutParams) params).WRAP_CONTENT);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_signature);
        setUpView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            return false;
        }
        return true;
    }

    Button buttonSave;
    Button buttonCancel;
    Button buttonClear;
    SignaturePad signaturePad;
    private void setUpView() {
        signaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        buttonSave = (Button) findViewById(R.id.getsign);
        buttonCancel = (Button) findViewById(R.id.cancel);
        buttonClear = (Button) findViewById(R.id.clear);

        verifyStoragePermissions(this);
        buttonSave.setOnClickListener( onSign() );
        buttonCancel.setOnClickListener( onCancel() );
        buttonClear.setOnClickListener( onClear() );
        getDataFromIntent();
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                buttonSave.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buttonSave.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.hold_payment_0));
                }
            }

            @Override
            public void onClear() {

            }
        });
    }

    private void getDataFromIntent() {
        contractNo = getIntent().getStringExtra("CONTRACT_NUMBER");

        try {
            customerSignPath = new File(getAlbumStorageDir(contractNo), String.format("signature_%s.jpg", contractNo));
            if (customerSignPath.exists()) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(customerSignPath.getAbsolutePath(), bmOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth() + 100), bitmap.getHeight(), true);
                signaturePad.setSignatureBitmap(bitmap);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buttonSave.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.dot_gray_dark));
                }
            }
        } catch (Exception ex) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                buttonSave.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.dot_gray_dark));
            }
        }
    }

    private View.OnClickListener onClear() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
                buttonSave.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buttonSave.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.dot_gray_dark));
                }

                File fileimage = null;
                fileimage = new File(customerSignPath.getAbsolutePath());
                if (fileimage.exists()) {
                    if (fileimage.delete()) {

                    }
                }
            }
        };
    }

    private View.OnClickListener onSign() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                    Toast.makeText(SignatureActivity.this, "บันทึกลายเซ็นแล้ว", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignatureActivity.this, "ไม่สามารถบันทึกลายเซ็นได้", Toast.LENGTH_SHORT).show();
                }

                setResult(RESULT_OK);
                finish();
            }
        };
    }

    private View.OnClickListener onCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity the activity from which permissions are checked
     */
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir(contractNo), String.format("Signature_%s.jpg", contractNo));
            saveBitmapToJPG(signature, photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap((bitmap.getWidth() + 100), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

//    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
//        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(newBitmap);
//        canvas.drawColor(Color.WHITE);
//        canvas.drawBitmap(bitmap, 0, 0, null);
//        OutputStream stream = new FileOutputStream(photo);
//        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        stream.close();
//
//        try {
//            AssetManager assetManager = this.getAssets();
//            bitmap = BitmapFactory.decodeStream(assetManager.open("k_viruch2.png"), null, null);
//            imageConfiguration.createSingleImageFromMultipleImages(bitmap, imageConfiguration.getResizedBitmap(newBitmap, 250, 60), mergeSignPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public File getAlbumStorageDir(String albumName) {
        File file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        }
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
}
