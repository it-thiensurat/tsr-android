package th.co.thiensurat.fragments.credit.Spare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.PartSpareController;
import th.co.thiensurat.data.info.PartSpareInfo;
import th.co.thiensurat.data.info.SpareDrawdownDetailInfo;
import th.co.thiensurat.data.info.SpareDrawdownInfo;

public class RequestSpareDrawdownListFragment extends BHFragment {

    @InjectView
    private EditText txtSearch;

    @InjectView
    private ListView lvSpareList;

    private List<PartSpareInfo> partSpareInfoList;
    private List<PartSpareInfo> searchResults;

    PartSpareAdapter partSpareAdapter;


    @Override
    protected int fragmentID() {
        return R.layout.fragment_request_spare_drawdown_list;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_request_drawdown};
    }

    @Override
    protected int titleID() {
        return R.string.title_request_spare_drawdown;
    }


    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        partSpareInfoList = new PartSpareController().getPartSpareUNIONProduct(BHPreference.organizationCode());

        if (partSpareInfoList != null && partSpareInfoList.size() > 0) {

            int i = 0;
            for (PartSpareInfo info : partSpareInfoList) {
                info.No = i;
                i++;
            }

            searchResults = new ArrayList<PartSpareInfo>(partSpareInfoList);
            partSpareAdapter = new PartSpareAdapter(activity, R.layout.request_spare_drawdown_list_item, searchResults);
            lvSpareList.setAdapter(partSpareAdapter);
        } else {
            showWarningDialog("แจ้งเตือน", "ไม่พอข้อมูลอะไหล่");
        }

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub
                //get the text in the EditText
                String searchString = editable.toString();
                int textLength = searchString.length();

                //clear the initial data set
                for (PartSpareInfo info : searchResults) {
                    partSpareInfoList.get(info.No).QTY = info.QTY;
                }

                searchResults.clear();

                if (textLength == 0) {
                    searchResults.addAll(partSpareInfoList);
                } else {
                    for (int i = 0; i < partSpareInfoList.size(); i++) {
                        String partSpareName = partSpareInfoList.get(i).PartSpareName.toString();
                        if (textLength <= partSpareName.length()) {
                            //compare the String in EditText with Names in the ArrayList
                            /*if (searchString.equalsIgnoreCase(playerName.substring(0, textLength)))
                                searchResults.add(partSpareInfoList.get(i));*/

                            if (partSpareName.toUpperCase().indexOf(searchString.toUpperCase()) > -1) {
                                searchResults.add(partSpareInfoList.get(i));
                            }
                        }
                    }
                }
                partSpareAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_request_drawdown:

                for (PartSpareInfo info : searchResults) {
                    partSpareInfoList.get(info.No).QTY = info.QTY;
                }

                final List<PartSpareInfo> newPartSpareInfoList = new ArrayList<PartSpareInfo>();

                for (int i = 0; i < partSpareInfoList.size(); i++) {
                    if (partSpareInfoList.get(i).QTY != 0) {
                        newPartSpareInfoList.add(partSpareInfoList.get(i));
                    }
                }

                if (newPartSpareInfoList.size() > 0) {
                    displayAlertDialog(newPartSpareInfoList);
                } else {
                    showWarningDialog("แจ้งเตือน", "ไม่พบการขอเบิกอะไหล่");
                }

                break;
            default:
                break;
        }
    }

    public class PartSpareAdapter extends BHArrayAdapter<PartSpareInfo> {

        public PartSpareAdapter(Context context, int resource, List<PartSpareInfo> objects) {
            super(context, resource, objects);
        }


        private class ViewHolder {
            public TextView txtPartSpareCode, txtPartSpareName, txtUnit, txtNo;
            public EditText editTextQTY;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, PartSpareInfo info) {
            final ViewHolder vh = (ViewHolder) holder;

            vh.txtNo.setText(String.valueOf(position));
            vh.txtPartSpareCode.setText(info.PartSpareCode != null ? info.PartSpareCode : "");
            vh.txtPartSpareName.setText(info.PartSpareName);
            vh.editTextQTY.setText(String.valueOf(info.QTY));
            vh.txtUnit.setText(info.PartSpareUnit);

            vh.editTextQTY.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // TODO Auto-generated method stub
                    String strQty = vh.editTextQTY.getText().toString();
                    int qty = Integer.parseInt(!strQty.equals("") ? strQty : "0");
                    int no = Integer.parseInt(vh.txtNo.getText().toString());

                    searchResults.get(no).QTY = qty;
                }
            });
        }
    }

    private void saveRequestSpareDrawdown(final List<PartSpareInfo> psinfo, final String RequestDetail) {

        (new BackgroundProcess(activity) {
            SpareDrawdownInfo sdInfo = new SpareDrawdownInfo();
            List<SpareDrawdownDetailInfo> sddInfoList = new ArrayList<SpareDrawdownDetailInfo>();

            Date newDate = new Date();


            @Override
            protected void before() {
                super.before();

                sdInfo.SpareDrawdownID = DatabaseHelper.getUUID();
                sdInfo.OrganizationCode = BHPreference.organizationCode();
                sdInfo.Status = SpareDrawdownInfo.SpareDrawdownStatus.REQUEST.toString();
                sdInfo.RequestDate = newDate;
                sdInfo.RequestBy = BHPreference.employeeID();
                sdInfo.RequestTeamCode = BHPreference.teamCode();
                sdInfo.RequestDetail = RequestDetail;
                //sdInfo.ApprovedDate = newDate;
                //sdInfo.ApprovedBy = "";
                //sdInfo.ApproveDetail = "";
                //sdInfo.EffectiveBy = "";
                //sdInfo.EffectiveDetail = "";
                //sdInfo.EffectiveDetail = "";
                //sdInfo.SpareDrawdownPaperID = "";
                sdInfo.PrintCount = 0;
                sdInfo.CreateDate = newDate;
                sdInfo.CreateBy = BHPreference.employeeID();
                sdInfo.LastUpdateDate = newDate;
                sdInfo.LastUpdateBy = BHPreference.employeeID();
                //sdInfo.SyncedDate = newDate;

                for (PartSpareInfo info : psinfo) {
                    SpareDrawdownDetailInfo sddInfo = new SpareDrawdownDetailInfo();
                    sddInfo.SpareDrawdownID = sdInfo.SpareDrawdownID;
                    sddInfo.PartSpareIDOrProductID = info.PartSpareID;
                    sddInfo.IsPartSpare = info.IsPartSpare;
                    sddInfo.RequestDetail = RequestDetail;
                    sddInfo.RequestQTY = info.QTY;
                    //sddInfo.ApproveDetail = "";
                    //sddInfo.ApproveQTY = 0;
                    sddInfo.CreateDate = newDate;
                    sddInfo.CreateBy = BHPreference.employeeID();
                    sddInfo.LastUpdateDate = newDate;
                    sddInfo.LastUpdateBy = BHPreference.employeeID();
                    //sddInfo.SyncedDate = newDate;

                    sddInfoList.add(sddInfo);
                }
            }

            @Override
            protected void calling() {
                TSRController.addSpareDrawdown(sdInfo, true);

                for (SpareDrawdownDetailInfo info : sddInfoList) {
                    TSRController.addSpareDrawdownDetail(info, true);
                }

            }

            @Override
            protected void after() {
                super.after();

                activity.showView(BHFragment.newInstance(SpareDrawdownMainFragment.class));
            }
        }).start();
    }

    private void displayAlertDialog(final List<PartSpareInfo> partSpareList) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.request_spare_drawdown_dialog, null);
        final ListView lvSummaryDetails = (ListView) alertLayout.findViewById(R.id.lvSummaryDetails);
        final EditText editTextRequestDetail = (EditText) alertLayout.findViewById(R.id.editTextRequestDetail);

        PartSpareSummaryDetailsAdapter partSpareSummaryDetailsAdapter = new PartSpareSummaryDetailsAdapter(activity, R.layout.request_spare_drawdown_summary_details_list_item, partSpareList);
        lvSummaryDetails.setAdapter(partSpareSummaryDetailsAdapter);

        AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle(String.format("สรุปขอเบิกเครื่อง/อะไหล่ %d รายการ", partSpareList.size()))
                .setView(alertLayout)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String requestDetail = editTextRequestDetail.getText().toString();

                        if (requestDetail != null && !requestDetail.isEmpty() && !requestDetail.trim().isEmpty()) {
                            saveRequestSpareDrawdown(partSpareList, requestDetail);
                        } else {
                            dialog.cancel();
                            alertDialog(partSpareList);
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        setupAlert.show();
    }

    private void alertDialog(final List<PartSpareInfo> partSpareList) {
        AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle("แจ้งเตือน")
                .setMessage("กรุณากรอก รายละเอียด การขอเบิกเครื่อง/อะไหล่")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        displayAlertDialog(partSpareList);
                    }
                });
        setupAlert.show();
    }

    public class PartSpareSummaryDetailsAdapter extends BHArrayAdapter<PartSpareInfo> {

        public PartSpareSummaryDetailsAdapter(Context context, int resource, List<PartSpareInfo> objects) {
            super(context, resource, objects);
        }


        private class ViewHolder {
            public TextView txtPartSpareCode, txtPartSpareName, txtQTY, txtUnit;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, PartSpareInfo info) {
            final ViewHolder vh = (ViewHolder) holder;

            vh.txtPartSpareCode.setText(info.PartSpareCode != null ? info.PartSpareCode : "");
            vh.txtPartSpareName.setText(info.PartSpareName);
            vh.txtQTY.setText(String.valueOf(info.QTY));
            vh.txtUnit.setText(info.PartSpareUnit);
        }
    }

}
