package th.co.thiensurat.fragments.employee.credit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.PositionController;
import th.co.thiensurat.data.controller.SubTeamController;
import th.co.thiensurat.data.controller.TripController;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.SubTeamInfo;
import th.co.thiensurat.data.info.TripInfo;

public class EmployeeDetailFragment extends BHFragment {

    @InjectView
    private ViewPager viewPager;
    @InjectView
    private LinearLayout linearLayoutDots;
    @InjectView
    TextView txtSaleLeaderTeamCode, txtSaleLeaderName, txtSupervisorName, txtLineManagerName,
            txtManagerName, txtTeamCode, txtTripNumber, txtTripDate;
    @InjectView
    TextView txtSupervisorTitle, txtLineManagerTitle, txtManagerTitle;
    TextView[] dots;

    private MyViewPagerAdapter myViewPagerAdapter;
    List<SubTeamInfo> memberOfSubTeamList;

    @Override
    protected int titleID() {
        return R.string.title_team_credit;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_employee_detail;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        Log.e("hhhh","1111");
        settingUI();
    }

    public void settingUI() {
        (new BackgroundProcess(activity) {
            TripInfo trip = null;
            EmployeeDetailInfo employeeDetail = null;
            EmployeeDetailInfo teamHeadDetail = null;

            @Override
            protected void calling() {
                trip = new TripController().getCurrentTrip();
                // Employee Login Detail

                employeeDetail = new EmployeeDetailController().getEmployeeDetailByTeamCodeByEmployeeID(BHPreference.organizationCode(), BHPreference.employeeID(), BHPreference.teamCode());
                Log.e("hhhh","2222");

                if (BHPreference.teamCode() != null && !BHPreference.teamCode().equals("")) {
                    // TeamHead Detail
                    teamHeadDetail = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                    List<SubTeamInfo> subTeamList;
                    // Check Position Level >= SaleLeader ? AllTeam : SubTeamCode [Who's login IF Position Level < SaleLeader, SubTeamCode is not null]
                    if(employeeDetail != null && employeeDetail.SubTeamCode != null && !employeeDetail.SubTeamCode.equals("")){
                        Log.e("hhhh","3333");

                        subTeamList = new SubTeamController().getSubTeamBySubTeamCode(employeeDetail.SubTeamCode);
                    }else{

                        subTeamList = new SubTeamController().getSubTeamByTeamCode(BHPreference.teamCode());
                    }

                    if(subTeamList != null && subTeamList.size() > 0){
                        memberOfSubTeamList = new ArrayList<SubTeamInfo>();
                        for(SubTeamInfo subTeam : subTeamList){
                            // ถ้าคน Login มีตำแหน่งเป็นพนักงานขายเพียงตำแหน่งเดียว
                            List<EmployeeDetailInfo> subTeamMember;
                            if(BHPreference.PositionCode().equals(PositionController.PositionCode.Credit.toString())){
                                Log.e("hhhh","4444");
                              //  subTeamMember = new EmployeeDetailController().getSubTeamMemberByEmpID(BHPreference.organizationCode(), subTeam.SubTeamCode
                                  //      , BHPreference.employeeID(), BHPreference.PositionCode());

                                subTeamMember = new EmployeeDetailController().getSubTeamMemberByEmpID(BHPreference.organizationCode(), BHPreference.SubTeamCode()
                                        , BHPreference.employeeID(), BHPreference.PositionCode());

                            }else{
                                Log.e("hhhh","5555");
                               // subTeamMember = new EmployeeDetailController().getSubTeamMemberBySubTeamCode(BHPreference.organizationCode(), subTeam.SubTeamCode);

                                subTeamMember = new EmployeeDetailController().getSubTeamMemberBySubTeamCode(BHPreference.organizationCode(), BHPreference.SubTeamCode());
                            }
                            if(subTeamMember != null && subTeamMember.size() > 0){
                                subTeam.Member = new ArrayList<EmployeeDetailInfo>();
                                subTeam.Member.addAll(subTeamMember);
                                memberOfSubTeamList.add(subTeam);
                            }
                        }
                    }
                }
            }

            @Override
            protected void after() {
                // Fixed - [BHPROJ-0024-407]
                /*
                if (trip != null) {
                    String lblTeamCode = String.format("ทีม %s", BHPreference.teamCode() != null ? BHPreference.teamCode() : "");
                    txtTeamCode.setText(lblTeamCode);
                    txtTripNumber.setText(String.format("ทริปที่ %d/%s", trip.TripNumber, BHUtilities.dateFormat(trip.StartDate, "yyyy")));
                    txtTripDate.setText(String.format("%s - %s", BHUtilities.dateFormat(trip.StartDate), BHUtilities.dateFormat(trip.EndDate)));
                }
                */
                String lblTeamCode = String.format("ทีม %s", BHPreference.teamCode() != null ? BHPreference.teamCode() : "");
                txtTeamCode.setText(lblTeamCode);
                if (trip != null) {
                    txtTripNumber.setText(String.format("ทริปที่ %d/%s", trip.TripNumber, BHUtilities.dateFormat(trip.StartDate, "yyyy")));
                    txtTripDate.setText(String.format("%s - %s", BHUtilities.dateFormat(trip.StartDate), BHUtilities.dateFormat(trip.EndDate)));
                }

                if (teamHeadDetail != null) {
                    txtSaleLeaderTeamCode.setText(String.format("%s %s", teamHeadDetail.PositionName, teamHeadDetail.TeamCode));
                    txtSaleLeaderName.setText(String.format("ชื่อ %s", teamHeadDetail.EmployeeName));
                    txtSupervisorName.setText(teamHeadDetail.SupervisorHeadName);
                    txtLineManagerName.setText(teamHeadDetail.SubDepartmentHeadName);
                    txtManagerName.setText(teamHeadDetail.DepartmentHeadName);
                }

                if(memberOfSubTeamList != null && memberOfSubTeamList.size() > 0){
                    myViewPagerAdapter = new MyViewPagerAdapter(activity, memberOfSubTeamList);
                    viewPager.setAdapter(myViewPagerAdapter);
                    viewPager.setCurrentItem(0);
                    viewPager.setOnPageChangeListener(new ViewPagerPageChangeListener());

                    int dotsCount = memberOfSubTeamList.size();
                    dots = new TextView[dotsCount];

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i] = new TextView(activity);
                        dots[i].setText(Html.fromHtml("&#149;"));
                        dots[i].setTextSize(30);
                        dots[i].setTextColor(getResources().getColor(R.color.dot_gray_dark));
                        linearLayoutDots.addView(dots[i]);
                    }

                    dots[0].setTextColor(getResources().getColor(R.color.dot_red));
                }
            }
        }).start();
    }

    public class ViewPagerPageChangeListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < memberOfSubTeamList.size(); i++) {
                dots[i].setTextColor(getResources().getColor(R.color.dot_gray_dark));
            }
            dots[position].setTextColor(getResources().getColor(R.color.dot_red));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;
        private Context mContext;
        private List<SubTeamInfo> subTeamInfos;

        public MyViewPagerAdapter(Context mContext, List<SubTeamInfo> subTeamInfos) {
            this.mContext = mContext;
            this.subTeamInfos = subTeamInfos;
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.list_credit_employee_detail, container, false);

            TextView txtSubTeamName = (TextView) itemView.findViewById(R.id.txtSubTeamName);
            ListView lvEmployee = (ListView) itemView.findViewById(R.id.lvEmployee);

            SubTeamInfo subTeamInfo = subTeamInfos.get(position);

            txtSubTeamName.setText(subTeamInfo.SubTeamName);
            EmployeeDetailAdapter employeeDetailAdapterAdapter = new EmployeeDetailAdapter(activity, R.layout.list_employee_team_list_item,
                    subTeamInfo.Member);
            lvEmployee.setAdapter(employeeDetailAdapterAdapter);

            setListViewHeightBasedOnChildren(lvEmployee);

            ((ViewPager) container).addView(itemView);
            return itemView;
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
            return subTeamInfos.size();
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

    public class EmployeeDetailAdapter extends BHArrayAdapter<EmployeeDetailInfo>
    {
        public EmployeeDetailAdapter(Context context, int resource, List<EmployeeDetailInfo> objects) {
            super(context, resource, objects);
        }

        class ViewHolder {
            public TextView txtTitle, txtPosition;
            public ImageView imgIcon;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, EmployeeDetailInfo info) {
            ViewHolder vh = (ViewHolder) holder;

            String titleString = String.format("%s\nชื่อ: %s", info.SaleCode != null ? info.SaleCode : info.EmployeeCode, info.EmployeeName);
            String positionNameString = String.format("\n%s", info.PositionName);

            vh.txtTitle.setText(titleString);
            vh.txtPosition.setText(positionNameString);
        }
    }
}
