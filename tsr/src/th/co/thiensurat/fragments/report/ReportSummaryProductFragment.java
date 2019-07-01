package th.co.thiensurat.fragments.report;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.PositionController;
import th.co.thiensurat.data.controller.SubTeamController;
import th.co.thiensurat.data.controller.TripController;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.ProductInfo;
import th.co.thiensurat.data.info.ReportInventoryInfo;
import th.co.thiensurat.data.info.ReportSummaryTradeProductReceiveInfo;
import th.co.thiensurat.data.info.SubTeamInfo;
import th.co.thiensurat.data.info.TeamInfo;
import th.co.thiensurat.data.info.TripInfo;
import th.co.thiensurat.service.data.ReportDashboardTeamInfo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ReportSummaryProductFragment extends BHFragment {
//	@InjectView
//	private ListView lvSummary;
//	@InjectView
//	private Spinner spnFortnight, spnProduct, spnTeam;
//	@InjectView
//	private LinearLayout Team;
//
//
//
//	private BHListViewAdapter adapter;
//	private String start, end, year, product, team, sup;
//
//	private int sumBFCount;
//	private int sumImportCount;
//	private int sumInstallCount;
//	private int sumChangeCount;
//	private int sumRetrunCount;
//	private int sumBalanceCount;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_summary_product;
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.menu_report_product;
	}
	

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		//return new int[] { R.string.button_back, R.string.button_print };
		/*** [START] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
//		return new int[] { R.string.button_back };
		return null;
		/*** [END] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/*Activity a = getActivity();
		if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/

		//setSpnTeam();
		//loadData();

		setWebView();


	}
	@InjectView
	private WebView webView;

	private  void setWebView()
	{
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
				view.loadUrl(urlNewString);
				BHLoading.show(getActivity());


				return true;
			}

			Runnable rRun;
			Handler handler = new Handler();;

			@Override
			public void onPageStarted(WebView view, String url, Bitmap facIcon) {
				//SHOW LOADING IF IT ISNT ALREADY VISIBLE

				BHLoading.show(getActivity());
				rRun=new Runnable() {
					@Override
					public void run() {
						showMessage(getResources().getString(R.string.mobile_report_error));
						BHLoading.close();
					}
				};
				int delay = Integer.parseInt(getActivity().getResources().getString(R.string.mobile_report_delay));
				handler.postDelayed(rRun, delay);

			}

			@Override
			public void onPageFinished(WebView view, String url) {

				handler.removeCallbacks(rRun);
				showMessage(getResources().getString(R.string.mobile_report_success));
				BHLoading.close();
			}


		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_product) + "");

	}

	/*
	private void setSpnTeam() {
		// TODO Auto-generated method stub
		if (BHPreference.PositionCode().equals("Supervisor")) {
			Team.setVisibility(View.VISIBLE);
			sup = BHPreference.employeeID();

			getSup();
		}

	}

	private void getSup() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {

			EmployeeInfo SupCode;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				SupCode = getEmployeeByID(sup);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (SupCode != null) {
					// sup = SupCode.SupervisorCode;
					sup = SupCode.SupervisorHeadCode;
				}
			}
		}).start();
	}

	private void loadData() {
		// TODO Auto-generated method stub

		(new BackgroundProcess(activity) {

			List<FortnightInfo> outputFortnight;
			List<ProductInfo> outputProduct;
			List<TeamInfo> outputTeam;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub

				outputFortnight = getAllFortnight(BHPreference
						.organizationCode());
				outputProduct = getProduct();
				if (sup != null) {
					outputTeam = getTeamBySup(sup);
				}
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (outputFortnight != null) {

					List<String> fortnightList = new ArrayList<String>();
					fortnightList.add(String.format(" "));
					for (FortnightInfo item : outputFortnight) {
						start = BHUtilities.dateFormat(item.StartDate);
						end = BHUtilities.dateFormat(item.EndDate);
						year = BHUtilities.dateFormat(item.StartDate, "yyyy");
						fortnightList.add(item.FortnightNumber + "/" + year
								+ " (" + start + " - " + end + ") ");
					}
					BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(
							activity, fortnightList);
					spnFortnight.setAdapter(dataAdapter);
					spnFortnight
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int position, long id) {

									if (!parent.getItemAtPosition(position)
											.toString().trim().equals("")) {
										year = outputFortnight
												.get(position - 1).FortnightNumber
												+ "/"
												+ BHUtilities.dateFormat(
														outputFortnight
																.get(position - 1).StartDate,
														"yyyy");

										showMessage(year);

									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> parent) {
									// TODO Auto-generated method stub

								}
							});

				}

				if (outputProduct != null) {
					List<String> productList = new ArrayList<String>();
					productList.add(String.format(" "));
					for (ProductInfo item : outputProduct) {
						productList.add(item.ProductName);
					}
					BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(
							activity, productList);
					spnProduct.setAdapter(dataAdapter);
					spnProduct
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(
										AdapterView<?> parent, View view,
										int position, long id) {
									if (!parent.getItemAtPosition(position)
											.toString().trim().equals("")) {
										product = outputProduct
												.get(position - 1).ProductID;

										showMessage(product);
										bindSummaryList();

									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> parent) {
									// TODO Auto-generated method stub

								}
							});

				}
				if (outputTeam != null) {
					List<String> TeamList = new ArrayList<String>();
					for (TeamInfo item : outputTeam) {
						TeamList.add(item.Code);
					}
					BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(
							activity, TeamList);
					spnTeam.setAdapter(dataAdapter);
					spnTeam.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {

							team = outputTeam.get(position).Code;

							showMessage(team);
							// bindSummaryList();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}
					});

				} else {
					team = BHPreference.teamCode();
				}

			}

		}).start();

	}

	public void bindSummaryList() {

		(new BackgroundProcess(activity) {

			List<ReportInventoryInfo> summery;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub

				summery = getReportInventory(year, product, team);

				if (summery != null) {
					summery.add(summery.size(), new ReportInventoryInfo());
					summery.add(summery.size(), new ReportInventoryInfo());

					sumBFCount = 0;
					sumImportCount = 0;
					sumInstallCount = 0;
					sumChangeCount = 0;
					sumRetrunCount = 0;
					sumBalanceCount = 0;

					for (int i = 0; i < summery.size(); i++) {
						sumBFCount = sumBFCount + summery.get(i).RemainProduct;
						sumImportCount = sumImportCount
								+ summery.get(i).PickProduct;
						sumInstallCount = sumInstallCount
								+ summery.get(i).InstallProduct;
						sumChangeCount = sumChangeCount
								+ summery.get(i).ChangeProduct;
						sumRetrunCount = sumRetrunCount
								+ summery.get(i).ReturnProduct;
						sumBalanceCount = sumBalanceCount
								+ summery.get(i).BalanceProduct;
					}

					summery.get(summery.size() - 1).RemainProduct = sumBFCount;
					summery.get(summery.size() - 1).PickProduct = sumImportCount;
					summery.get(summery.size() - 1).InstallProduct = sumInstallCount;
					summery.get(summery.size() - 1).ChangeProduct = sumChangeCount;
					summery.get(summery.size() - 1).ReturnProduct = sumRetrunCount;
					summery.get(summery.size() - 1).BalanceProduct = sumBalanceCount;
				}
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				if (summery != null) {
					adapter = new BHListViewAdapter(activity) {
						class ViewHolder {
							public TextView txtDate;
							public TextView txtBF;
							public TextView txtImport;
							public TextView txtInstall;
							public TextView txtChange;
							public TextView txtRetrun;
							public TextView txtBalance;

						}

						@Override
						protected int viewForSectionHeader(int section) {
							// TODO Auto-generated method stub

							return R.layout.list_report_summary_product_header;

						}

						@Override
						protected int viewForItem(int section, int row) {
							// TODO Auto-generated method stub
							return R.layout.list_report_summary_product_item;
						}

						@Override
						protected void onViewItem(View view, Object holder,
								int section, int row) {
							// TODO Auto-generated method stub
							// ViewHolder vh = (ViewHolder) holder;
							// vh.txtDate.setText("19/12/2557");
							// vh.txtBF.setText("30");
							// vh.txtImport.setText("10");
							// vh.txtInstall.setText("2");
							// vh.txtChange.setText("1");
							// vh.txtRetrun.setText("1");
							// vh.txtBalance.setText("8");

							try {
								ReportInventoryInfo info = summery.get(row);
								ViewHolder vh = (ViewHolder) holder;
								vh.txtDate.setText(BHUtilities.dateFormat(
										info.ReportDate,
										BHUtilities.DEFAULT_DATE_FORMAT));
								vh.txtBF.setText(BHUtilities
										.numericFormat(info.RemainProduct));
								vh.txtImport.setText(BHUtilities
										.numericFormat(info.PickProduct));
								vh.txtInstall.setText(BHUtilities
										.numericFormat(info.InstallProduct));
								vh.txtChange.setText(BHUtilities
										.numericFormat(info.ChangeProduct));
								vh.txtRetrun.setText(BHUtilities
										.numericFormat(info.ReturnProduct));
								vh.txtBalance.setText(BHUtilities
										.numericFormat(info.BalanceProduct));

								if (row == summery.size() - 1) {
									vh.txtDate.setText("ยอดรวม");

								} else if (row == summery.size() - 2) {
									vh.txtBF.setText("");
									vh.txtImport.setText("");
									vh.txtInstall.setText("");
									vh.txtChange.setText("");
									vh.txtRetrun.setText("");
									vh.txtBalance.setText("");

								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						}

						@Override
						protected int getItemCount(int section) {
							// TODO Auto-generated method stub
							return summery != null ? summery.size() : 0;
						}
					};
				}

				lvSummary.setAdapter(adapter);
			}
		}).start();

	}
	*/

	/*** [START] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
	/*
	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_print:

			break;

		case R.string.button_back:
			showLastView();
			break;

		default:
			break;
		}
	}
	*/
	/*** [END] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/





}
