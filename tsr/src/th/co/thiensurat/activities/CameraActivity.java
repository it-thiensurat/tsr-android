package th.co.thiensurat.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;

//import jpegkit.Jpeg;
//import jpegkit.JpegImageView;
import th.co.thiensurat.R;

//import com.jpegkit.Jpeg;
//import com.jpegkit.JpegImageView;

public class CameraActivity extends AppCompatActivity {

//    private JpegImageView imageView;
    private ImageView imageView;
    private CameraKitView cameraKitView;
    private RelativeLayout layoutPreview;
    private FloatingActionButton takePhoto, applyPhoto;
    private ImageButton cancelPhoto;

    private String photoURI;
    private String imageID;
    private String dirName;
    private String typeName;

    private File fileUri;
    private byte[] fileStream;
    private Bitmap bitmap = null;
    private Boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraKitView   = findViewById(R.id.camera);
        takePhoto       = findViewById(R.id.photoButton);
        applyPhoto      = findViewById(R.id.applyImage);
        cancelPhoto      = findViewById(R.id.cancelImage);
        imageView       = findViewById(R.id.imagePreview);
        layoutPreview   = findViewById(R.id.layoutPreview);

        Intent intent = getIntent();
        imageID = intent.getExtras().getString("IMAGE_NAME");
        dirName = intent.getExtras().getString("DIR_NAME");
        typeName = intent.getExtras().getString("IMAGE_TYPE");
//        photoURI = intent.getExtras().getString("IMAGE_URL");
//        fileUri = (File)intent.getSerializableExtra("IMAGE_FILE");
//        Toast.makeText(getApplicationContext(), "File path: " + photoURI.getPath(), Toast.LENGTH_SHORT).show();
        cameraKitView.setPermissionsListener(new CameraKitView.PermissionsListener() {
            @Override
            public void onPermissionsSuccess() {

            }

            @Override
            public void onPermissionsFailure() {
                cameraKitView.requestPermissions(CameraActivity.this);
            }
        });

        cameraKitView.setCameraListener(new CameraKitView.CameraListener() {
            @Override
            public void onOpened() {
                Log.v("CameraKitView", "CameraListener: onOpened()");
            }

            @Override
            public void onClosed() {
                Log.v("CameraKitView", "CameraListener: onClosed()");
            }
        });

        cameraKitView.setPreviewListener(new CameraKitView.PreviewListener() {
            @Override
            public void onStart() {
                Log.v("CameraKitView", "PreviewListener: onStart()");
            }

            @Override
            public void onStop() {
                Log.v("CameraKitView", "PreviewListener: onStop()");
            }
        });


        applyPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    File direct = new File(String.format("%s/%s/%s",Environment.getExternalStorageDirectory(), dirName, typeName));
                    if (!direct.exists()) {
                        direct.mkdirs();
                    }

                    File savedPhoto = new File(direct.getAbsolutePath(), imageID + ".jpg");
                    if (savedPhoto.exists()) {
                        savedPhoto.delete();
                    }

                    FileOutputStream outputStream = new FileOutputStream(savedPhoto);
                    bitmap = BitmapFactory.decodeByteArray(fileStream, 0, fileStream.length);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }).start();
                } catch (java.io.IOException e){
                    Log.e("Take photo", e.getLocalizedMessage());
                    Log.e("DEMO", "Not saved");
                }
            }
        });

        cancelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutPreview.setVisibility(View.INVISIBLE);
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Take photo", String.valueOf("take take take"));
                cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fileStream = bytes;
                                        layoutPreview.setVisibility(View.VISIBLE);
                                        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        DisplayMetrics dm = new DisplayMetrics();
                                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                                        imageView.setMinimumHeight(dm.heightPixels);
                                        imageView.setMinimumWidth(dm.widthPixels);
                                        imageView.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}