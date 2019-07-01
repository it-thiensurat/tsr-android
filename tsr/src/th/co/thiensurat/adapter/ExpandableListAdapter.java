package th.co.thiensurat.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import th.co.bighead.utilities.BHExpandableListAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.R;
import th.co.thiensurat.data.info.EmployeeDetailInfo;

public class ExpandableListAdapter extends BHExpandableListAdapter {
    private Context context;

    private class GroupViewHolder {
        public TextView txtHeader;
        public ImageView toggleGroup;
        public LinearLayout layoutEmp;
        public RelativeLayout layoutHeader;
    }

    private class ChildViewHolder {
        public TableLayout tbChild;
    }

    public ExpandableListAdapter(Context _context, int _resourceHeader, int _resourceChild, List<ExpandableHeader> _listDataHeader, HashMap<ExpandableHeader, Table> _listDataChild) {
        super(_context, _resourceHeader, _resourceChild, _listDataHeader, _listDataChild);
        this.context = _context;
    }

    @Override
    protected void onGroupViewItem(int groupPosition, boolean isExpanded, View view, Object holder, ExpandableHeader info) {
        GroupViewHolder vh = (GroupViewHolder) holder;
        vh.layoutHeader.setVisibility(info.isEmpLayout() ? View.GONE : View.VISIBLE);
        vh.layoutEmp.setVisibility(info.isEmpLayout() ? View.VISIBLE : View.GONE);
        if (vh.txtHeader.getVisibility() == View.VISIBLE) vh.txtHeader.setText(info.getText());
        if (vh.toggleGroup.getVisibility() == View.VISIBLE) vh.toggleGroup.setImageResource(isExpanded ? R.drawable.ic_toggle_off : R.drawable.ic_toggle_on);
        if (vh.layoutEmp.getVisibility() == View.VISIBLE) {
            EmployeeDetailInfo emp = info.getEmp();
            if(emp == null) emp = new EmployeeDetailInfo();
            ((TextView) vh.layoutEmp.findViewById(R.id.txtEmployeeFullName)).setText(isNull(emp.EmployeeName));
            ((TextView) vh.layoutEmp.findViewById(R.id.txtPositionName)).setText(isNull(emp.PositionName));
            ((TextView) vh.layoutEmp.findViewById(R.id.txtSaleLeaderTeamCode)).setText(String.format("หัวหน้าทีม %s : ", isNull(emp.TeamCode)));
            ((TextView) vh.layoutEmp.findViewById(R.id.txtSaleLeaderName)).setText(isNull(emp.TeamHeadName));
            ((TextView) vh.layoutEmp.findViewById(R.id.txtSupervisorTitle)).setText(String.format("ซุปฯ %s : ", isNull(emp.SupervisorCode)));
            ((TextView) vh.layoutEmp.findViewById(R.id.txtSupervisorName)).setText(isNull(emp.SupervisorHeadName));
            ((TextView) vh.layoutEmp.findViewById(R.id.txtLineManagerTitle)).setText(String.format("ผู้จัดการสาย %s : ", isNull(emp.SubDepartmentCode)));
            ((TextView) vh.layoutEmp.findViewById(R.id.txtLineManagerName)).setText(isNull(emp.SubDepartmentHeadName));
            ((TextView) vh.layoutEmp.findViewById(R.id.txtManagerTitle)).setText(String.format("ผู้จัดการฝ่าย %s : ", isNull(emp.DepartmentCode)));
            ((TextView) vh.layoutEmp.findViewById(R.id.txtManagerName)).setText(isNull(emp.DepartmentHeadName));

            if(((TextView) vh.layoutEmp.findViewById(R.id.txtDate)) != null)
                ((TextView) vh.layoutEmp.findViewById(R.id.txtDate)).setText(String.format("ยอดขาย ณ %s", BHUtilities.dateFormat(emp.ReportDate)));
            if(((TextView) vh.layoutEmp.findViewById(R.id.txtSaleSumTotal)) != null)
                ((TextView) vh.layoutEmp.findViewById(R.id.txtSaleSumTotal)).setText(BHUtilities.numericFormat(emp.SaleSumTotal));
            if(((TextView) vh.layoutEmp.findViewById(R.id.txtSaleSumMoney)) != null)
                ((TextView) vh.layoutEmp.findViewById(R.id.txtSaleSumMoney)).setText(BHUtilities.numericFormat(emp.SaleSumMoney));

            if (info.isNotCollapse()) vh.layoutEmp.setOnClickListener(null);
        }
    }

    public String isNull(String s) {
        return s == null ? "" : s;
    }

    @Override
    protected void onChildViewItem(int groupPosition, int childPosition, boolean isLastChild, View view, Object holder, Table info) {
        ChildViewHolder vh = (ChildViewHolder) holder;
        if (vh.tbChild.getChildCount() > 0) vh.tbChild.removeAllViews();
        int r = 0;
        for (Row row : info.getRows()) {
            TableRow tr = new TableRow(this.context);
            TableLayout.LayoutParams tr_params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            if (r == (info.getRows().size() - 1))
                tr_params.setMargins(0, 0, 0, 10);
            tr.setLayoutParams(tr_params);
            tr.setOnClickListener(null);
            boolean isFirst;
            boolean isLast;
            int i = 0;
            for (Column c : row.getFields()) {
                isFirst = i == 0 ? true : false;
                isLast = i == (row.getFields().size() - 1) ? true : false;
                int resource = isFirst && isLast ? R.style.TextView_Label_Expandable : isFirst ? R.style.TextView_Label_Expandable_first : isLast ? R.style.TextView_Label_Expandable_last : R.style.TextView_Label_Expandable_LayoutParams;
                TextView tv = new TextView(context);
                tv.setText(c.getText());
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tv.setTextColor(this.context.getResources().getColor(android.R.color.black));

                TypedArray ta = this.context.obtainStyledAttributes(resource, R.styleable.ExpandableStyle);
                try {
                    int margin_top = ta.getDimensionPixelSize(R.styleable.ExpandableStyle_layoutMarginTop, 0);
                    int margin_bottom = ta.getDimensionPixelSize(R.styleable.ExpandableStyle_layoutMarginBottom, 0);
                    int margin_left = ta.getDimensionPixelSize(R.styleable.ExpandableStyle_layoutMarginLeft, 0);
                    int margin_right = ta.getDimensionPixelSize(R.styleable.ExpandableStyle_layoutMarginRight, 0);

                    TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
                    params.setMargins(margin_left, margin_top, margin_right, margin_bottom);
                    params.weight = 1;

                    if (c.getStyle() != null) {
                        if (c.getStyle().getColor() > Integer.MIN_VALUE)
                            tv.setTextColor(this.context.getResources().getColor(c.getStyle().getColor()));
                        if (c.getStyle().getTypeface() > Integer.MIN_VALUE)
                            tv.setTypeface(null, Typeface.BOLD);
                        if (c.getStyle().getGravity() > Integer.MIN_VALUE)
                            tv.setGravity(c.getStyle().getGravity());
                        if (c.getStyle().getLayout_weight() > Float.MIN_VALUE)
                            params.weight = c.getStyle().getLayout_weight();
                    }

                    tv.setLayoutParams(params);
                    tr.addView(tv);
                } finally {
                    ta.recycle();
                }
                i++;
            }
            vh.tbChild.addView(tr);
            if (row.isLine()) {
                View v = new View(this.context);
                v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                v.setBackgroundColor(Color.rgb(192, 192, 192));
                vh.tbChild.addView(v);
            }
            r++;
        }
    }
}
