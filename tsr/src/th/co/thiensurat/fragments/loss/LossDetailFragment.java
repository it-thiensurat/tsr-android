package th.co.thiensurat.fragments.loss;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHBitmap;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHStorage.FolderType;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.AssignController.AssignTaskType;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.ContractImageController.ImageType;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.FortnightController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.ProblemInfo.ProblemType;
import th.co.thiensurat.data.info.WriteOffNPLInfo;
import th.co.thiensurat.data.info.WriteOffNPLInfo.WriteOffNPLStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LossDetailFragment extends BHFragment {

    public static String FRAGMENT_LOSS = "th.co.thiensurat.fragments.loss";
    private int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private final int MEDIA_TYPE_IMAGE = 1;
    private final String IMAGE_DIRECTORY_NAME = BHPreference.RefNo();
    private Uri fileUri;
    private String Parth;
    private String imageID;

    @InjectView
    private TextView txtCode, txtProduct, txtNo, txtName, txtIDCardNo, txtAddress, txtTel, txtDataInstall, txtSale, txtFortnight;
    @InjectView
    private EditText etMore, etDate;
    @InjectView
    ImageButton imageButtonWriteoffDate;
    @InjectView
    private Spinner spnProblem;
    @InjectView
    private ImageView imageViewLoss;

    public static class Data extends BHParcelable {
        public String Serialnumber, Name;
    }

    private Data dataLoss;

    private List<ProblemInfo> problemList;
    private ProblemInfo selectedProblem = null;
    private String Date;
    private Calendar myCal = Calendar.getInstance();
    private ContractInfo contractInfo = null;
    private WriteOffNPLInfo writeOffNPLInfo = null;

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_loss_detail;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back, R.string.button_ok};
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        super.onProcessButtonClicked(buttonID);
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_ok:
                final String title = "กรุณาตรวจสอบข้อมูล";
                String message = "";
                Builder setupAlert;

                if (spnProblem.getSelectedItemId() == spnProblem.getCount() - 1 && etMore.getText().toString().trim().equals("")) {
                    message = "กรุณากรอกสาเหตุการตัดหนี้สูญอื่น ๆ";
                    setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                            .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                } else if (etDate.getText().toString().trim().equals("")) {
                    message = "กรุณาเลือกวันที่";
                    setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                            .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            });
                } else {
                    message = "ต้องการตัดเป็นหนี้สูญหรือไม่ (R)?";
                    setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                            .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    (new BackgroundProcess(activity) {

                                        @Override
                                        protected void before() {
                                            // TODO Auto-generated method stub
                                            writeOffNPLInfo = new WriteOffNPLInfo();
                                            writeOffNPLInfo.WriteOffNPLID = DatabaseHelper.getUUID();
                                            writeOffNPLInfo.OrganizationCode = contractInfo.OrganizationCode;
                                            writeOffNPLInfo.RefNo = contractInfo.RefNo;
                                            writeOffNPLInfo.Status = WriteOffNPLStatus.REQUEST.toString();
                                            writeOffNPLInfo.RequestProblemID = selectedProblem.ProblemID;// spnProblem.getSelectedItem().toString();
                                            if (spnProblem.getSelectedItemId() == spnProblem.getCount() - 1) {
                                                writeOffNPLInfo.RequestDetail = etMore.getText().toString();
                                            }
                                            writeOffNPLInfo.RequestDate = myCal.getTime();
                                            writeOffNPLInfo.RequestBy = BHPreference.employeeID();
                                            writeOffNPLInfo.RequestTeamCode = BHPreference.teamCode();
                                            writeOffNPLInfo.ApprovedDate = new Date();
                                            writeOffNPLInfo.EffectiveDate = new Date();
                                            writeOffNPLInfo.CreateBy = BHPreference.employeeID();
                                            writeOffNPLInfo.CreateDate = new Date();
                                            writeOffNPLInfo.LastUpdateBy = BHPreference.employeeID();
                                            writeOffNPLInfo.LastUpdateDate = new Date();
                                            writeOffNPLInfo.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                                        }

                                        @Override
                                        protected void calling() {
                                            // TODO Auto-generated method stub
                                            addRequestLoss(writeOffNPLInfo, true);
                                        }

                                        @Override
                                        protected void after() {
                                            // TODO Auto-generated method stub
                                            (new BackgroundProcess(activity) {

                                                AssignInfo assignInfo = new AssignInfo();

                                                @Override
                                                protected void before() {
                                                    // TODO Auto-generated method
                                                    // stub

                                                    writeOffNPLInfo.Status = WriteOffNPLStatus.APPROVED.toString();
                                                    writeOffNPLInfo.ApproveDetail = writeOffNPLInfo.RequestDetail;
                                                    writeOffNPLInfo.ApprovedDate = myCal.getTime();
                                                    writeOffNPLInfo.ApprovedBy = writeOffNPLInfo.RequestBy;
                                                    writeOffNPLInfo.EffectiveEmployeeLevelPath = writeOffNPLInfo.RequestEmployeeLevelPath;

                                                    writeOffNPLInfo.RequestBy = null;
                                                    writeOffNPLInfo.RequestDate = null;
                                                    writeOffNPLInfo.RequestDetail = null;
                                                    writeOffNPLInfo.RequestProblemID = null;
//												writeOffNPLInfo.RequestEmployeeLevelPath = null;

                                                    assignInfo.AssignID = DatabaseHelper.getUUID();
                                                    assignInfo.TaskType = AssignTaskType.WriteOffNPL.toString();
                                                    assignInfo.OrganizationCode = contractInfo.OrganizationCode;
                                                    assignInfo.RefNo = contractInfo.RefNo;
                                                    assignInfo.AssigneeEmpID = BHPreference.employeeID();
                                                    assignInfo.AssigneeTeamCode = BHPreference.teamCode();
                                                    assignInfo.ReferenceID = writeOffNPLInfo.WriteOffNPLID;
                                                    assignInfo.CreateBy = assignInfo.AssigneeEmpID;
                                                    assignInfo.CreateDate = new Date();
                                                    assignInfo.LastUpdateBy = assignInfo.AssigneeEmpID;
                                                    assignInfo.LastUpdateDate = new Date();
                                                }

                                                @Override
                                                protected void calling() {
                                                    // TODO Auto-generated method
                                                    // stub
                                                    approveLoss(writeOffNPLInfo, assignInfo, true);
                                                }

                                                @Override
                                                protected void after() {
                                                    // TODO Auto-generated method
                                                    // stub
                                                    (new BackgroundProcess(activity) {

                                                        @Override
                                                        protected void before() {
                                                            // TODO Auto-generated
                                                            // method stub

                                                            writeOffNPLInfo.Status = WriteOffNPLStatus.COMPLETED.toString();
                                                            writeOffNPLInfo.ResultProblemID = selectedProblem.ProblemID;// spnProblem.getSelectedItem().toString();
                                                            writeOffNPLInfo.ResultDetail = writeOffNPLInfo.ApproveDetail;
                                                            writeOffNPLInfo.EffectiveDate = myCal.getTime();
                                                            writeOffNPLInfo.EffectiveBy = writeOffNPLInfo.ApprovedBy;
                                                            writeOffNPLInfo.WriteOffNPLPaperID = writeOffNPLInfo.WriteOffNPLID;

                                                            writeOffNPLInfo.ApproveDetail = null;
                                                            writeOffNPLInfo.ApprovedDate = null;
                                                            writeOffNPLInfo.ApprovedBy = null;
                                                        }

                                                        @Override
                                                        protected void calling() {
                                                            // TODO Auto-generated
                                                            // method stub
                                                            actionLoss(writeOffNPLInfo, true);
                                                            ContractInfo contract = getContract(contractInfo.RefNo);
                                                            if (contract != null) {
                                                                contract.isActive = false;
                                                                contract.STATUS = ContractStatus.R.toString();
                                                                contract.LastUpdateDate = writeOffNPLInfo.EffectiveDate;
                                                                contract.LastUpdateBy = BHPreference.employeeID();
                                                                updateContract(contract, true);
                                                            }
                                                        }

                                                        @Override
                                                        protected void after() {
                                                            // TODO Auto-generated
                                                            // method stub
                                                            showLastView();
                                                        }
                                                    }).start();
                                                }
                                            }).start();
                                        }
                                    }).start();

                                }
                            }).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // just do nothing
                                }
                            });
                }
                setupAlert.show();

                break;
            default:
                break;
        }
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_loss;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        dataLoss = getData();
        txtCode.setText(dataLoss != null && dataLoss.Serialnumber != null ? dataLoss.Serialnumber : "-");
        txtProduct.setText(dataLoss != null && dataLoss.Name != null ? dataLoss.Name : "-");

        getProblem();
        DateControlInit();
        etDate.setText(BHUtilities.dateFormat(myCal.getTime(), "dd/MM/yyyy"));
        etDate.setEnabled(false);
        etDate.setKeyListener(null);

        imageID = DatabaseHelper.getUUID();
        if(BHGeneral.DEVELOPER_MODE){
            showMessage(BHPreference.RefNo());
        }

        imageViewLoss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderType F = BHStorage.FolderType.Picture;
                Parth = BHStorage.getFolder(F);
                // File folder = new File(Parth + "/" + IMAGE_DIRECTORY_NAME +
                // "/" + ImageType.LOSS);

                captureImage();
            }
        });

    }

    private void captureImage() {
        /*** [START] :: Permission ***/
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/


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

        }, BHPermissions.PermissionType.CAMERA);
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

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Parth + "/" + IMAGE_DIRECTORY_NAME + "/" + ImageType.LOSS);
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
                input.ImageTypeCode = ImageType.LOSS.toString();
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
                previewCapturedImage();
            }
        }).start();
    }

    private void previewCapturedImage() {
        try {
            imageViewLoss.setVisibility(View.VISIBLE);

            String part = fileUri.getPath();
            Bitmap bm = BHBitmap.decodeSampledBitmapFromImagePath(part, 500);
            Bitmap bitmap = BHBitmap.setRotateImageFromImagePath(part, bm);

            imageViewLoss.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    private void DateControlInit() {
        // TODO Auto-generated method stub
        final DatePickerDialog.OnDateSetListener dpl = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCal.set(Calendar.YEAR, year);
                myCal.set(Calendar.MONTH, monthOfYear);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etDate.setText(BHUtilities.dateFormat(myCal.getTime(), "dd/MM/yyyy"));
            }
        };

        imageButtonWriteoffDate.setVisibility(View.GONE);
//        imageButtonWriteoffDate.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                DatePickerDialog dpd = new DatePickerDialog(arg0.getContext(), dpl, myCal.get(Calendar.YEAR), myCal.get(Calendar.MONTH), myCal
//                        .get(Calendar.DAY_OF_MONTH));
//                dpd.show();
//            }
//        });
    }

    private void getProblem() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                problemList = getProblemByProblemType(BHPreference.organizationCode(), ProblemType.WriteOffNPL.toString());
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (problemList != null) {
                    List<String> problem = new ArrayList<String>();
                    for (ProblemInfo item : problemList) {
                        problem.add(String.format(item.ProblemName));
                    }
                    BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(activity, problem);
                    spnProblem.setAdapter(dataAdapter);
                }
                getContractInfo();
            }


            private void getContractInfo() {
                // TODO Auto-generated method stub
                (new BackgroundProcess(activity) {
                    FortnightInfo fortnight;

                    @Override
                    protected void calling() {
                        // TODO Auto-generated method stub
                        if (dataLoss != null && dataLoss.Serialnumber != null) {
                            contractInfo = getContractBySerialNo(BHPreference.organizationCode(), txtCode.getText().toString(), ContractStatus.NORMAL.toString());
                        } else {
                            contractInfo = new ContractController().getContractByRefNo(BHPreference.organizationCode(), BHPreference.RefNo(), ContractStatus.NORMAL.toString());
                        }

                        //fortnight = getCurrentFortnight(BHPreference.organizationCode());
                        fortnight = new FortnightController().getFortnightByFortnightID(contractInfo.FortnightID);
                        //fortnight = new FortnightController().getCurrentFortnightInfo();
                    }

                    @Override
                    protected void after() { // TODO
                        // Auto-generated method stub
                        if (contractInfo != null) {
                            txtNo.setText(contractInfo.CONTNO);
                            txtName.setText(BHUtilities.trim(contractInfo.CustomerFullName) + BHUtilities.trim(contractInfo.CompanyName));
                            txtIDCardNo.setText(contractInfo.IDCard);
                            txtSale.setText(contractInfo.SaleEmployeeName);
                            Date = new SimpleDateFormat("dd/MM/yy").format(contractInfo.InstallDate);
                            txtDataInstall.setText(BHUtilities.dateFormat(contractInfo.InstallDate));

                            String strFortnightNumber = String.valueOf(fortnight.FortnightNumber);
                            String strFortnightYear = BHUtilities.dateFormat(fortnight.StartDate, "yy");
                            txtFortnight.setText(String.format("%s/%s", strFortnightNumber, strFortnightYear));

                            getAddress(contractInfo.RefNo);
                        }

                    }

                    private void getAddress(final String refNo) {
                        // TODO Auto-generated method stub
                        (new BackgroundProcess(activity) {
                            AddressInfo address;

                            @Override
                            protected void calling() {
                                // TODO Auto-generated method stub
                                address = getAddress(refNo, AddressType.AddressInstall);
                            }

                            @Override
                            protected void after() {
                                // TODO Auto-generated method stub
                                if (address != null) {
                                    txtAddress.setText(address.Address());

                                    if (address.TelMobile == null || address.TelMobile.equals(""))
                                        txtTel.setText(address.TelHome);
                                    else {
                                        if (address.TelHome == null)
                                            address.TelHome = "";
                                        txtTel.setText(address.TelHome + " , " + address.TelMobile);
                                    }

                                }

                            }

                        }).start();
                    }

                }).start();
            }
        }).start();

        // (1) Event listener of spinner
        spnProblem.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                try {
                    if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                        selectedProblem = problemList.get(position);
                    } else {
                        selectedProblem = null;
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                selectedProblem = null;
            }
        });

    }

}
