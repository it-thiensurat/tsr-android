//package th.co.thiensurat.fragments.sales;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.util.Date;
//
//import th.co.bighead.utilities.BHFragment;
//import th.co.bighead.utilities.BHPreference;
//import th.co.bighead.utilities.BHStorage;
//import th.co.bighead.utilities.BHStorage.FolderType;
//import th.co.bighead.utilities.annotation.InjectView;
//import th.co.thiensurat.R;
//import th.co.thiensurat.business.controller.BackgroundProcess;
//import th.co.thiensurat.business.controller.TSRController;
//import th.co.thiensurat.data.controller.DatabaseHelper;
//import th.co.thiensurat.data.info.ContractImageInfo;
//import th.co.thiensurat.data.info.ContractInfo;
//import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.Toast;
//
//public class SalePhotographyFragment extends BHFragment {
//
//	private String STATUS_CODE = "11";
//	private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
//	private static final int MEDIA_TYPE_IMAGE = 1;
//	private static final String IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
//	private Uri fileUri;
//	GPSTracker gps;
//	private static String Parth;
//	private static String namePic;
//	private ContractInfo contract;
//	private String Image;
//
//	@InjectView
//	private Button buttonCard, buttonInstall, buttonAddressNumber, buttonHome, buttonNearbyPlaces, buttonCheckPicture, buttonMapInstall;
//
//	@Override
//	protected int titleID() {
//		// TODO Auto-generated method stub
//		return R.string.title_sales;
//	}
//
//	@Override
//	protected int fragmentID() {
//		// TODO Auto-generated method stub
//		return R.layout.fragment_sale_photography;
//
//	}
//
//	@Override
//	protected int[] processButtons() {
//		// TODO Auto-generated method stub
//		return new int[] { R.string.button_more };
//	}
//
//	@Override
//	protected void onCreateViewSuccess(Bundle savedInstanceState) {
//		if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
//			saveStatusCode();
//		}
//		FolderType F = BHStorage.FolderType.Picture;
//		Parth = BHStorage.getFolder(F);
//		GetContract();
//
//		buttonCard.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				namePic = "ภาพบัตรประชาชน";
//				captureImage();
//			}
//		});
//
//		buttonInstall.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				namePic = "ภาพเครื่องที่ติดตั้ง";
//				captureImage();
//			}
//		});
//
//		buttonAddressNumber.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				namePic = "ภาพบ้านเลขที่";
//				captureImage();
//			}
//		});
//
//		buttonHome.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				namePic = "ภาพที่อยู่อาศัย";
//				captureImage();
//			}
//		});
//
//		buttonNearbyPlaces.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				namePic = "ภาพสถานที่ใกล้เคียง";
//				captureImage();
//			}
//		});
//
//		buttonMapInstall.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				gps = new GPSTracker(getActivity());
//				if (gps.canGetLocation()) {
//					double latitude = gps.getLatitude();
//					double longitude = gps.getLongitude();
//					Uri uri = Uri.parse("geo:" + latitude + "," + longitude + "?z100");
//					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//					startActivity(Intent.createChooser(intent, "View map with"));
//					showMessage("latitude:" + latitude + "   longitude:" + longitude);
//				}
//			}
//		});
//		buttonCheckPicture.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showNextView(new SaleCheckPhotographyFragment());
//			}
//		});
//	}
//
//	private void GetContract() {
//		// TODO Auto-generated method stub
//		(new BackgroundProcess(activity) {
//
//			@Override
//			protected void calling() {
//				// TODO Auto-generated method stub
//				contract = getContract(BHPreference.RefNo());
//			}
//		}).start();
//	}
//
//	private void AddContractImage() {
//		// TODO Auto-generated method stub
//		(new BackgroundProcess(activity) {
//			ContractImageInfo input = new ContractImageInfo();
//
//			@Override
//			protected void before() {
//				// TODO Auto-generated method stub
//
//				File imageFile = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + namePic);
//				Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//				ByteArrayOutputStream stream = new ByteArrayOutputStream();
//				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//				byte[] image = stream.toByteArray();
//				String img_str = Base64.encodeToString(image, 0);
//
//				Image = DatabaseHelper.getUUID();
//
//				input.ImageID = Image;
//				input.RefNo = BHPreference.RefNo();
//				input.ImageName = namePic + "_" + BHPreference.RefNo() + ".jpg";
//				input.ImageTypeCode = "0";
//				input.SyncedDate = new Date();
//				input.ImageData = img_str;
//			}
//
//			@Override
//			protected void calling() {
//				// TODO Auto-generated method stub
//				addContractImage(input);
//			}
//		}).start();
//	}
//
//	private void saveStatusCode() {
//		TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
//
//		(new BackgroundProcess(activity) {
//
//			@Override
//			protected void calling() {
//				// TODO Auto-generated method stub
//				contract.StatusCode = STATUS_CODE;
//				updateContract(contract);
//			}
//		}).start();
//	}
//
//	private void captureImage() {
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		outState.putParcelable("file_uri", fileUri);
//	}
//
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onSaveInstanceState(savedInstanceState);
//		fileUri = savedInstanceState.getParcelable("file_uri");
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//			if (resultCode == Activity.RESULT_OK) {
//				AddContractImage();
//			} else if (resultCode == Activity.RESULT_CANCELED) {
//				Toast.makeText(getActivity(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
//			} else {
//				Toast.makeText(getActivity(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
//			}
//		}
//	}
//
//	public Uri getOutputMediaFileUri(int type) {
//		return Uri.fromFile(getOutputMediaFile(type));
//	}
//
//	private static File getOutputMediaFile(int type) {
//		File mediaStorageDir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME);
//		if (!mediaStorageDir.exists()) {
//			if (!mediaStorageDir.mkdirs()) {
//				Log.d(IMAGE_DIRECTORY_NAME, "" + IMAGE_DIRECTORY_NAME + " directory");
//				return null;
//			}
//		}
//		File mediaFile;
//		if (type == MEDIA_TYPE_IMAGE) {
//			mediaFile = new File(mediaStorageDir.getPath() + File.separator + namePic + ".jpg");
//		} else {
//			return null;
//		}
//		return mediaFile;
//	}
//
//	@Override
//	public void onProcessButtonClicked(int buttonID) {
//		// TODO Auto-generated method stub
//		switch (buttonID) {
//		case R.string.button_more:
//			showNextView(new SaleMoreDetailAddress());
//			break;
//		case R.string.button_back:
//			showLastView();
//			break;
//		default:
//			break;
//		}
//	}
//}

package th.co.thiensurat.fragments.sales;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.BitmapCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHStorage.FolderType;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.bighead.utilities.save_image_to_gallery;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.CameraActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractImageController.ImageType;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.ImageTypeController;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.retrofit.api.Service;

import static org.acra.ACRA.LOG_TAG;
import static org.acra.ACRA.getACRASharedPreferences;
import static th.co.thiensurat.retrofit.api.client.BASE_URL;
import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class SalePhotographyFragment extends BHFragment {



    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    private String STATUS_CODE = "11";
    private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static String IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
    private Uri fileUri,fileUri2;
    private static String Parth;
    private static String imageTypeCode;
    private GPSTracker gps;
    private static String imageID;

    /*** [START] Fixed - [BHPROJ-0024-686] ***/
//    @InjectView private ImageView imgGps;
    @InjectView private ImageView imgGpsAddressInstall;
    @InjectView private ImageView imgGpsAddressPayment;
    /*** [END] Fixed - [BHPROJ-0024-686] ***/

    @InjectView private ImageView imgHome;
    @InjectView private ImageView imgId;
    @InjectView private ImageView imgProduct;

    //-- Fixed - [BHPROJ-0020-932]
    @InjectView private ImageView imgCardOrOther;

    @InjectView private LinearLayout linearLayoutHeadNumber;
    @InjectView private TextView txtNumber1;
    @InjectView private TextView txtNumber2;
    @InjectView private TextView txtNumber3;
    @InjectView private TextView txtNumber4;
    @InjectView private TextView txtNumber5;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            return R.string.title_edit_customer_details;
        } else {
            return R.string.title_sales;
        }
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_photography;

    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            return new int[]{R.string.button_back, R.string.button_cheak_photo};
        } else {
            return new int[]{R.string.button_more, R.string.button_cheak_photo};

        }

    }

    @Override
    public String fragmentTag() {
        // TODO Auto-generated method stub
        return SalePhotographyMapFragment.FRAGMENT_SALE_PHOTOGRAPHY_MAP;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            linearLayoutHeadNumber.setVisibility(View.GONE);
        } else {
            saveStatusCode();
        }

        if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            txtNumber1.setText("...");
            txtNumber2.setText("8");
            txtNumber3.setText("9");
            txtNumber4.setText("10");
            txtNumber5.setText("11");
            txtNumber4.setBackgroundResource(R.drawable.circle_number_sale_color_red);
        }

        FolderType F = BHStorage.FolderType.Picture;
        Parth = BHStorage.getFolder(F);

        imgHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTypeCode = ImageType.ADDRESS.toString();
                imageID = DatabaseHelper.getUUID();
                captureImage();
            }
        });

        imgId.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTypeCode = ImageType.IDCARD.toString();
                imageID = DatabaseHelper.getUUID();
                captureImage();
            }
        });

        imgProduct.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTypeCode = ImageType.PRODUCT.toString();
                imageID = DatabaseHelper.getUUID();
                captureImage();
            }
        });


        /*** [START] Fixed - [BHPROJ-0020-932] ***/
        imgCardOrOther.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTypeCode = ImageType.PAYMENTCARD.toString();
                imageID = DatabaseHelper.getUUID();
                captureImage();
            }
        });
        /*** [END] Fixed - [BHPROJ-0020-932] ***/

        /*** [START] Fixed - [BHPROJ-0024-686] + [BHPROJ-0024-678] ***/

        /*
        imgGps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextView(new SalePhotographyMapFragment());

                // gps = new GPSTracker(getActivity());
                // if (gps.canGetLocation()) {
                // double latitude = gps.getLatitude();
                // double longitude = gps.getLongitude();
                // Uri uri = Uri.parse("geo:" + latitude + "," + longitude +
                // "?z100");
                // Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // startActivity(Intent.createChooser(intent, "View map with"));
                // showMessage("latitude:" + latitude + "   longitude:" +
                // longitude);
                // }
            }
        });
        */

        imgGpsAddressInstall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*** [START] :: Permission ***/
//                showNextView(new SalePhotographyMapFragment());
                /*SalePhotographyMapFragment.Data data1 = new SalePhotographyMapFragment.Data();
                data1.imageTypeCode = ImageType.MAP.toString();
                data1.title = titleID();
                SalePhotographyMapFragment fm = BHFragment.newInstance(SalePhotographyMapFragment.class, data1);
                showNextView(fm);*/

                new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

                    @Override
                    public void onSuccess(BHPermissions bhPermissions) {
                        SalePhotographyMapFragment.Data data1 = new SalePhotographyMapFragment.Data();
                        data1.imageTypeCode = ImageType.MAP.toString();
                        data1.title = titleID();
                        SalePhotographyMapFragment fm = BHFragment.newInstance(SalePhotographyMapFragment.class, data1);
                        showNextView(fm);
                    }

                    @Override
                    public void onNotSuccess(BHPermissions bhPermissions) {
                        bhPermissions.openAppSettings(getActivity());
                    }

                    @Override
                    public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
                        bhPermissions.showMessage(getActivity(), permissionType);
                    }

                }, BHPermissions.PermissionType.LOCATION);
                /*** [END] :: Permission ***/
            }
        });
        imgGpsAddressPayment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*** [START] :: Permission ***/
//                showNextView(new SalePhotographyMapFragment());
                /*SalePhotographyMapFragment.Data data1 = new SalePhotographyMapFragment.Data();
                data1.imageTypeCode = ImageType.MAPPAYMENT.toString();
                data1.title = titleID();
                SalePhotographyMapFragment fm = BHFragment.newInstance(SalePhotographyMapFragment.class, data1);
                showNextView(fm);*/

                imageTypeCode = ImageType.MAPPAYMENT.toString();
                imageID = DatabaseHelper.getUUID();
                captureImage();

//                new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {
//
//                    @Override
//                    public void onSuccess(BHPermissions bhPermissions) {
//                        SalePhotographyMapFragment.Data data1 = new SalePhotographyMapFragment.Data();
//                        data1.imageTypeCode = ImageType.MAPPAYMENT.toString();
//                        data1.title = titleID();
//                        SalePhotographyMapFragment fm = BHFragment.newInstance(SalePhotographyMapFragment.class, data1);
//                        showNextView(fm);
//                    }
//
//                    @Override
//                    public void onNotSuccess(BHPermissions bhPermissions) {
//                        bhPermissions.openAppSettings(getActivity());
//                    }
//
//                    @Override
//                    public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
//                        bhPermissions.showMessage(getActivity(), permissionType);
//                    }
//
//                }, BHPermissions.PermissionType.LOCATION);
                /*** [END] :: Permission ***/
            }
        });

        /*** [END] Fixed - [BHPROJ-0024-686] + [BHPROJ-0024-678] ***/

    }

    private void saveStatusCode() {
        TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
    }

    private void captureImage() {

        /*** [START] :: Permission ***/
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/

//        Intent intent = new Intent(getActivity(), CameraActivity.class);
//        intent.putExtra("DIR_NAME", IMAGE_DIRECTORY_NAME);
//        intent.putExtra("IMAGE_NAME", imageID);
//        intent.putExtra("IMAGE_TYPE", imageTypeCode);
//        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

            @Override
            public void onSuccess(BHPermissions bhPermissions) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                Uri photoURI = FileProvider.getUriForFile(getActivity(),getActivity().getApplicationContext().getPackageName() + ".provider", new File(fileUri.getPath()));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }

            @Override
            public void onNotSuccess(BHPermissions bhPermissions) {
                bhPermissions.openAppSettings(getActivity());
            }

            @Override
            public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
                bhPermissions.showMessage(getActivity(), permissionType);
            }

        }, BHPermissions.PermissionType.CAMERA,BHPermissions.PermissionType.STORAGE);
        /*** [END] :: Permission ***/

//        new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {
//
//            @Override
//            public void onSuccess(BHPermissions bhPermissions) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//                Uri photoURI = FileProvider.getUriForFile(getActivity(),getActivity().getApplicationContext().getPackageName() + ".provider", new File(fileUri.getPath()));
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//            }
//
//            @Override
//            public void onNotSuccess(BHPermissions bhPermissions) {
//                bhPermissions.openAppSettings(getActivity());
//            }
//
//            @Override
//            public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
//                bhPermissions.showMessage(getActivity(), permissionType);
//            }
//
//        }, BHPermissions.PermissionType.CAMERA);

    }
    private File createImageFile() throws IOException {
        File outputDir = activity.getBaseContext().getCacheDir();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "photo_" + timeStamp + "_";
        File image = new File(outputDir, imageFileName);

        return image;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                AddContractImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);
//       File mediaStorageDir = new File("/sdcard/Android/data/"+activity.getApplicationContext().getPackageName()+"/files/pictures/"  + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
              //  Log.d(IMAGE_DIRECTORY_NAME, "" + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageID + ".jpg");
        } else {
            return null;
        }
        return mediaFile ;

    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

String DD="",NAME_IMAGE="",IMAGE_TYPE="";
    private void AddContractImage() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            ContractImageInfo input = new ContractImageInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub
                input.ImageID = imageID;
                input.RefNo = BHPreference.RefNo();
                input.ImageName = imageID + ".jpg";
                input.ImageTypeCode = imageTypeCode;
                input.SyncedDate = new Date();
//                DD = String.format("%s/%s/%s/%s", Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY_NAME, imageTypeCode, input.ImageName);
                DD="/sdcard/Android/data/"+activity.getApplicationContext().getPackageName()+"/files/pictures/"+IMAGE_DIRECTORY_NAME + "/"+ input.ImageTypeCode+ "/" +input.ImageName;
                NAME_IMAGE=input.ImageName;
                IMAGE_TYPE=input.ImageTypeCode;

                checkPermissions();
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                addContractImage(input, true);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
            }
        }).start();
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(activity, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }

                File file21 = new File(DD);
                String filePath = file21.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                save_image_to_gallery.getResizedBiBitmaptmap(bitmap,NAME_IMAGE,IMAGE_TYPE);
             //  getResizedBiBitmaptmap(bitmap,NAME_IMAGE,IMAGE_TYPE);
                break;
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_more:
                showNextView(new SaleMoreDetailAddress());
                break;
            case R.string.button_cheak_photo:
                showNextView(new SaleListPhotoFragment());
//                getImageContract();
                break;
            case R.string.button_back:
                showLastView();
                break;
//            case R.string.button_send_image:
//                getImageContract();
//                break;
            default:
                break;
        }
    }

//    List<ContractImageInfo> contractImageInfoList = null;
//    public void getImageContract() {
//        (new BackgroundProcess(activity) {
//
//            @Override
//            protected void before() {
//                contractImageInfoList = new ArrayList<ContractImageInfo>();
//            }
//
//            @Override
//            protected void calling() {
//                contractImageInfoList = TSRController.getContractImageByRefno(BHPreference.RefNo());
//            }
//
//            @Override
//            protected void after() {
//                uploadToServer();
//                Log.e("Image list", String.valueOf(contractImageInfoList));
//            }
//        }).start();
//    }
//
//    public void uploadToServer() {
//        List<MultipartBody.Part> parts = new ArrayList<>();
//        MultipartBody.Part[] imageList = new MultipartBody.Part[contractImageInfoList.size()];
//        if (contractImageInfoList != null && contractImageInfoList.size() > 0) {
//            for (int i = 0; i < contractImageInfoList.size(); i++) {
//                ContractImageInfo info = contractImageInfoList.get(i);
//                File imageFile = new File(Parth + "/" + info.RefNo + "/" + info.ImageTypeCode + "/" + info.ImageName);
//                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
//                parts.add(MultipartBody.Part.createFormData("files[]", info.ImageTypeCode + "_" + info.ImageName, imageBody));
//            }
//        }
//
//        RequestBody refnoBody = RequestBody.create(MediaType.parse("text/plain"), BHPreference.RefNo());
//
//        try {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(GIS_BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            Service request = retrofit.create(Service.class);
//            Call call = request.uploadImageToServer(parts, refnoBody);
//            call.enqueue(new Callback() {
//                @Override
//                public void onResponse(Call call, retrofit2.Response response) {
//                    Gson gson=new Gson();
//                    try {
//                        Log.e("GSON response", String.valueOf(gson.toJson(response.body())));
//                        JSONObject jsonObject=new JSONObject(gson.toJson(response.body()));
//                        Log.e("onResponse jsonObject", String.valueOf(jsonObject));
//                        JSONArray array = jsonObject.getJSONArray("data");
//                        Log.e("onResponse jsonArray", String.valueOf(array));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.e("onResponse",e.getLocalizedMessage());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call call, Throwable t) {
//                    Log.e("onFailure",t.getLocalizedMessage());
//                }
//            });
//
//        } catch (Exception e) {
//            Log.e("Exception",e.getLocalizedMessage());
//        }
//    }
//
//    public static RequestBody createRequestBody(@NonNull File file){
//        return RequestBody.create(MediaType.parse("multipart/form-data"),file);
//    }
}
