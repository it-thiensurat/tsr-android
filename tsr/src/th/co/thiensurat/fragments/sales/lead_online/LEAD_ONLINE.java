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

package th.co.thiensurat.fragments.sales.lead_online;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHApplication;
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
import th.co.thiensurat.fragments.sales.lead_online.adapter.RecyclerViewDataAdapter;
import th.co.thiensurat.fragments.sales.lead_online.models.Getdata;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;

public class LEAD_ONLINE extends BHFragment {

    @InjectView
    private RecyclerView row1;

    private List<EmployeeInfo> mEmployeeList;
    private List<EmployeeDetailInfo> mEmployeeDetailList;
    RecyclerView.LayoutManager recyclerViewlayoutManager;

    List<Getdata> getdata;
    Getdata GetDataAdapter1;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.lead_online;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.leadonline_row;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //loadFortnight();
        getdata = new ArrayList<>();

        row1.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getActivity());
        row1.setLayoutManager(recyclerViewlayoutManager);
        load_data_lead();
    }






    public  void load_data_lead() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call=null;

                call = request.get_load_data_lead();



            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));

                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data_lead(jsonObject.getJSONArray("data"));

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                }
            });

        } catch (Exception e) {

        }
    }


    public  void JSON_PARSE_DATA_AFTER_WEBCALL_load_data_lead(JSONArray array) {

         Log.e("length1", String.valueOf(array.length()));
        for (int i = 0; i < array.length(); i++) {

              final Getdata GetDataAdapter2 = new Getdata();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setCusName(json.getString("CusName"));

            } catch (JSONException e) {

                e.printStackTrace();
            }

            getdata.add(GetDataAdapter2);

        }



        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(getdata,getActivity());
        row1.setAdapter(adapter);

    }

}
