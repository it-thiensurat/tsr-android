package th.co.thiensurat.fragments.payment.first;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.MenuInfo;
import th.co.thiensurat.fragments.sales.SaleUnfinishedFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FirstPaymentMainMenuFragment extends BHFragment {

	@InjectView
	private ListView lvMenu;
	// private EfficientAdapter mEfficientAdapter;
	private static TypedArray first_payment_menu_icons;
	private static String[] first_payment_menu_titles;

	private EmployeeDetailInfo EmpDetail;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_first;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		// return 0;
		return R.layout.fragment_first_payment_main_menu;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return null;
		// return new int[] { R.string.button_back };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		setupMenu();
		setWidgetsEventListener();
	}

//	private void GetEmpDetail() {
//		// TODO Auto-generated method stub
//		(new BackgroundProcess(activity) {
//			@Override
//			protected void calling() {
//				// TODO Auto-generated method stub
//				EmpDetail = getEmpDetailByempCode(BHPreference.employeeID());
//			}
//
//			@Override
//			protected void after() {
//				// TODO Auto-generated method stub
//				if (EmpDetail != null) {
//					FirstPaymentCustomerListFragment.Data data = new FirstPaymentCustomerListFragment.Data();
//					data.EmpDetail = EmpDetail;
//					FirstPaymentCustomerListFragment fm = BHFragment.newInstance(FirstPaymentCustomerListFragment.class, data);
//					showNextView(fm);
//				} else {
//
//				}
//
//			}
//		}).start();
//	}

	private void setupMenu() {
		// first_payment_menu_titles = getResources().getStringArray(
		// R.array.first_payment_menu_titles);
		// first_payment_menu_icons = getResources().obtainTypedArray(
		// R.array.first_payment_menu_icon);
		try {
			// lvMenu.setAdapter(new EfficientAdapter(getActivity()));
			// setAdapter();
			bindMenu();
		} catch (Exception e) {
			Log.e(this.getClass().getName(), e.getMessage());
		}
	}

	private void setWidgetsEventListener() {
		lvMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity(),
				// String.format("Selected Menu #%d", position),
				// Toast.LENGTH_LONG).show();
				// drawerLayout.closeDrawer(drawerListView);

				MenuInfo info = (MenuInfo) parent.getItemAtPosition(position);
				view.setSelected(true);
				selectedMenu(position, info.titleID);
			}
		});
	}

	private void selectedMenu(int position, int titleResourceID) {

		switch (titleResourceID) {
		case R.string.first_payment_menu_customer_detail:// Customer Detail
//			GetEmpDetail();
			showNextView(BHFragment.newInstance(FirstPaymentCustomerListFragment.class));			
			break;

		case R.string.first_payment_menu_summary:// Summary
			showNextView(BHFragment.newInstance(FirstPaymentSummaryFragment.class));
			break;

		// case R.string.first_payment_menu_import_data:// Import data
		// showNextView(BHFragment
		// .newInstance(FirstPaymentImportDataFragment.class));
		// break;

//		case R.string.first_payment_menu_exit:// Exit System
//			showLastView();
//			break;

		default:
			break;
		}
	}

	private void bindMenu() {
		MenuInfo[] menus = MenuInfo.from(R.array.first_payment_menu_titles, R.array.first_payment_menu_icon);
		BHArrayAdapter<MenuInfo> adapter = new BHArrayAdapter<MenuInfo>(activity, R.layout.list_first_payment_menu, menus) {

			class ViewHolder {
				public ImageView imgIcon;
				public TextView txtTitle;
			}

			@Override
			protected void onViewItem(int position, View view, Object holder, MenuInfo info) {
				// TODO Auto-generated method stub
				ViewHolder vh = (ViewHolder) holder;
				vh.txtTitle.setText(info.titleID);
				vh.imgIcon.setImageResource(info.iconID);
			}
		};
		lvMenu.setAdapter(adapter);
	}
	
	private void getSaleLeader() {
		(new BackgroundProcess(activity) {
			
			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				
			}
		}).start();
	}

	// private static class EfficientAdapter extends BaseAdapter {
	//
	// private LayoutInflater mInflater;
	// private Context mContext;
	//
	// public EfficientAdapter(Context context) {
	// mInflater = LayoutInflater.from(context);
	// mContext = context;
	// }
	//
	// @Override
	// public int getCount() {
	// // TODO Auto-generated method stub
	// // return 0;
	// return first_payment_menu_titles.length;
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// // TODO Auto-generated method stub
	// // return null;
	// return position;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // TODO Auto-generated method stub
	// ViewHolder holder;
	//
	// if (convertView == null) {
	// convertView = mInflater.inflate(
	// R.layout.list_first_payment_listview_menu_item, null);
	// holder = new ViewHolder();
	// holder.icon = (ImageView) convertView.findViewById(R.id.icon);
	// holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);
	// holder.title = (TextView) convertView.findViewById(R.id.title);
	//
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	//
	// try {
	// holder.icon.setImageResource(first_payment_menu_icons
	// .getResourceId(position, -1));
	// holder.title.setText(first_payment_menu_titles[position]);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return convertView;
	// }
	//
	// private static class ViewHolder {
	// ImageView icon;
	// TextView title;
	// ImageView arrow;
	// }
	// }
}
