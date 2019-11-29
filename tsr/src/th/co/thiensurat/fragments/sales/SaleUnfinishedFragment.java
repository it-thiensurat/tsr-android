package th.co.thiensurat.fragments.sales;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.info.MenuInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.share.BarcodeScanFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment.ScanCallBack;

public class SaleUnfinishedFragment extends BHFragment {

	public static class Data extends BHParcelable {
		public String Statuscode;
		public String refno;
		public ProcessType ProcessType;
		public String ProductSerialNumber;
	}

	private Data data;

	@InjectView
	private ListView lvMenu;
	private static final int UNFINISHED = R.array.sale_unfinished_menu;
	int intStatuscode;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_unfinished;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back , R.string.bluetooth_connect};
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		data = getData();
		intStatuscode = Integer.parseInt(data.Statuscode);

		setMenu(UNFINISHED);
		setWidgetsEventListener();
	}

	private void setWidgetsEventListener() {
		// TODO Auto-generated method stub
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MenuInfo info = (MenuInfo) parent.getItemAtPosition(position);
				view.setSelected(true);

				BHPreference.setRefNo(data.refno);
				BHPreference.setProcessType(ProcessType.Sale.toString());

				selectedMenu(position, info.titleID);
			}

			private void selectedMenu(int position, int titleID) {
				// TODO Auto-generated method stub
				switch (titleID) {
					case R.string.sale_unfinished_scan_product:
						if (intStatuscode <= 6) {
							if (intStatuscode >= 1) {
								BarcodeScan();
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังออกสัญญาได้");
						}
						break;
					case R.string.sale_unfinished_employee:
						if (intStatuscode <= 6) {
							if (intStatuscode >= 2) {
								showNextView(BHFragment.newInstance(SaleScanEmployeesFragment.class));
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังออกสัญญาได้");
						}

						break;
					case R.string.sale_unfinished_customer:

						if (intStatuscode <= 6) {
							if (intStatuscode >= 3) {
//							showNextView(BHFragment.newInstance(SaleCustomerAddressMainFragment.class));	// PARADA [Tab-Address]
								showNextView(BHFragment.newInstance(New2SaleCustomerAddressCardFragment.class));    // PARADA [Separate-Address]
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังออกสัญญาได้");
						}

						break;
					case R.string.sale_unfinished_package:

						if (intStatuscode <= 6) {
							if (intStatuscode >= 4) {
								showNextView(BHFragment.newInstance(SaleListPackagePaymentFragment.class));
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังออกสัญญาได้");
						}

						break;
					case R.string.sale_unfinished_discount:

						if (intStatuscode <= 6) {
							if (intStatuscode >= 5) {
								showNextView(BHFragment.newInstance(SalePayFragment.class));
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังออกสัญญาได้");
						}

						break;
					case R.string.sale_unfinished_detail_contract:

						if (intStatuscode <= 6) {
							if (intStatuscode >= 6) {
								showNextView(BHFragment.newInstance(SaleDetailCheckFragment.class));
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังออกสัญญาได้");
						}

						break;
					case R.string.sale_unfinished_contract:

						if (intStatuscode <= 8) {
							if (intStatuscode >= 7) {
								showNextView(BHFragment.newInstance(SaleContractPrintFragment.class));
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังออกสัญญาได้");
						}

						break;
					case R.string.sale_unfinished_payment:

						if (intStatuscode <= 8) {
							if (intStatuscode >= 8) {
								showNextView(BHFragment.newInstance(SaleFirstPaymentChoiceFragment.class));
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังออกสัญญาได้");
						}

						break;
					case R.string.sale_unfinished_receipt:

						if (intStatuscode <= 12) {
							if (intStatuscode >= 9) {
								(new BackgroundProcess(activity) {
									PaymentInfo output;

									@Override
									protected void calling() {
										// TODO Auto-generated method stub
										output = getPaymentRefNo(data.refno);
									}

									@Override
									protected void after() {
										// TODO Auto-generated method stub
										super.after();
										if (output != null) {
										/*SaleReceiptFirstPayment.Data receiptData = new SaleReceiptFirstPayment.Data();
										receiptData.refNo = data.refno;
										receiptData.processType = ProcessType.Sale;
										receiptData.contract = null;// data.contract;
										receiptData.payment = output;
										receiptData.bank = null;// data.bank;
										SaleReceiptFirstPayment fm = BHFragment.newInstance(SaleReceiptFirstPayment.class, receiptData);
										showNextView(fm);*/







									/*		SaleReceiptPayment.Data dataReceiptID = new SaleReceiptPayment.Data();
											dataReceiptID.PaymentID = output.PaymentID;
											SaleReceiptPayment fmReceipt = BHFragment.newInstance(SaleReceiptPayment.class, dataReceiptID);
											showNextView(fmReceipt);*/






											SaleReceiptPayment_old.Data dataReceiptID = new SaleReceiptPayment_old.Data();
											dataReceiptID.PaymentID = output.PaymentID;
											SaleReceiptPayment_old fmReceipt = BHFragment.newInstance(SaleReceiptPayment_old.class, dataReceiptID);
											showNextView(fmReceipt);





										}
									}
								}).start();

							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังใบเสร็จรับเงินได้");
						}

						break;
					case R.string.sale_unfinished_photo:

						if (intStatuscode <= 12) {
							if (intStatuscode >= 11) {
								showNextView(BHFragment.newInstance(SalePhotographyFragment.class));
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังใบเสร็จรับเงินได้");
						}

						break;
					case R.string.sale_unfinished_more:
						if (intStatuscode <= 12) {
							if (intStatuscode >= 12) {
								showNextView(BHFragment.newInstance(SaleMoreDetailAddress.class));
							} else {
								showMessage("สถานะการทำงานของท่านยังมาไม่ถึง");
							}
						} else {
							showMessage("ไม่สามารถย้อนกลับไปแก้ไขข้อมูลหลังใบเสร็จรับเงินได้");
						}

						break;

					default:
						break;
				}
			}
		});
	}

	private void setMenu(int Array) {
		// TODO Auto-generated method stub
		MenuInfo[] menus = MenuInfo.from(Array);
		BHArrayAdapter<MenuInfo> adapter = new BHArrayAdapter<MenuInfo>(activity, R.layout.list_sale_unfinished_menu, menus) {
			class ViewHolder {
				public TextView txtMenu;
				public TextView txtNumber;
				public ImageView arrow;
				public ImageView checklist;
			}

			@Override
			protected void onViewItem(int position, View view, Object holder, MenuInfo info) {
				// TODO Auto-generated method stub
				ViewHolder vh = (ViewHolder) holder;
				vh.txtMenu.setText(info.titleID);

				vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_gray);
				vh.arrow.setVisibility(View.VISIBLE);
				vh.checklist.setVisibility(View.VISIBLE);

				switch (info.titleID) {
				case R.string.sale_unfinished_scan_product:
					vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
					vh.arrow.setVisibility(View.GONE);
					vh.txtNumber.setText("1");

					break;
				case R.string.sale_unfinished_employee:
					if (intStatuscode >= 2) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}

					vh.txtNumber.setText("2");

					break;
				case R.string.sale_unfinished_customer:
					if (intStatuscode >= 3) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}

					vh.txtNumber.setText("3");

					break;
				case R.string.sale_unfinished_package:
					if (intStatuscode >= 4) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}

					vh.txtNumber.setText("4");

					break;
				case R.string.sale_unfinished_discount:
					if (intStatuscode >= 5) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}

					vh.txtNumber.setText("5");

					break;
				case R.string.sale_unfinished_detail_contract:
					if (intStatuscode >= 6) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}

					vh.txtNumber.setText("6");

					break;
				case R.string.sale_unfinished_contract:
					if (intStatuscode >= 7) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}

					vh.txtNumber.setText("7");

					break;
				case R.string.sale_unfinished_payment:
					if (intStatuscode >= 8) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}
					vh.txtNumber.setText("8");

					break;
				case R.string.sale_unfinished_receipt:
					if (intStatuscode >= 9) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}

					vh.txtNumber.setText("9");

					break;
				case R.string.sale_unfinished_photo:
					if (intStatuscode >= 11) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}

					vh.txtNumber.setText("10");

					break;
				case R.string.sale_unfinished_more:
					if (intStatuscode >= 12) {
						vh.txtNumber.setBackgroundResource(R.drawable.circle_number_unfinish_color_green);
						vh.arrow.setVisibility(View.GONE);
					} else {
						vh.checklist.setVisibility(View.GONE);
					}

					vh.txtNumber.setText("11");

					break;

				default:
					break;
				}

			}
		};
		lvMenu.setAdapter(adapter);
		setWidgetsEventListener();
	}

	private void BarcodeScan() {
		// TODO Auto-generated method stub
		BarcodeScanFragment fm = BHFragment.newInstance(BarcodeScanFragment.class, new ScanCallBack() {
			@Override
			public void onResult(final BHParcelable data1) {
				// TODO Auto-generated method stub
				final BarcodeScanFragment.Result barcodeResult = (BarcodeScanFragment.Result) data1;
				(new BackgroundProcess(activity) {
					private ProductStockInfo productInfo;

					@Override
					protected void calling() {
						// TODO Auto-generated method stub

						/*** [START] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย ***/
						//productInfo = getProductStock(barcodeResult.barcode, ProductStockController.ProductStockStatus.CHECKED);

						if(BHPreference.IsSaleForCRD()) {
							productInfo = getProductStockSerialNumberForCRD(barcodeResult.barcode, BHPreference.employeeID());
						} else {
							productInfo = getProductStockSerialNumber(barcodeResult.barcode);
						}
						/*** [END] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย  ***/

					}

					@Override
					protected void after() {
						// TODO Auto-generated method stub

						/*** [START] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย ***/
						/*if (productInfo != null) {
							(new BackgroundProcess(activity) {
								@Override
								protected void before() {
									// TODO Auto-generated method stub
									productInfo.ProductSerialNumber = barcodeResult.barcode;
									productInfo.OrganizationCode = BHPreference.organizationCode();
									// productInfo.Status =
									// ProductStockStatus.SOLD.toString(); //Not
									// update Status now
									productInfo.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.teamCode();
									productInfo.ScanDate = new Date();
								};

								@Override
								protected void calling() {
									// TODO Auto-generated method stub
									// updateProductStockStatus(productInfo);
									updateProductStock(productInfo,true);
								}

								@Override
								protected void after() {
									// TODO Auto-generated method stub
									super.after();
									BHPreference.setProductSerialNumber(productInfo.ProductSerialNumber);
									SaleProductCheckFragment.Data data = new SaleProductCheckFragment.Data();
									data.productID = productInfo.ProductID;
									data.productSerialNumber = productInfo.ProductSerialNumber;
									SaleProductCheckFragment fm = BHFragment.newInstance(SaleProductCheckFragment.class, data);
									showNextView(fm);

								}
							}).start();
						} else {
							showMessage("ไม่พบสินค้ากรุณาทำการสแกนใหม่");
						}*/

						String title = "";
						String message = "";

						if (productInfo != null) {
							String status = productInfo.Status;
							switch (status) {
								case "CHECKED":
									BarcodeScanSuccess(productInfo, barcodeResult.barcode);
									break;
								case "OVER":
									title = "กรุณาตรวจสอบสินค้า";
									message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้าเกิน");
									showWarningDialog(title, message);
									break;
								case "RETURN":
									title = "กรุณาตรวจสอบสินค้า";
									message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้ารีเทิร์น");
									showWarningDialog(title, message);
									break;
								case "SOLD":
									if(data.ProductSerialNumber.equals(productInfo.ProductSerialNumber)) {
										BarcodeScanSuccess(productInfo, barcodeResult.barcode);
									} else {
										title = "กรุณาตรวจสอบสินค้า";
										message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้าถูกขาย");
										showWarningDialog(title, message);
									}
									break;
								case "WAIT":
									title = "กรุณาตรวจสอบสินค้า";
									message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้ารอการตรวจสอบ");
									showWarningDialog(title, message);
									break;
								case "DAMAGE":
									title = "กรุณาตรวจสอบสินค้า";
									message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "เครื่องชำรุด");
									showWarningDialog(title, message);
									break;
								case "TEAM_DESTROY":
									title = "กรุณาตรวจสอบสินค้า";
									message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "ถูกยุบทีมขาย");
									showWarningDialog(title, message);
									break;
								case "WAIT_RETURN":
									title = "กรุณาตรวจสอบสินค้า";
									message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้านี้ถูกส่งคืนเข้าระบบ");
									showWarningDialog(title, message);
									break;
								default:
									break;
							}
						} else {
							title = "กรุณาตรวจสอบสินค้า";
							message = String.format("ไม่พบรหัสสินค้า  %s อยู่ในระบบ", barcodeResult.barcode);
							showWarningDialog(title, message);
						}

						/*** [END] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย  ***/
					}
				}).start();
			}
			// @Override
			// public String onNextClick() {
			// return "SA04580002";
			// }
		});
		fm.setTitle(R.string.title_sales);
		fm.setViewTitle("บันทึกข้อมูลสินค้า");
		showNextView(fm);
	}

	public void BarcodeScanSuccess(final ProductStockInfo productInfo, final String barcode) {
		(new BackgroundProcess(activity) {
			@Override
			protected void before() {
				// TODO Auto-generated method stub
				productInfo.ProductSerialNumber = barcode;
				productInfo.OrganizationCode = BHPreference.organizationCode();
				// productInfo.Status =
				// ProductStockStatus.SOLD.toString(); //Not
				// update Status now
				productInfo.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.teamCode();
				productInfo.ScanDate = new Date();
			};

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				// updateProductStockStatus(productInfo);
				updateProductStock(productInfo,true);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				super.after();
				BHPreference.setProductSerialNumber(productInfo.ProductSerialNumber);
				SaleProductCheckFragment.Data data = new SaleProductCheckFragment.Data();
				data.productID = productInfo.ProductID;
				data.productSerialNumber = productInfo.ProductSerialNumber;
				SaleProductCheckFragment fm = BHFragment.newInstance(SaleProductCheckFragment.class, data);
				showNextView(fm);

			}
		}).start();
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			showLastView();
			break;
		default:
			break;
		}
	}
}
