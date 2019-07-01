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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.ContractImageController.ImageType;
import th.co.thiensurat.data.info.MenuInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;

public class SaleListPhotoFragment extends BHFragment {

    private String ImageTypeCode;

    @InjectView private ListView lvMenuPhoto;
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
        return R.layout.fragment_sale_list_photo;

    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            return new int[]{R.string.button_back};
        } else { //if(BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString()))
            return new int[]{R.string.button_back, R.string.button_end};
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            linearLayoutHeadNumber.setVisibility(View.GONE);
        }

        // TODO Auto-generated method stub
        if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            txtNumber1.setText("...");
            txtNumber2.setText("8");
            txtNumber3.setText("9");
            txtNumber4.setText("10");
            txtNumber5.setText("11");
            txtNumber4.setBackgroundResource(R.drawable.circle_number_sale_color_red);
        }
        setupMenu();
    }

    private void setupMenu() {
        try {

            bindMenu();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }

    private void bindMenu() {
        MenuInfo[] menus = MenuInfo.from(R.array.sale_photo);
        BHArrayAdapter<MenuInfo> adapter = new BHArrayAdapter<MenuInfo>(activity, R.layout.list_sale_photo, menus) {

            class ViewHolder {
                // public ImageView imgPhoto;
                public TextView txtMenu;
                // public ImageView imgPhoto;
            }

            @Override
            protected void onViewItem(int position, View view, Object holder, MenuInfo info) {
                // TODO Auto-generated method stub
                ViewHolder vh = (ViewHolder) holder;
                vh.txtMenu.setText(info.titleID);
                // vh.imgPhoto.setImageResource(info.iconID);

            }
        };

        lvMenuPhoto.setAdapter(adapter);
        lvMenuPhoto.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                MenuInfo info = (MenuInfo) parent.getItemAtPosition(position);
                view.setSelected(true);
                selectedMenu(position, info.titleID);
            }

        });
    }

    protected void selectedMenu(int position, int titleResourceID) {
        // TODO Auto-generated method stub
        switch (titleResourceID) {
            case R.string.sale_photo_home:
                ImageTypeCode = ImageType.ADDRESS.toString();
                CheckPhoto(ImageTypeCode);
                break;
            case R.string.sale_photo_id:
                ImageTypeCode = ImageType.IDCARD.toString();
                CheckPhoto(ImageTypeCode);
                break;
            /*** [START] Fixed - [BHPROJ-0024-678] ***/
            case R.string.sale_photo_gps:
                ImageTypeCode = ImageType.MAP.toString();
                CheckPhoto(ImageTypeCode);
                break;
            case R.string.sale_photo_gps_payment:
                ImageTypeCode = ImageType.MAPPAYMENT.toString();
                CheckPhoto(ImageTypeCode);
                break;
            /*** [END] Fixed - [BHPROJ-0024-678] ***/
            case R.string.sale_photo_product:
                ImageTypeCode = ImageType.PRODUCT.toString();
                CheckPhoto(ImageTypeCode);
                break;
            /*** [START] Fixed - [BHPROJ-0020-932] ***/
            case R.string.sale_photo_payment_card:
                ImageTypeCode = ImageType.PAYMENTCARD.toString();
                CheckPhoto(ImageTypeCode);
                break;
            /*** [END] Fixed - [BHPROJ-0020-932] ***/
            default:
                break;
        }
    }

    private void CheckPhoto(String imageTypeCode) {
        // TODO Auto-generated method stub
        SaleCheckPhotographyFragment.Data data1 = new SaleCheckPhotographyFragment.Data();
        data1.TypeCode = imageTypeCode;
        data1.title = titleID();
        SaleCheckPhotographyFragment fm = BHFragment.newInstance(SaleCheckPhotographyFragment.class, data1);
        showNextView(fm);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_ok:

                break;
            case R.string.button_end:
                activity.showView(new EditContractsMainFragment());
                break;
            default:
                break;
        }
    }

}
