package th.co.thiensurat.fragments.products.returns;

import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ReturnProductInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ReturnProductListFragment extends BHFragment {

	@InjectView private EditText edtSearch;
	@InjectView private Button btnSearch;
	@InjectView private ListView lvReturnProductList;

	private List<ReturnProductInfo> returnList;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_return_product_list;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_add_return_product };
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_return_product;
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
			case R.string.button_add_return_product:
				ReturnProductAddFragment fmAdd = BHFragment.newInstance(ReturnProductAddFragment.class);
				showNextView(fmAdd);
				break;
			default:
				break;
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		getReturnProductHistory();
		setWidgetsEventListener();
		setHeader();
	}

	private void setHeader() {
		LayoutInflater inflater = activity.getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_return_product_list_header, lvReturnProductList, false);
		lvReturnProductList.addHeaderView(header, null, false);
	}

	private void getReturnProductHistory() {
		(new BackgroundProcess(activity) {

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				try {
					returnList = TSRController.getReturnProductByCondition(BHPreference.organizationCode(), "", BHPreference.teamCode(), "");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			protected void after() {
				try {
					if (returnList != null) {
						bindReturnProduct();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		}).start();
	}

	private void bindReturnProduct() {
		BHArrayAdapter<ReturnProductInfo> adapter = new BHArrayAdapter<ReturnProductInfo>(activity, R.layout.list_return_product_list_item, returnList) {

			class ViewHolder {
				public TextView txtReturnDate, txtReturnProductID;
			}

			@Override
			protected void onViewItem(int position, View view, Object holder, ReturnProductInfo info) {
				// TODO Auto-generated method stub
				ViewHolder vh = (ViewHolder) holder;
				try {
					vh.txtReturnDate.setText(BHUtilities.dateFormat(info.ReturnDate, "dd/MM/yyyy"));
					vh.txtReturnProductID.setText(info.ReturnProductID);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
		lvReturnProductList.setAdapter(adapter);
	}

	private void setWidgetsEventListener() {
		btnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lvReturnProductList.setAdapter(null);
				returnList = null;
				search();
			}
		});

		lvReturnProductList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				try {
					final String returnProductID = ((ReturnProductInfo) lvReturnProductList.getItemAtPosition(position)).ReturnProductID;
					ReturnProductDisplayFragment.Data dataReturn = new ReturnProductDisplayFragment.Data();
					dataReturn.ReturnProductID = returnProductID;
					ReturnProductDisplayFragment fmDisplay = BHFragment.newInstance(ReturnProductDisplayFragment.class, dataReturn);
					showNextView(fmDisplay);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}

	private void search() {
		(new BackgroundProcess(activity) {
			List<ReturnProductInfo> output = null;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				try {
					output = TSRController.getReturnProductByCondition(BHPreference.organizationCode(), edtSearch.getText().toString(), BHPreference.teamCode(), "");
					if (output == null) {
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			@Override
			protected void after() {
				try {
					if (output != null) {
						returnList = output;
						bindReturnProduct();
					} else {
						String title = "";
						String message = "ไม่พบข้อมูล!!!";
						showNoticeDialogBox(title, message);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void showNoticeDialogBox(final String title, final String message) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(activity);
		setupAlert.setTitle(title);
		setupAlert.setMessage(message);
		setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// ??
					}
				});
		setupAlert.show();
	}



}
