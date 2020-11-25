package th.co.thiensurat.fragments.sales;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHBitmap;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHStorage.FolderType;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.CameraActivity;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TransactionService;

public class SaleCheckPhotographyFragment extends BHFragment {

    private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static String IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
    private Uri fileUri;
    private static String Parth;
    private static String imageTypeCode;
    private List<String> ImageList;
    private File folder;
    private static String imageID;

    private static final int PICK_IMAGE = 1;

    @InjectView
    private LinearLayout linearLayoutHeadNumber;
    @InjectView
    private TextView txtNumber1;
    @InjectView
    private TextView txtNumber2;
    @InjectView
    private TextView txtNumber3;
    @InjectView
    private TextView txtNumber4;
    @InjectView
    private TextView txtNumber5;
    @InjectView
    private GridView gridView;
    @InjectView
    TextView tvImageTitle;

    public static class Data extends BHParcelable {
        public String TypeCode;
        public int title;
    }

    private Data data;

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());


    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        if (data == null) {
            data = getData();
        }

        return data.title;
        //return R.string.title_sales;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_check_photography;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        /*** [START] :: Fixed - [BHPROJ-0026-747] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/

        /*if (data == null) {
            data = getData();
        }

        int[] pButtons;

        if (isCredit) {
            switch (Enum.valueOf(ContractImageController.ImageType.class, data.TypeCode)) {
                case MAPPAYMENT:
                    pButtons = new int[]{R.string.button_back, R.string.button_import_image};
                    break;
                default:
                    pButtons = new int[]{R.string.button_back};
                    break;
            }
        } else {
            pButtons = new int[]{R.string.button_back, R.string.button_delete_image, R.string.button_import_image};
        }

        return pButtons;*/

        return  new int[]{R.string.button_back, R.string.button_delete_image, R.string.button_import_image};

        /*** [END] :: Fixed - [BHPROJ-0026-747] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        IMAGE_DIRECTORY_NAME = BHPreference.RefNo();

        if (isCredit || BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            linearLayoutHeadNumber.setVisibility(View.GONE);
        } else {
            linearLayoutHeadNumber.setVisibility(View.VISIBLE);
        }

        // TODO Auto-generated method stub
        txtNumber1.setText("...");
        txtNumber2.setText("8");
        txtNumber3.setText("9");
        txtNumber4.setText("10");
        txtNumber5.setText("11");
        txtNumber4.setBackgroundResource(R.drawable.circle_number_sale_color_red);

        data = getData();
        imageTypeCode = data.TypeCode;
        CheckImageTitle(imageTypeCode);

        /*FolderType F = BHStorage.FolderType.Picture;
        Parth = BHStorage.getFolder(F);
        folder = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);

        GetImage();*/

        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                FolderType F = BHStorage.FolderType.Picture;
                Parth = BHStorage.getFolder(F);
                folder = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                GetImage();
            }
        }).start();

    }

    private void CheckImageTitle(String imageTypeCode) {
        // TODO Auto-generated method stub
        switch ( ContractImageController.ImageType.valueOf(ContractImageController.ImageType.class, imageTypeCode)){
            case IDCARD:
                tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_idcard));
                break;
            case PRODUCT:
                tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_product));
                break;
            case ADDRESS:
                tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_address));
                break;
            case MAP:
                tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_map));
                break;
            case MAPPAYMENT:
                tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_map_payment));
                break;
            /*case LOSS:
                break;
            case CUSTOMER:
                break;
            case IMPOUNDPRODUCT:
                break;
            case CHANGEPRODUCT:
                break;
            case SALEAUDIT:
                break;*/
            case PAYMENTCARD:
                tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_payment_card));
                break;
        }

        /*if (imageTypeCode.equals(ImageType.IDCARD.toString())) {
            tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_idcard));
        } else if (imageTypeCode.equals(ImageType.PRODUCT.toString())) {
            tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_product));
        } else if (imageTypeCode.equals(ImageType.ADDRESS.toString())) {
            tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_address));
        }
        *//*** [START] Fixed - [BHPROJ-0024-678] ***//*
        else if (imageTypeCode.equals(ImageType.MAP.toString())) {
            tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_map));
        } else if (imageTypeCode.equals(ImageType.MAPPAYMENT.toString())) {
            tvImageTitle.setText(getResources().getString(R.string.label_sale_photo_map_payment));
        }
        *//*** [END] Fixed - [BHPROJ-0024-678] ***/
    }

    private void GetImage() {
        // TODO Auto-generated method stub
        /*boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        if (success) {
            ImageList = getSD();
            gridView.setAdapter(new ImageAdapter(getActivity(), ImageList));
        } else {
            showMessage("ไม่พบภาพถ่าย");

        }*/
        //gridView.setAdapter(null);
        ImageList = new ArrayList<String>();
        List<ContractImageInfo> contractImageList = TSRController.getContractImageList(BHPreference.RefNo(), data.TypeCode);
        if (contractImageList != null && contractImageList.size() > 0) {
            for (ContractImageInfo info : contractImageList) {
                File imageFile = new File(Parth + "/" + info.RefNo + "/" + info.ImageTypeCode + "/" + info.ImageName);
                if (!imageFile.exists()) {

                    /*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
                    //ImageList.add(String.format("%s%s", BHPreference.TSR_IMAGE_URL, info.ImageName));
                    ImageList.add(String.format("%s%s", BHPreference.TSR_IMAGE_URL, info.ImageID));
                    /*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

                } else {
                    ImageList.add(null);
                }
            }

            String[] strArgs = new String[ImageList.size()];
            strArgs = ImageList.toArray(strArgs);

            new LoadImageToGridView(contractImageList, gridView).execute(strArgs);
        } else {
            List<String> strList = new ArrayList<String>();
            gridView.setAdapter(new ImageAdapter(getActivity(), strList));

            showMessage("ไม่พบภาพถ่าย");

        }
    }

    public class LoadImageToGridView extends AsyncTask<String, String, Boolean> {
        boolean isConnecting;
        ProgressDialog pDialog;

        List<ContractImageInfo> contractImageInfo;
        GridView gridView;

        public LoadImageToGridView(List<ContractImageInfo> contractImageInfo, GridView gridView) {
            this.contractImageInfo = contractImageInfo;
            this.gridView = gridView;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.activity);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setTitle("Connecting To Server");
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }

        protected Boolean doInBackground(String... args) {

            try {
                isConnecting = true;
                int timeOutImage = 2000;//TimeOut 2s
                for (int i = 0; i < args.length; i++) {
                    if (args[i] != null) {
                        HttpGet httpGet = new HttpGet(args[i]);
                        HttpClient client = new DefaultHttpClient();

                        HttpParams params = client.getParams();
                        HttpConnectionParams.setConnectionTimeout(params, timeOutImage);
                        HttpConnectionParams.setSoTimeout(params, timeOutImage);

                        HttpResponse response = client.execute(httpGet);

                        int statusCode = response.getStatusLine().getStatusCode();

                        Bitmap image;
                        if (statusCode == 200) {
                            Log.e("Image url", String.valueOf(args[i]));
                            image = BitmapFactory.decodeStream((InputStream) new URL(args[i]).getContent());

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] b = stream.toByteArray();
                            String temp = Base64.encodeToString(b, Base64.DEFAULT);

                            contractImageInfo.get(i).ImageData = temp;
                        } else {
                            isConnecting = false;

                            image = BitmapFactory.decodeResource(activity.getResources(), R.drawable.no_image);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] b = stream.toByteArray();
                            String temp = Base64.encodeToString(b, Base64.DEFAULT);

                            contractImageInfo.get(i).ImageData = temp;
                        }
                    } else {

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return isConnecting;
        }

        protected void onPostExecute(Boolean isCon) {

            List<String> strList = new ArrayList<String>();

            for (ContractImageInfo info : contractImageInfo) {
                File imageFile = new File(Parth + "/" + info.RefNo + "/" + info.ImageTypeCode + "/" + info.ImageName);
                strList.add(imageFile.getPath());

                if (info.ImageData != null) {
                    TSRController.saveContractImage(info);
                }
            }

            gridView.setAdapter(new ImageAdapter(getActivity(), strList));
            pDialog.dismiss();

            /*if (!isCon) {
                AlertDialog.Builder setupAlert;
                setupAlert = new AlertDialog.Builder(activity)
                        .setTitle("Connecting To Server")
                        .setMessage("เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้")
                        .setCancelable(false)
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                setupAlert.show();
            }*/
        }
    }

    private List<String> getSD() {
        List<String> it = new ArrayList<String>();
        File f = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            Log.d("Count", file.getPath());
            it.add(file.getPath());
        }
        return it;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private List<String> lis;

        private List<Bitmap> bitmapList;
        public List<Boolean> isCheckList;
        public List<String> fileNameList;


        public ImageAdapter(Context c, List<String> li) {
            // TODO Auto-generated method stub
            context = c;
            lis = li;

            bitmapList = new ArrayList<>();
            isCheckList = new ArrayList<>();
            fileNameList = new ArrayList<>();
            for (String str : lis) {
                bitmapList.add(null);
                isCheckList.add(false);

                String fileName = str.substring(str.lastIndexOf('/') + 1, str.length());
                fileNameList.add(fileName);
            }
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lis.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gridview_check_photography, null);
            }

            String strPath = lis.get(position);

            Bitmap tempBitmap = bitmapList.get(position);
            boolean isCheck = isCheckList.get(position);
            String fileName = fileNameList.get(position);

            // Image Resource
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

            if (tempBitmap != null) {
                imageView.setImageBitmap(tempBitmap);
            } else {
                /*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                //String part = fileUri.getPath().toString();*/

                (new BackgroundProcess(activity) {
                    Bitmap bm;
                    Bitmap bitmap;

                    @Override
                    protected void before() {
                        imageView.setImageBitmap(null);
                    }

                    @Override
                    protected void calling() {
                        bm = BHBitmap.decodeSampledBitmapFromImagePath(strPath, 500);
                        bitmap = BHBitmap.setRotateImageFromImagePath(strPath, bm);
                    }

                    @Override
                    protected void after() {
                        imageView.setImageBitmap(bitmap);
                        bitmapList.set(position, bitmap);
                    }
                }).start(false);

            }

            imageView.setTag(lis.get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*** [START] :: FileProvider ***/
                    /*String part = (String) view.getTag();

                    final File file = new File(part);
                    Uri uri = Uri.fromFile(new File(part));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String mimeType = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(MimeTypeMap
                                    .getFileExtensionFromUrl(file.getPath()));
                    intent.setDataAndType(uri, mimeType);
                    startActivity(Intent.createChooser(intent, "Open file with"));*/


                    String part = (String) view.getTag();

                    File file = new File(part);
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.getPath()));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri apkURI = FileProvider.getUriForFile(getActivity(),getActivity().getApplicationContext().getPackageName() + ".provider", file);
                    intent.setDataAndType(apkURI, mimeType);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Open file with"));

                    /*** [END] :: FileProvider ***/
                }

            });

            CheckBox ch = (CheckBox) convertView.findViewById(R.id.checkBox);

            /*** [START] :: Fixed - [BHPROJ-0026-747] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/
            /*if (isCredit) {
                ch.setVisibility(View.GONE);
            } else {
                ch.setVisibility(View.VISIBLE);
            }*/
            /*** [END] :: Fixed - [BHPROJ-0026-747] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/

            ch.setChecked(isCheck);
            ch.setTag(fileName);
            ch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox checkbox = (CheckBox) view;
                    isCheckList.set(position, checkbox.isChecked());
                }
            });
            return convertView;
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_import_image:

                switch (Enum.valueOf(ContractImageController.ImageType.class, data.TypeCode)) {
                    case MAP:
                    case MAPPAYMENT:
                        /*** [START] :: Permission ***/
                        //showNextView(new SalePhotographyMapFragment());
                        /*SalePhotographyMapFragment.Data data1 = new SalePhotographyMapFragment.Data();
                        data1.imageTypeCode = data.TypeCode;
                        data1.title = titleID();
                        SalePhotographyMapFragment fm = BHFragment.newInstance(SalePhotographyMapFragment.class, data1);
                        showNextView(fm);*/

                        new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

                            @Override
                            public void onSuccess(BHPermissions bhPermissions) {
                                SalePhotographyMapFragment.Data data1 = new SalePhotographyMapFragment.Data();
                                data1.imageTypeCode = data.TypeCode;
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

                        break;
                    default:
                        imageID = DatabaseHelper.getUUID();
                        captureImage();
                        break;
                }
                break;
            case R.string.button_delete_image:
                startSync();
                /*int count = gridView.getAdapter().getCount();
                for (int i = 0; i < count; i++) {
                    LinearLayout itemLayout = (LinearLayout) gridView.getChildAt(i);
                    CheckBox checkbox = (CheckBox) itemLayout.findViewById(R.id.checkBox);
                    if (checkbox.isChecked()) {
                        String checkboxnameimage = checkbox.getTag().toString();
                        DeleteImage(checkboxnameimage);
                    }
                }*/
                break;
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }

    private void DeleteImage(final String checkboxnameimage) {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                deleteContractImage(checkboxnameimage.replaceAll("\\..*", ""));
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                File file = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode + "/" + checkboxnameimage);
                boolean deleted = file.delete();
                GetImage();
            }
        }).start();
    }

    private void captureImage() {

        /*** [START] :: Permission ***/
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/

        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra("DIR_NAME", IMAGE_DIRECTORY_NAME);
        intent.putExtra("IMAGE_NAME", imageID);
        intent.putExtra("IMAGE_TYPE", imageTypeCode);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

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
        /*** [END] :: Permission ***/

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

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + imageTypeCode);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "" + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageID + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

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
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                addContractImage(input, true);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                GetImage();
            }
        }).start();
    }

    private class SynchronizeReceiver extends BroadcastReceiver {
        private SynchronizeReceiver instance;

        private ProgressDialog dialog;
        private SynchronizeService.SynchronizeResult result;
        private boolean isProcessing;

        private SynchronizeReceiver() {
            dialog = new ProgressDialog(activity);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setTitle("");
            dialog.setMessage("");

            result = null;
            isProcessing = false;
        }

        public SynchronizeReceiver getInstance() {
            if (instance == null) {
                instance = new SynchronizeReceiver();
                LocalBroadcastManager.getInstance(activity).registerReceiver(instance, new IntentFilter(SynchronizeService.SYNCHRONIZE_BROADCAST_ACTION));
            }

            return instance;
        }

        public void show() {
            if (isProcessing && !dialog.isShowing()) {
                dialog.show();
            }
        }

        private void start() {
            if (!isProcessing) {
                isProcessing = true;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        }

        private void stop() {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(this);
            isProcessing = false;
            dialog = null;
            result = null;
            instance = null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            if (intent != null) {
                result = intent.getParcelableExtra(SynchronizeService.SYNCHRONIZE_RESULT_DATA_KEY);
                dialog.setTitle(result.title);
                dialog.setMessage(result.message);
                dialog.setProgress(result.progress);

                if (result.progress >= 100) {
                    dialog.setProgress(100);
                }

                if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED || result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                    dialog.dismiss();
                    if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED) {
                        int count = gridView.getAdapter().getCount();
                        ImageAdapter imgAdapter = (ImageAdapter) gridView.getAdapter();

                        for (int i = 0; i < count; i++) {
                           /* LinearLayout itemLayout = (LinearLayout) gridView.getChildAt(i);
                            CheckBox checkbox = (CheckBox) itemLayout.findViewById(R.id.checkBox);
                            if (checkbox.isChecked()) {
                                String checkboxnameimage = checkbox.getTag().toString();
                                DeleteImage(checkboxnameimage);
                            }*/

                            if (imgAdapter.isCheckList.get(i)) {
                                String checkboxnameimage = imgAdapter.fileNameList.get(i);
                                DeleteImage(checkboxnameimage);
                            }
                        }
                    }
                    if (result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                        showWarningDialog(result.error);

                    }
                    stop();
                }

            }
        }

    }

    private void startSync() {
        TransactionService.stopService(activity);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        //new BaseController().removeDatabase();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
        /*request.master.syncTeamRelated = true;
        request.master.syncProductRelated = true;
        request.master.syncCustomerRelated = true;
        request.master.syncPaymentRelated = true;
        request.master.syncContractRelated = true;
        request.master.syncEditContractRelated = true;
        request.master.syncSendMoneyRelated = true;
        request.master.syncMasterDataRelated = true;*/

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);
    }
}
