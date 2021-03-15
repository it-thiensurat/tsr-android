//package th.co.thiensurat.fragments.employee;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import th.co.bighead.utilities.BHArrayAdapter;
//import th.co.bighead.utilities.BHFragment;
//import th.co.bighead.utilities.BHPreference;
//import th.co.bighead.utilities.BHUtilities;
//import th.co.bighead.utilities.annotation.InjectView;
//import th.co.thiensurat.R;
//import th.co.thiensurat.business.controller.BackgroundProcess;
//import th.co.thiensurat.data.info.EmployeeInfo;
//import th.co.thiensurat.data.info.FortnightInfo;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//public class EmployeeTeamFragment extends BHFragment {
//
//	private EmployeeInfo mEmployee;
//	// private int mFortnightNumber;
//	// private Date mStartDate;
//	// private Date mEndDate;
//	@InjectView
//	private TextView txtSaleLeaderTeamCode;
//	@InjectView
//	TextView txtSaleLeaderName;
//	@InjectView
//	TextView txtSupervisorName;
//	@InjectView
//	TextView txtLineManagerName;
//	@InjectView
//	TextView txtManagerName;
//	@InjectView
//	TextView txtDriverName;
//	@InjectView
//	TextView txtTeamCode;
//	@InjectView
//	TextView txtFortnightNumber;
//	@InjectView
//	TextView txtFortnightDate;
//	@InjectView
//	LinearLayout llSubTeamEmployeeList;
//	@InjectView
//	TextView txtDateNow;
//	// @InjectView
//	// TextView txtSubTeamCode, txtSubTeamEmployeeName;
//
//	// private static ArrayList<HashMap<String, String>> employeeList = null;
//	private static List<EmployeeInfo> mEmployeeList;
//
//	// @InjectView
//	// private ListView lvEmployee;
//	// @InjectView
//	// private TextView txtSubTeamEmployeeName;
//
//	// @InjectView
//	// private TextView txtTeamCode;
//	// @InjectView
//	// private TextView txtFortnightNumber;
//	// @InjectView
//	// private TextView txtFortnightDate;
//
//	@Override
//	protected int titleID() {
//		// TODO Auto-generated method stub
//		return R.string.title_team;
//	}
//
//	@Override
//	protected int fragmentID() {
//		// TODO Auto-generated method stub
//		// return 0;
//		return R.layout.fragment_employee_team;
//	}
//
//	@Override
//	protected int[] processButtons() {
//		// TODO Auto-generated method stub
//		return null;
//		// return new int[] { R.string.button_next };
//	}
//
//	@Override
//	protected void onCreateViewSuccess(Bundle savedInstanceState) {
//
//		// loadData();
//		loadFortnight();
//		// setWidgetsEventListener();
//	}
//
//	// @Override
//	// public void onProcessButtonClicked(int buttonID) {
//	//
//	// // TODO Auto-generated method stub
//	// switch (buttonID) {
//	// case R.string.button_next:
//	// EmployeeTeamListFragment.Data data = new EmployeeTeamListFragment.Data();
//	// data.FortnightNumber = mFortnightNumber;
//	// data.StartDate = mStartDate;
//	// data.EndDate = mEndDate;
//	//
//	// EmployeeTeamListFragment fm = BHFragment.newInstance(
//	// EmployeeTeamListFragment.class, data);
//	// showNextView(fm);
//	// break;
//	//
//	// default:
//	// break;
//	// }
//	// }
//
//	private void loadFortnight() {
//		(new BackgroundProcess(activity) {
//
//			FortnightInfo output = null;
//
//			@Override
//			protected void before() {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			protected void calling() {
//				// TODO Auto-generated method stub
//				String organizationCode = BHPreference.organizationCode();// "0";
//				output = getCurrentFortnight(organizationCode);// BHPreference.organizationCode());
//			}
//
//			@Override
//			protected void after() {
//				// TODO Auto-generated method stub
//				if (output != null) {
//					// mFortnightNumber = output.FortnightNumber;
//					// mStartDate = output.StartDate;
//					// mEndDate = output.EndDate;
//
//					// ทีม PAK23
//					String teamCode = BHPreference.teamCode();// "PAK23";
//					txtTeamCode.setText(String.format("ทีม %s", teamCode));
//
//					// ปักษ์ที่ 10/57
//					// String strFortnightNumber =
//					// String.valueOf(output.FortnightNumber);
//					String strFortnightYear = BHUtilities.dateFormat(output.StartDate, "yy");// String.valueOf(output.FortnightYear);
//					txtFortnightNumber.setText(String.format("ปักษ์ที่ %d/%s", output.FortnightNumber, strFortnightYear));
//
//					// 5/6/57 - 20/6/57
//					String strStartDate = "01/11/57";// BHUtilities.dateFormat(output.StartDate,
//														// "dd/mm/yy");
//					String strEndDate = "15/11/57";// BHUtilities.dateFormat(output.EndDate,
//													// "dd/mm/yy");
//					txtFortnightDate.setText(String.format("%s-%s", strStartDate, strEndDate));
//				}
//				loadData();
//			}
//		}).start();
//	}
//
//	private void loadData() {
//		(new BackgroundProcess(activity) {
//
//			EmployeeInfo employee;
//
//			@Override
//			protected void before() {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			protected void calling() {
//				// TODO Auto-generated method stub
//				// String organizationCode = BHPreference.organizationCode();//
//				// "0";
//				// String teamCode = BHPreference.teamCode();// "PAK23";
//				// String empID = BHPreference.userID();
//				employee = getSaleTeamByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());
//			}
//
//			@Override
//			protected void after() {
//				// TODO Auto-generated method stub
//				if (employee != null) {
//					mEmployee = employee;
//					txtSaleLeaderTeamCode.setText(String.format("หัวหน้าทีมขาย %s", mEmployee.SaleLeaderTeamCode));
//					txtSaleLeaderName.setText(String.format("ชื่อ %s %s", mEmployee.SaleLeaderFirstName.trim(), mEmployee.SaleLeaderLastName.trim()));
//
//					txtSupervisorName.setText(String.format("%s %s", mEmployee.SupervisorFirstName.trim(), mEmployee.SupervisorLastName.trim()));
//					txtLineManagerName.setText(String.format("%s %s", mEmployee.LineManagerFirstName.trim(), mEmployee.LineManagerLastName.trim()));
//					txtManagerName.setText(String.format("%s %s", mEmployee.ManagerFirstName.trim(), mEmployee.ManagerLastName.trim()));
//
//					// txtDriverName.setText(String.format("พขร. %s : %s %s",
//					// mEmployee.DriverEmpID.trim(),
//					// mEmployee.DriverFirstName.trim(),
//					// mEmployee.DriverLastName.trim()));
//					// txtSubTeamCode.setText(mEmployee.SubTeamCode);
//				}
//
//				// TODO Auto-generated method stub
//
//				// lvEmployee.setAdapter(new EfficientAdapter(activity));
//				(new BackgroundProcess(activity) {
//
//					List<EmployeeInfo> output;
//					String organizationCode;
//					String saleTeamCode;
//
//					@Override
//					protected void before() {
//						// TODO Auto-generated method stub
//						organizationCode = BHPreference.organizationCode();
//						// saleTeamCode = BHPreference.saleCode();
//						saleTeamCode = BHPreference.teamCode();
//
//						// for (int i = output.size() - 1; i >= 0; i--) {
//						// if (!output.get(i).SubTeamCode.equals("PAK02301"))
//						// output.remove(i);
//						// }
//					}
//
//					@Override
//					protected void calling() {
//						// TODO Auto-generated method stub
//						output = getAllTeamMember(organizationCode, saleTeamCode);
//					}
//
//					@Override
//					protected void after() {
//						mEmployeeList = output;
//						// setAdapter();
//
//						LayoutInflater li = activity.getLayoutInflater();
//
//						// String date = BHUtilities.dateFormat(new Date(),
//						// "dd/MM/yyyy");
//						// txtDateNow.setText("ณ   วันที่   "+ date);
//
//						String subTeamCode = "";
//						String subTeamEmployeeName = "";
//						String subTeamDriverName = "";
//
//						for (int i = 0; i < mEmployeeList.size(); i++) {
//
//							if (!subTeamCode.equals(mEmployeeList.get(i).SubTeamCode)) {
//								List<EmployeeInfo> mEmployeeListTemp = new ArrayList<EmployeeInfo>();
//								subTeamCode = mEmployeeList.get(i).SubTeamCode;
//
//								if (mEmployeeList.get(i).DriverEmpID != null)
//									subTeamDriverName = "พขร. " + mEmployeeList.get(i).DriverEmpID.trim() + " : " + mEmployeeList.get(i).DriverFirstName.trim()
//											+ " " + mEmployeeList.get(i).DriverLastName.trim();
//
//								if (subTeamCode == null)
//									subTeamCode = "";
//
//								for (int j = 0; j < mEmployeeList.size(); j++) {
//									if (mEmployeeList.get(j).SubTeamCode.equals(subTeamCode)) {
//										mEmployeeListTemp.add(mEmployeeList.get(j));
//
//										if (mEmployeeList.get(j).PositionCode.equals("SubTeamLeader")) {
//											subTeamEmployeeName = mEmployeeList.get(j).EmpName;
//											subTeamDriverName = "พขร. " + mEmployeeList.get(j).DriverEmpID.trim() + " : "
//													+ mEmployeeList.get(j).DriverFirstName.trim() + " " + mEmployeeList.get(j).DriverLastName.trim();
//
//										}
//									}
//
//								}
//
//								// Add item;
//
//								ViewGroup item = (ViewGroup) li.inflate(R.layout.list_employee_team, llSubTeamEmployeeList, false);
//								TextView txtSubTeamEmployeeName = (TextView) item.findViewById(R.id.txtSubTeamEmployeeName);
//								ListView lvEmployee = (ListView) item.findViewById(R.id.lvEmployee);
//								TextView txtSubTeamDriverName = (TextView) item.findViewById(R.id.txtSubTeamDriverName);
//
//								if (mEmployeeListTemp != null) {
//									txtSubTeamEmployeeName.setText(String.format("%s หน่วย %s", subTeamEmployeeName, subTeamCode));
//									txtSubTeamDriverName.setText(subTeamDriverName);
//								}
//
//								BHArrayAdapter<EmployeeInfo> adapter = new BHArrayAdapter<EmployeeInfo>(activity, R.layout.list_employee_team_list_item,
//										mEmployeeListTemp) {
//
//									class ViewHolder {
//										public TextView txtTitle;
//										// public ImageView imgIcon;
//									}
//
//									@Override
//									protected void onViewItem(int position, View view, Object holder, EmployeeInfo info) {
//										// TODO Auto-generated method stub
//
//										try {
//											ViewHolder vh = (ViewHolder) holder;
//											String titleString = String.format("%s ชื่อ: %s", info.EmpID, info.EmpName);
//
//											vh.txtTitle.setText(titleString);
//
//										} catch (Exception e) {
//											e.printStackTrace();
//										}
//									}
//								};
//								lvEmployee.setAdapter(adapter);
//								llSubTeamEmployeeList.addView(item);
//
//							}
//						}
//
//					}
//				}).start();
//
//			}
//		}).start();
//
//	}
//
//	// private void setWidgetsEventListener() {
//	//
//	// }
//
//	// private void setAdapter() {
//	// BHArrayAdapter<EmployeeInfo> adapter = new BHArrayAdapter<EmployeeInfo>(
//	// activity, R.layout.list_employee_team_list_item, mEmployeeList) {
//	//
//	// class ViewHolder {
//	// public TextView txtTitle;
//	// public ImageView imgIcon;
//	// }
//	//
//	// @Override
//	// protected void onViewItem(int position, View view, Object holder,
//	// EmployeeInfo info) {
//	// // TODO Auto-generated method stub
//	//
//	// try {
//	// ViewHolder vh = (ViewHolder) holder;
//	// String titleString = String.format("%s ชื่อ: %s",
//	// info.EmpID, info.EmpName);
//	//
//	// vh.txtTitle.setText(titleString);
//	//
//	//
//	// } catch (Exception e) {
//	// e.printStackTrace();
//	// }
//	// }
//	// };
//	// lvEmployee.setAdapter(adapter);
//	// }
//}
//
//// import java.util.ArrayList;
//// import java.util.List;
////
//// import th.co.bighead.utilities.BHArrayAdapter;
//// import th.co.bighead.utilities.BHFragment;
//// import th.co.bighead.utilities.BHPreference;
//// import th.co.bighead.utilities.BHUtilities;
//// import th.co.bighead.utilities.annotation.InjectView;
//// import th.co.thiensurat.R;
//// import th.co.thiensurat.business.controller.BackgroundProcess;
//// import th.co.thiensurat.data.info.EmployeeInfo;
//// import th.co.thiensurat.data.info.FortnightInfo;
//// import android.content.Context;
//// import android.os.Bundle;
//// import android.support.v4.view.PagerAdapter;
//// import android.support.v4.view.ViewPager;
//// import android.support.v4.view.ViewPager.OnPageChangeListener;
//// import android.text.Html;
//// import android.view.LayoutInflater;
//// import android.view.View;
//// import android.view.ViewGroup;
//// import android.widget.LinearLayout;
//// import android.widget.ListView;
//// import android.widget.TextView;
////
//// public class EmployeeTeamFragment extends BHFragment {
////
//// private MyViewPagerAdapter myViewPagerAdapter;
//// private ArrayList<Integer> listOfItems;
//// private int dotsCount;
////
//// @InjectView
//// private ViewPager viewPager;
//// @InjectView
//// private LinearLayout viewPagerCountDots;
//// @InjectView
//// private TextView[] dots;
////
//// private EmployeeInfo mEmployee;
//// @InjectView
//// private TextView txtSaleLeaderTeamCode;
//// @InjectView
//// TextView txtSaleLeaderName;
//// @InjectView
//// TextView txtSupervisorName;
//// @InjectView
//// TextView txtLineManagerName;
//// @InjectView
//// TextView txtManagerName;
//// @InjectView
//// TextView txtDriverName;
//// @InjectView
//// TextView txtTeamCode;
//// @InjectView
//// TextView txtFortnightNumber;
//// @InjectView
//// TextView txtFortnightDate;
//// @InjectView
//// TextView txtDateNow;
////
//// private static List<EmployeeInfo> mEmployeeList;
////
//// String subTeamCode = "";
//// String subTeamEmployeeName = "";
//// String subTeamDriverName = "";
////
//// @Override
//// protected int titleID() {
//// // TODO Auto-generated method stub
//// return R.string.title_team;
//// }
////
//// @Override
//// protected int fragmentID() {
//// // TODO Auto-generated method stub
//// return R.layout.fragment_employee_team;
//// }
////
//// @Override
//// protected int[] processButtons() {
//// // TODO Auto-generated method stub
//// return null;
//// }
////
//// @Override
//// protected void onCreateViewSuccess(Bundle savedInstanceState) {
//// // TODO Auto-generated method stub
//// loadFortnight();
//// }
////
//// private void loadFortnight() {
//// (new BackgroundProcess(activity) {
////
//// FortnightInfo output = null;
////
//// @Override
//// protected void calling() {
//// // TODO Auto-generated method stub
//// String organizationCode = BHPreference.organizationCode();
//// output = getCurrentFortnight(organizationCode);
//// }
////
//// @Override
//// protected void after() {
//// // TODO Auto-generated method stub
//// if (output != null) {
////
//// String teamCode = BHPreference.teamCode();
//// txtTeamCode.setText(String.format("ทีม %s", teamCode));
////
//// String strFortnightYear = BHUtilities.dateFormat(output.StartDate, "yy");
//// txtFortnightNumber.setText(String.format("ปักษ์ที่ %d/%s",
//// output.FortnightNumber, strFortnightYear));
////
//// String strStartDate = "01/11/57";
//// String strEndDate = "15/11/57";
//// txtFortnightDate.setText(String.format("%s-%s", strStartDate, strEndDate));
//// }
//// loadData();
//// }
//// }).start();
//// }
////
//// private void loadData() {
//// (new BackgroundProcess(activity) {
////
//// EmployeeInfo employee;
////
//// @Override
//// protected void calling() {
//// // TODO Auto-generated method stub
//// employee = getSaleTeamByTeamCode(BHPreference.organizationCode(),
//// BHPreference.teamCode(), BHPreference.employeeID());
//// }
////
//// @Override
//// protected void after() {
//// // TODO Auto-generated method stub
//// if (employee != null) {
//// mEmployee = employee;
//// txtSaleLeaderTeamCode.setText(String.format("หัวหน้าทีมขาย %s",
//// mEmployee.SaleLeaderTeamCode));
//// txtSaleLeaderName.setText(String.format("ชื่อ %s %s",
//// mEmployee.SaleLeaderFirstName.trim(), mEmployee.SaleLeaderLastName.trim()));
////
//// txtSupervisorName.setText(String.format("%s %s",
//// mEmployee.SupervisorFirstName.trim(), mEmployee.SupervisorLastName.trim()));
//// txtLineManagerName.setText(String.format("%s %s",
//// mEmployee.LineManagerFirstName.trim(),
//// mEmployee.LineManagerLastName.trim()));
//// txtManagerName.setText(String.format("%s %s",
//// mEmployee.ManagerFirstName.trim(), mEmployee.ManagerLastName.trim()));
//// }
////
//// (new BackgroundProcess(activity) {
////
//// @Override
//// protected void calling() {
//// // TODO Auto-generated method stub
//// mEmployeeList = getAllTeamMember(BHPreference.organizationCode(),
//// BHPreference.teamCode());
////
////
//// }
////
//// @Override
//// protected void after() {
//// // TODO Auto-generated method stub
//// initViews(mEmployeeList);
//// setViewPagerItemsWithAdapter();
//// setUiPageViewController();
//// }
//// }).start();
//// }
//// }).start();
////
//// }
////
//// private void initViews(List<EmployeeInfo> mEmployeeList) {
////
//// listOfItems = new ArrayList<Integer>();
//// int listemp = mEmployeeList.size();
//// for (int i = 0; i < listemp; i++) {
//// listOfItems.add(i);
//// }
//// }
////
//// private void setViewPagerItemsWithAdapter() {
//// myViewPagerAdapter = new MyViewPagerAdapter(listOfItems);
//// viewPager.setAdapter(myViewPagerAdapter);
//// viewPager.setCurrentItem(0);
//// viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
//// }
////
//// OnPageChangeListener viewPagerPageChangeListener = new OnPageChangeListener()
//// {
////
//// @Override
//// public void onPageSelected(int position) {
//// for (int i = 0; i < dotsCount; i++) {
//// dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
//// }
//// dots[position].setTextColor(getResources().getColor(R.color.app_green));
//// }
////
//// @Override
//// public void onPageScrolled(int arg0, float arg1, int arg2) {
////
//// }
////
//// @Override
//// public void onPageScrollStateChanged(int arg0) {
////
//// }
//// };
////
//// private void setUiPageViewController() {
////
//// dotsCount = myViewPagerAdapter.getCount();
//// dots = new TextView[dotsCount];
////
//// for (int i = 0; i < dotsCount; i++) {
//// dots[i] = new TextView(activity);
//// dots[i].setText(Html.fromHtml("&#149;"));
//// dots[i].setTextSize(30);
//// dots[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
//// viewPagerCountDots.addView(dots[i]);
//// }
////
//// dots[0].setTextColor(getResources().getColor(R.color.app_green));
//// }
////
//// public class MyViewPagerAdapter extends PagerAdapter {
////
//// private LayoutInflater layoutInflater;
//// private ArrayList<Integer> items;
////
//// public MyViewPagerAdapter(ArrayList<Integer> listOfItems) {
//// this.items = listOfItems;
//// }
////
//// @Override
//// public Object instantiateItem(ViewGroup container, int position) {
////
//// // layoutInflater = (LayoutInflater)
//// //
//// getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//// // View view = layoutInflater.inflate(R.layout.list_employee_team,
//// // container, false);
////
//// // TextView txtSubTeamEmployeeName = (TextView)
//// // view.findViewById(R.id.txtSubTeamEmployeeName);
//// // ListView lvEmployee = (ListView)
//// // view.findViewById(R.id.lvEmployee);
//// // TextView txtSubTeamDriverName = (TextView)
//// // view.findViewById(R.id.txtSubTeamDriverName);
////
//// // txtSubTeamEmployeeName.setText(String.format("%s หน่วย %s",
//// // "BBBBB", "CCCCC"));
//// // txtSubTeamDriverName.setText("AAAAAA");
////
//// for (int i = 0; i < mEmployeeList.size(); i++) {
////
//// if (!subTeamCode.equals(mEmployeeList.get(i).SubTeamCode)) {
//// List<EmployeeInfo> mEmployeeListTemp = new ArrayList<EmployeeInfo>();
//// subTeamCode = mEmployeeList.get(i).SubTeamCode;
////
//// if (mEmployeeList.get(i).DriverEmpID != null)
//// subTeamDriverName = "พขร. " + mEmployeeList.get(i).DriverEmpID.trim() + " : "
//// + mEmployeeList.get(i).DriverFirstName.trim() + " "
//// + mEmployeeList.get(i).DriverLastName.trim();
////
//// if (subTeamCode == null)
//// subTeamCode = "";
////
//// for (int j = 0; j < mEmployeeList.size(); j++) {
//// if (mEmployeeList.get(j).SubTeamCode.equals(subTeamCode)) {
//// mEmployeeListTemp.add(mEmployeeList.get(j));
////
//// if (mEmployeeList.get(j).PositionCode.equals("SubTeamLeader")) {
//// subTeamEmployeeName = mEmployeeList.get(j).EmpName;
//// subTeamDriverName = "พขร. " + mEmployeeList.get(j).DriverEmpID.trim() + " : "
//// + mEmployeeList.get(j).DriverFirstName.trim()
//// + " " + mEmployeeList.get(j).DriverLastName.trim();
////
//// }
//// }
////
//// }
////
//// // Add item;
//// layoutInflater = (LayoutInflater)
//// getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//// View view = layoutInflater.inflate(R.layout.list_employee_team, container,
//// false);
//// TextView txtSubTeamEmployeeName = (TextView)
//// view.findViewById(R.id.txtSubTeamEmployeeName);
//// ListView lvEmployee = (ListView) view.findViewById(R.id.lvEmployee);
//// TextView txtSubTeamDriverName = (TextView)
//// view.findViewById(R.id.txtSubTeamDriverName);
////
//// if (mEmployeeListTemp != null) {
//// txtSubTeamEmployeeName.setText(String.format("%s หน่วย %s",
//// subTeamEmployeeName, subTeamCode));
//// txtSubTeamDriverName.setText(subTeamDriverName);
//// }
////
//// BHArrayAdapter<EmployeeInfo> adapter = new
//// BHArrayAdapter<EmployeeInfo>(activity, R.layout.list_employee_team_list_item,
//// mEmployeeListTemp) {
////
//// class ViewHolder {
//// public TextView txtTitle;
//// // public ImageView imgIcon;
//// }
////
//// @Override
//// protected void onViewItem(int position, View view, Object holder,
//// EmployeeInfo info) {
//// // TODO Auto-generated method stub
////
//// try {
//// ViewHolder vh = (ViewHolder) holder;
//// String titleString = String.format("%s ชื่อ: %s", info.EmpID, info.EmpName);
////
//// vh.txtTitle.setText(titleString);
////
//// } catch (Exception e) {
//// e.printStackTrace();
//// }
//// }
//// };
//// lvEmployee.setAdapter(adapter);
//// ((ViewPager) container).addView(view);
////
//// return view;
//// }
//// }
//// return position;
//// }
////
//// @Override
//// public int getCount() {
//// return items.size();
//// }
////
//// @Override
//// public boolean isViewFromObject(View view, Object obj) {
//// return view == ((View) obj);
//// }
////
//// @Override
//// public void destroyItem(ViewGroup container, int position, Object object) {
//// View view = (View) object;
//// ((ViewPager) container).removeView(view);
//// }
//// }
////
//// }

package th.co.thiensurat.fragments.employee;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.FortnightInfo;

public class EmployeeTeamFragment extends BHFragment {

    private MyViewPagerAdapter myViewPagerAdapter;
    // private ArrayList<Integer> listOfItems;
    private int dotsCount;

    @InjectView
    private ViewPager viewPager;
    @InjectView
    private LinearLayout viewPagerCountDots;
    //    @InjectView
    private TextView[] dots;

    private EmployeeInfo mEmployee;
    @InjectView
    private TextView txtSaleLeaderTeamCode;
    @InjectView
    TextView txtSaleLeaderName;
    @InjectView
    TextView txtSupervisorTitle;
    @InjectView
    TextView txtSupervisorName;
    @InjectView
    TextView txtLineManagerTitle;
    @InjectView
    TextView txtLineManagerName;
    @InjectView
    TextView txtManagerTitle;
    @InjectView
    TextView txtManagerName;
    //    @InjectView
//    TextView txtDriverName;
    @InjectView
    TextView txtTeamCode;
    @InjectView
    TextView txtFortnightNumber;
    @InjectView
    TextView txtFortnightDate;

    private List<EmployeeInfo> mEmployeeList;
    private List<EmployeeDetailInfo> mEmployeeDetailList;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_team;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_employee_team;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        loadFortnight();
    }

    private void loadFortnight() {
        (new BackgroundProcess(activity) {

            FortnightInfo output = null;

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                output = getCurrentFortnightInfo();
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (output != null) {

                    String teamCode = BHPreference.teamCode();
//                    String aa = BHPreference.SubTeamCode();
                    txtTeamCode.setText(String.format("ทีม %s", teamCode));

                    String strFortnightYear = BHUtilities.dateFormat(output.StartDate, "yy");
                    txtFortnightNumber.setText(String.format("ปักษ์ที่ %d/%s", output.FortnightNumber, strFortnightYear));

                    String strStartDate = BHUtilities.dateFormat(output.StartDate);
                    String strEndDate = BHUtilities.dateFormat(output.EndDate);
                    txtFortnightDate.setText(String.format("%s-%s", strStartDate, strEndDate));
                }
                loadData();
            }
        }).start();
    }

    private void loadData() {
        (new BackgroundProcess(activity) {

            EmployeeInfo employee;

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                employee = getSaleTeamByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (employee != null) {
                    Log.e("Emp team", String.valueOf(employee));
                    mEmployee = employee;
                    try {
                        txtSaleLeaderTeamCode.setText(String.format("หัวหน้าทีมขาย %s", mEmployee.SaleLeaderTeamCode));
                        txtSaleLeaderName.setText(String.format("ชื่อ %s %s", mEmployee.SaleLeaderFirstName.trim(), mEmployee.SaleLeaderLastName.trim()));

                        txtSupervisorTitle.setText(txtSupervisorTitle.getText().toString().substring(0, txtSupervisorTitle.getText().toString().length() - 1) + (mEmployee.SaleCode != null ? mEmployee.SaleCode : "") + " :");
                        txtSupervisorName.setText(String.format("%s %s", mEmployee.SupervisorFirstName.trim(), mEmployee.SupervisorLastName.trim()));
                        txtLineManagerTitle.setText(txtLineManagerTitle.getText().toString().substring(0, txtLineManagerTitle.getText().toString().length() - 1) + (mEmployee.SaleCode != null ? (mEmployee.SaleCode.length() > 0 ? mEmployee.SaleCode.substring(0, 1) : "") : "") + " :");
                        txtLineManagerName.setText(String.format("%s %s", mEmployee.LineManagerFirstName.trim(), mEmployee.LineManagerLastName.trim()));
                        txtManagerTitle.setText(txtManagerTitle.getText().toString().substring(0, txtManagerTitle.getText().toString().length() - 1) + (mEmployee.SaleCode != null ? (mEmployee.SaleCode.length() > 0 ? mEmployee.SaleCode.substring(0, 1) : "") : "") + " :");
                        txtManagerName.setText(String.format("%s %s", mEmployee.ManagerFirstName.trim(), mEmployee.ManagerLastName.trim()));
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }

                (new BackgroundProcess(activity) {

                    @Override
                    protected void calling() {
                        // TODO Auto-generated method stub
                        String[] positionCode = BHPreference.PositionCode().split(",");

                        if (Arrays.asList(positionCode).contains("SaleLeader")) {
                            mEmployeeList = getAllTeamMember(BHPreference.organizationCode(), BHPreference.teamCode(), null);


                            /*//test
                            List<EmployeeInfo> newEmployeeLis = getAllTeamMember(BHPreference.organizationCode(), BHPreference.teamCode(), null);
                            for (EmployeeInfo info : newEmployeeLis) {
                                if (info != null) {
                                    info.SubTeamCode = "1";
                                }
                            }
                            mEmployeeList.addAll(newEmployeeLis);
                            //test*/

                        } else {
                            mEmployeeList = getAllTeamMember(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.SubTeamCode());
                        }
                    }

                    @Override
                    protected void after() {
                        // TODO Auto-generated method stub
                        setViewPagerItemsWithAdapter();
                        setUiPageViewController();
                    }
                }).start();
            }
        }).start();
    }

    private void setViewPagerItemsWithAdapter() {
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
    }

    OnPageChangeListener viewPagerPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setTextColor(getResources().getColor(R.color.dot_gray));
            }
            dots[position].setTextColor(getResources().getColor(R.color.dot_red));
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void setUiPageViewController() {

        dotsCount = myViewPagerAdapter.getCount();
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(activity);
            dots[i].setText(Html.fromHtml("&#149;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(R.color.dot_gray));
            viewPagerCountDots.addView(dots[i]);
        }

        dots[0].setTextColor(getResources().getColor(R.color.dot_red));
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private Map<String, List<EmployeeInfo>> groups;
        private String[] groupsKey;

        public MyViewPagerAdapter() {
            // this.items = ;
            groups = new HashMap<String, List<EmployeeInfo>>();

            for (EmployeeInfo employee : mEmployeeList) {
                if (!groups.containsKey(employee.SubTeamCode)) {
                    List<EmployeeInfo> infos = new ArrayList<EmployeeInfo>();
                    infos.add(employee);
                    groups.put(employee.SubTeamCode, infos);
                } else {
                    groups.get(employee.SubTeamCode).add(employee);
                }
            }
            groupsKey = groups.keySet().toArray(new String[groups.size()]);
            Arrays.sort(groupsKey, new Comparator<String>() {

                @Override
                public int compare(String lhs, String rhs) {
                    // TODO Auto-generated method stub
                    return lhs.compareTo(rhs);
                }
            });
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // Add item;
            layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.list_employee_team, container, false);

            ImageView img_arrow = (ImageView) view.findViewById(R.id.imageView_arrow);
            TextView txtSubTeamEmployeeName = (TextView) view.findViewById(R.id.txtSubTeamEmployeeName);
            ListView lvEmployee = (ListView) view.findViewById(R.id.lvEmployee);

            ListView lvEmployeeOther = (ListView) view.findViewById(R.id.lvEmployeeOther);
//            TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
//            TextView txtPosition = (TextView) view.findViewById(R.id.txtPosition);
            //ImageView imgIcon = (ImageView) view.findViewById(R.id.imgIcon);

//            List<EmployeeInfo> employees = new ArrayList<EmployeeInfo>(groups.get(groupsKey[position]));

            final List<EmployeeInfo> tmpemployees = new ArrayList<EmployeeInfo>(groups.get(groupsKey[position]));
            EmployeeInfo driver = null;//new EmployeeInfo();
            List<EmployeeInfo> employees = new ArrayList<EmployeeInfo>();
            for (int i = 0; i < tmpemployees.size(); i++) {
                if (tmpemployees.get(i).PositionID.equals("Driver")) {
                    driver = tmpemployees.get(i);
                } else {
                    employees.add(tmpemployees.get(i));
                }
            }
            //employees.add(driver);
            List<EmployeeInfo> otherEmployees = new ArrayList<EmployeeInfo>();
            if (driver != null) {
                otherEmployees.add(driver);
                //employees.add(driver);
            }

            txtSubTeamEmployeeName.setText(String.format("หน่วย %s", employees.get(0).SubTeamCode));
            int a = position + 1;
            if (a == groupsKey.length) {
                img_arrow.setVisibility(View.GONE);
            }

//            String titleString = String.format("%s ชื่อ: %s", driver.EmpID, driver.FullName());
//            txtTitle.setText(titleString);
//            txtPosition.setText(driver.PositionName);


            BHArrayAdapter<EmployeeInfo> adapter = new BHArrayAdapter<EmployeeInfo>(activity, R.layout.list_employee_team_list_item, employees) {

                class ViewHolder {
                    public TextView txtTitle;
                    public TextView txtPosition;
                    public ImageView imgIcon;
                }

                @Override
                protected void onViewItem(int position, View view, Object holder, EmployeeInfo info) {
                    // TODO Auto-generated method stub

                    try {

                        ViewHolder vh = (ViewHolder) holder;
                        String titleString = String.format("%s\nชื่อ: %s", info.SaleCode != null ? info.SaleCode : info.EmpID, info.FullName());
                        vh.txtTitle.setText(titleString);
                        String positionNameString = String.format("\n%s", info.PositionName);
                        vh.txtPosition.setText(positionNameString);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            lvEmployee.setAdapter(adapter);

            BHArrayAdapter<EmployeeInfo> adapterOther = new BHArrayAdapter<EmployeeInfo>(activity, R.layout.list_employee_team_list_item, otherEmployees) {

                class ViewHolder {
                    public TextView txtTitle;
                    public TextView txtPosition;
                    public ImageView imgIcon;
                }

                @Override
                protected void onViewItem(int position, View view, Object holder, EmployeeInfo info) {
                    // TODO Auto-generated method stub

                    try {

                        ViewHolder vh = (ViewHolder) holder;
                        String titleString = String.format("%s\nชื่อ: %s", info.EmpID, info.FullName());
                        vh.txtTitle.setText(titleString);
                        String positionNameString = String.format("\n%s", info.PositionName);
                        vh.txtPosition.setText(positionNameString);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            lvEmployeeOther.setAdapter(adapterOther);

            setListViewHeightBasedOnChildren(lvEmployee);
            setListViewHeightBasedOnChildren(lvEmployeeOther);

            ((ViewPager) container).addView(view);
            return view;
        }

        public void setListViewHeightBasedOnChildren(ListView listView) {

            ListAdapter mAdapter = listView.getAdapter();

            int totalHeight = 0;

            for (int i = 0; i < mAdapter.getCount(); i++) {
                View mView = mAdapter.getView(i, null, listView);

                mView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                totalHeight += mView.getMeasuredHeight();
                //Log.w("HEIGHT" + i, String.valueOf(totalHeight));

            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();

        }




        @Override
        public int getCount() {
            return groups.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            ((ViewPager) container).removeView(view);
        }
    }

}
