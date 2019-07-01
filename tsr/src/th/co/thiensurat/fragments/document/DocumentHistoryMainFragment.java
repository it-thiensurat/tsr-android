package th.co.thiensurat.fragments.document;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.viewpagerindicator.TabPageIndicator;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TransactionService;


public class DocumentHistoryMainFragment extends BHFragment {

	public int currentTab = 0;
		
	@InjectView
	private ViewPager vpDocument;
	
	@InjectView
	private TabPageIndicator tsDocument;
	
	FragmentPagerAdapter adapter;
	private DocumentHistoryDetailFragment[] childs;
	private final String[] PAGE_TITLE = { "ใบสัญญา", "ใบเสร็จ", "ใบเปลี่ยนเครื่อง", "ใบถอดเครื่อง", "ใบเปลี่ยนสัญญา", "เอกสารมือ", "Slip ธนาคาร", "Slip เพย์พอยท์"};
	
	
	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_document_main;
	}
	
	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_document_main;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_update_list, R.string.button_conclude, R.string.button_save };
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		childs = new DocumentHistoryDetailFragment[PAGE_TITLE.length];
		
		for (int ii = 0; ii < PAGE_TITLE.length; ii++) {
			DocumentHistoryDetailFragment fm = BHPagerFragment.newInstance(DocumentHistoryDetailFragment.class, null, DocumentHistoryMainFragment.this);
			fm.documentType = Integer.toString(ii);
			childs[ii] = fm; 
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setWidgetsEventListener();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	
	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		final DocumentHistoryDetailFragment fm = childs[currentTab];
		switch (buttonID) {
		
		case R.string.button_update_list:
			//DocumentHistoryDetailFragment fm = (DocumentHistoryDetailFragment)adapter.getItem(currentTab);
			//fm.process(currentTab);
			//fm.bindSyncData(Integer.toString(currentTab), "1");
			//fm.updateData(false);
            startSync();
			break;
		case R.string.button_conclude:
			DocumentHistoryPrintFragment fmPrint = BHFragment.newInstance(DocumentHistoryPrintFragment.class);
			showNextView(fmPrint);
			break;
			
		case R.string.button_save:
			(new BackgroundProcess(activity) {
				@Override
				protected void calling() {
					fm.updateData(true);
				}
			}).start();

			break;

		default:
			break;	
		}
	}

	private void setWidgetsEventListener() {

		//final String[] PAGE_TITLE = { "ใบสัญญา" };

		
		adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
			//private final int PAGE_COUNT = 6;

			
			@Override
			public CharSequence getPageTitle(int position) {
				return PAGE_TITLE[position];
			}
			
			@Override
			public int getCount() {	
				
				// TODO Auto-generated method stub
				return PAGE_TITLE.length;
			}
			
			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				return childs[position];
			}		
		};
		
		vpDocument.setAdapter(adapter);
		tsDocument.setViewPager(vpDocument);
		//tsDocument.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, activity.getResources().getDisplayMetrics()));
		tsDocument.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
//				if (position == 0) {
//					//activity.menu.removeIgnoredView(vpDocument);
////					fmHistoryDetail.TypeTab = position;
//					activity.menu.removeIgnoredView(vpDocument);
//				} else {
//					activity.menu.addIgnoredView(vpDocument);
//				}
				currentTab = position;
//				DocumentHistoryDetailFragment fm = (DocumentHistoryDetailFragment)adapter.getItem(position);
//				fm.process(position);

			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
 	}

    private class SynchronizeReceiver extends BroadcastReceiver implements MainActivity.IApiAccessResponse{
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

                    if (result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                        showWarningDialog(result.error);
                    } else {
						if(result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED){
							MainActivity.checkLogin = true;
							//
							final MainActivity.DownloadTask downloadTask = new MainActivity.DownloadTask(activity);
							String URL = String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip");
							downloadTask.delegate = this;
							downloadTask.execute(URL);

							downloadTask.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									downloadTask.cancel(true);
								}
							});
						}
					}

                    stop();
                }

            }
        }

		@Override
		public void postResult(String asyncresult) {
			activity.showView(BHFragment.newInstance(DocumentHistoryMainFragment.class));
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
        /*request.master.syncTeamRelated = false;
        request.master.syncProductRelated = false;
        request.master.syncCustomerRelated = false;
        request.master.syncPaymentRelated = false;
        request.master.syncContractRelated = true;
        request.master.syncEditContractRelated = false;
        request.master.syncSendMoneyRelated = false;
        request.master.syncMasterDataRelated = false;*/
		request.master.syncFullRelated = true;

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);
    }


}
