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


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.fragments.sales.lead_online.adapter.ReadJSON;
import th.co.thiensurat.fragments.sales.lead_online.adapter.RecyclerViewDataAdapter;
import th.co.thiensurat.fragments.sales.lead_online.models.Getdata;
import th.co.thiensurat.fragments.sales.lead_online.models.GetdataStampCode;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;

public class LEAD_ONLINE extends BHFragment implements RecyclerViewDataAdapter.ItemClickListener{

    @InjectView
    private RecyclerView row1;
    private RecyclerViewDataAdapter recyclerViewDataAdapter;
    private List<EmployeeInfo> mEmployeeList;
    private List<EmployeeDetailInfo> mEmployeeDetailList;
    private List<GetdataStampCode> getdataStampCodes;
    RecyclerView.LayoutManager recyclerViewlayoutManager;

    List<Getdata> getdata;
    List<GetdataStampCode> getdata1;
    Getdata GetDataAdapter1;
    RecyclerViewDataAdapter adapter;

SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<HashMap<String, String>> MyArrListTotal = new ArrayList<>();
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
        getdata1 = new ArrayList<>();
        row1.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getActivity());
        row1.setLayoutManager(recyclerViewlayoutManager);

        load_data_lead();
    }

 public  void dialogspinner(String _id , String _StatusWork,String _Namecustomer,String _IdProvince){

     Dialog dialog_image;
     log("xx",_id+""+_StatusWork);
     dialog_image = new Dialog(activity);
     //dialog_image.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
     dialog_image.setTitle("Stamp Cord");
     dialog_image.setContentView(R.layout.dialog_spinner);
     Spinner spinner = (Spinner) dialog_image.findViewById(R.id.spinnercus);
     Button buttonOk = (Button) dialog_image.findViewById(R.id.btnOkstc);
     Button buttonCancel = (Button) dialog_image.findViewById(R.id.btnCancelstc);
     TextView txtremark = (TextView) dialog_image.findViewById(R.id.txtRemark);
             //ArrayAdapter<String> adapter =new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.leadonline_status));
    // adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
   // spinner.setAdapter(adapter);
     ArrayList<String> statusc = new ArrayList<String>();
     statusc.add("เลือกการติดต่อ");
     statusc.add("I01-ปิดการขายติดตั้งเรียบร้อยแล้ว");
     statusc.add("A01-ไม่รับสาย");
     statusc.add("A02-เบอร์โทรผิด");
     statusc.add("A03-โทรไม่ติด,ปิดเครื่อง");
     statusc.add("A04-ลูกค้าไม่สะดวกคุยจะติดต่อกลับ");
     statusc.add("B01-รอตัดสินใจ");
     statusc.add("B02-รอปรึกษาครอบครัวก่อน");
     statusc.add("B03-ลูกค้านัดวันติดตั้ง/ (สามารถเลือกระบุวันนัดในปฏิทินของระบบ เพื่อแจ้งเตือนได้เมื่อถึงวันนัด)");
     statusc.add("B04-สอบถามข้อมูลให้เพื่อนหรือญาติ");
     statusc.add("B05-ลูกค้าให้ส่งข้อมูลเพิ่มเติมทางไลน์");
     statusc.add("B06-ลูกค้าจะส่งข้อมูลให้แอดมินเพิ่มเติม (เช่น Location,เบอร์โทรอื่นเพิ่มเติม) / แอดมินสามารถEditแก้ไขหรือเพิ่มข้อมูลในNoteได้");
     statusc.add("F01-ไม่สนใจสินค้า");
     statusc.add("F02-ซื้อยี่ห้ออื่นมาแล้ว");
     statusc.add("F03-ลูกค้าใช้เซฟอยู่แล้วทักมาสอบถามเฉยๆ");
     statusc.add("F04-ต้องการสินค้าอื่นๆ");
     statusc.add("E01-ไม่ต้องการเทิร์น ต้องการเปลี่ยนสารกรอง / (ต้องระบุรุ่นที่จะเปลี่ยนสาร)");
     statusc.add("E02-นอกเขตการขาย,พื้นที่ปิดการขาย / (ต้องระบุพื้นที่ของลูกค้าที่แจ้งว่านอกเขต)");
     statusc.add("E03-ลูกค้ามีสถานะถอดT / (ต้องแนบรูปภาพการตรวจสถานะจากระบบ)");
     statusc.add("E04-ลูกค้ามีสถานะหนี้สูญR / (ต้องแนบรูปภาพการตรวจสถานะจากระบบ)");
     statusc.add("E05-ฝ่ายขายพิจารณาแล้วสภาพบ้านไม่ผ่าน / (ต้องแนบรูปภาพถ่ายบ้านลูกค้า)");
     statusc.add("E06-มีฝ่ายขายอื่นของTSRติดตั้งไปแล้ว / (ต้องแนบรูปภาพถ่ายเครื่องกรองของลูกค้าที่พึ่งติดตั้งใหม่)");
     
     BHSpinnerAdapter<String> arrayLead = new BHSpinnerAdapter<String>(activity, statusc);
     spinner.setAdapter(arrayLead);
     buttonOk.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             String cus="";
             if(spinner.getSelectedItem()=="เลือกการติดต่อ"){
                 String title = "แจ้งเตือน";
                 String message = "กรุณาเลือกสานะการทำงาน";
                 showWarningDialog(title, message);
                 return ;
             }else if(spinner.getSelectedItem()=="I01-ปิดการขายติดตั้งเรียบร้อยแล้ว"){
                 cus="I01";
             }else if(spinner.getSelectedItem()=="A01-ไม่รับสาย"){
                 cus="A01";
             }else if(spinner.getSelectedItem()=="A02-เบอร์โทรผิด"){
                 cus="A02";
             }else if(spinner.getSelectedItem()=="A03-โทรไม่ติด,ปิดเครื่อง"){
                 cus="A03";
             }else if(spinner.getSelectedItem()=="A4-ลูกค้าไม่สะดวกคุยจะติดต่อกลับ"){
                 cus="A04";
             }else if(spinner.getSelectedItem()=="B01-รอตัดสินใจ"){
                 cus="B01";
             }else if(spinner.getSelectedItem()=="B02-รอปรึกษาครอบครัวก่อน"){
                 cus="B02";
             }else if(spinner.getSelectedItem()=="B03-ลูกค้านัดวันติดตั้ง/ (สามารถเลือกระบุวันนัดในปฏิทินของระบบ เพื่อแจ้งเตือนได้เมื่อถึงวันนัด)"){
                 cus="B03";
             }else if(spinner.getSelectedItem()=="B04-สอบถามข้อมูลให้เพื่อนหรือญาติ"){
                 cus="B04";
             }else if(spinner.getSelectedItem()=="B05-ลูกค้าให้ส่งข้อมูลเพิ่มเติมทางไลน์"){
                 cus="B05";
             }else if(spinner.getSelectedItem()=="B06-ลูกค้าจะส่งข้อมูลให้แอดมินเพิ่มเติม (เช่น Location,เบอร์โทรอื่นเพิ่มเติม) / แอดมินสามารถEditแก้ไขหรือเพิ่มข้อมูลในNoteได้"){
                 cus="B06";
             }else if(spinner.getSelectedItem()=="F01-ไม่สนใจสินค้า"){
                 cus="F01";
             }else if(spinner.getSelectedItem()=="F02-ซื้อยี่ห้ออื่นมาแล้ว"){
                 cus="F02";
             }else if(spinner.getSelectedItem()=="F03-ลูกค้าใช้เซฟอยู่แล้วทักมาสอบถามเฉยๆ"){
                 cus="F03";
             }else if(spinner.getSelectedItem()=="F04-ต้องการสินค้าอื่นๆ"){
                 cus="F04";
             }else if(spinner.getSelectedItem()=="E01-ไม่ต้องการเทิร์น ต้องการเปลี่ยนสารกรอง / (ต้องระบุรุ่นที่จะเปลี่ยนสาร)"){
                 cus="E01";
             }else if(spinner.getSelectedItem()=="E02-นอกเขตการขาย,พื้นที่ปิดการขาย / (ต้องระบุพื้นที่ของลูกค้าที่แจ้งว่านอกเขต)"){
                 cus="E02";
             }else if(spinner.getSelectedItem()=="E03-ลูกค้ามีสถานะถอดT / (ต้องแนบรูปภาพการตรวจสถานะจากระบบ)"){
                 cus="E03";
             }else if(spinner.getSelectedItem()=="E04-ลูกค้ามีสถานะหนี้สูญR / (ต้องแนบรูปภาพการตรวจสถานะจากระบบ)"){
                 cus="E04";
             }else if(spinner.getSelectedItem()=="E05-ฝ่ายขายพิจารณาแล้วสภาพบ้านไม่ผ่าน / (ต้องแนบรูปภาพถ่ายบ้านลูกค้า)"){
                 cus="E05";
             }else if(spinner.getSelectedItem()=="E06-มีฝ่ายขายอื่นของTSRติดตั้งไปแล้ว / (ต้องแนบรูปภาพถ่ายเครื่องกรองของลูกค้าที่พึ่งติดตั้งใหม่)"){
                 cus="E06";
             }
             String title = "แจ้งเตือน";
             String message = "ยืนยันการอัพเดทสานะการทำงาน";
             showWarningDialog(title, message);
             update_data_lead(_id , _StatusWork, cus, _Namecustomer,_IdProvince,txtremark.getText().toString());
             BHLoading.close();
             dialog_image.dismiss();
             load_data_lead();
         }
     });
     dialog_image.setCancelable(true);
     dialog_image.show();
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog_image.dismiss();
                }
            });
 }
 public void openimage(String url){
     Dialog dialog_image;
     dialog_image = new Dialog(activity);
     dialog_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
     dialog_image.setContentView(R.layout.image_dialog);
     ImageView imageView = (ImageView) dialog_image.findViewById(R.id.user_fullimage);
     Glide.with(activity)
             .load(url)
             //.load("https://www.safealkaline.com/media/catalog/product/cache/1/image/750x750/9df78eab33525d08d6e5fb8d27136e95/s/a/safe_uv_alkaline_front.png")
             .placeholder(R.drawable.barcode) //5
             .error(R.drawable.bg_splash) //6
//             .fallback(R.drawable.barcode) //7
             .into(imageView);
     dialog_image.setCancelable(true);
     dialog_image.show();
 }
    public void load_data_lead() {
        try {
            BHLoading.show(activity);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = null;
               String emp = BHPreference.employeeID();
          //  String emp = "A61432";
            call = request.get_api_leadonline(emp);
            // call = request.get_load_data_lead();

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data_lead(jsonObject.getJSONArray("data"));
                        BHLoading.close();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        BHLoading.close();
                        Log.e("JSONException", e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                    BHLoading.close();
                    Log.e("onFailure", t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            BHLoading.close();
        }
    }


    public void JSON_PARSE_DATA_AFTER_WEBCALL_load_data_lead(JSONArray array) {
        Log.e("length1", String.valueOf(array.length()));
        getdata = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {

            final Getdata GetDataAdapter2 = new Getdata();

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                GetDataAdapter2.setId(json.getString("id"));
                GetDataAdapter2.setCreateDate(json.getString("DateCrete"));
                GetDataAdapter2.setCustomerName(json.getString("CustomerName"));
                GetDataAdapter2.setTel(json.getString("Tel"));
                GetDataAdapter2.setProvince(json.getString("Province"));
                GetDataAdapter2.setEmail(json.getString("Email"));
                GetDataAdapter2.setIDLine(json.getString("IDLine"));
                GetDataAdapter2.setDetails(json.getString("Details"));
                GetDataAdapter2.setChannel(json.getString("Channel"));
                GetDataAdapter2.setStatusWork(json.getString("StatusWork"));
                GetDataAdapter2.setPicture(json.getString("Link"));
                GetDataAdapter2.setProduct(json.getString("Product"));
                GetDataAdapter2.setCodeStamp(json.getString("StatusCus"));
                GetDataAdapter2.setIDProvince(json.getString("IdProvince"));
                GetDataAdapter2.setDateSale((json.getString("DateSale")));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException x){
                GetDataAdapter2.setProduct("-");
            }
            getdata.add(GetDataAdapter2);
        }

        adapter = new RecyclerViewDataAdapter(getdata, activity);
        row1.setAdapter(adapter);
        adapter.setClickListener(this);
        adapter.notifyDataSetChanged();
    }

    public String update_data_lead(String _idl, String _statuswork, String _statuscus,String _Namecustomer,String _IdProvince,String remark) {
        final String[] rs = {""};
        String empsale = BHPreference.employeeID();
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = null;
            call = request.updates_status_leadonline(_idl, _statuswork, _statuscus,_Namecustomer,empsale,_IdProvince,remark);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
                        Log.e("update lead data", String.valueOf(jsonObject));
                        rs[0] = jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                    Log.e("onFailure update", t.getLocalizedMessage());
                }
            });
        } catch (Exception e) {

        }

        return rs[0];
    }

    public void loadSpinnerData(String id,String sw,String sc) {
        try {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = null;
            call = request.get_leadonlineCoeStamp();


            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));

                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data_CoeStamp1(jsonObject.getJSONArray("data"));

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
    public void JSON_PARSE_DATA_AFTER_WEBCALL_load_data_CoeStamp1(JSONArray array) {
        HashMap<String, String> map;
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject c = array.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("id", c.getString("id"));
                map.put("CodeStampTxt", c.getString("CodeStampTxt")+"-"+c.getString("CodeStampTxt"));
                MyArrListTotal.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String[] array2 = new String[MyArrListTotal.size()];
        ArrayAdapter<String> adapter = null ;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onAccept(View v, int position) {
        Getdata data = getdata.get(position);
        Log.e("accept", String.valueOf(data.getId()));
        AlertDialog.Builder setupAlert = new AlertDialog.Builder(activity);
        setupAlert.setTitle("แจ้งเตือน");
        setupAlert.setCancelable(false);
        setupAlert.setMessage("ยืนยันการอัพเดทสานะการทำงาน");
        setupAlert.setNegativeButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                String status = update_data_lead(data.getId(), "2", "", data.getCustomerName(), data.getIDProvince(),"");
                load_data_lead();
            }
        }).setPositiveButton(activity.getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        setupAlert.show();
    }
    @Override
    public void onSearch(View v, int position) {
        Getdata data = getdata.get(position);
        dialogspinner(data.getId(),"3", data.getCustomerName(), data.getIDProvince());
    }

    @Override
    public void onCancel(View v, int position) {
        Getdata data = getdata.get(position);
        Log.e("accept", String.valueOf(data.getId()));
        AlertDialog.Builder setupAlert = new AlertDialog.Builder(activity);
        setupAlert.setTitle("แจ้งเตือน");
        setupAlert.setCancelable(false);
        setupAlert.setMessage("ยืนยันการอัพเดทสานะการทำงาน");
        setupAlert.setNegativeButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                String status = update_data_lead(data.getId(), "0", "", data.getCustomerName(), data.getIDProvince(),"");
                load_data_lead();
            }
        }).setPositiveButton(activity.getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        setupAlert.show();
    }
}
