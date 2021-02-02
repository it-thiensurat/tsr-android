package th.co.thiensurat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.R;
import th.co.thiensurat.data.info.EmployeeInfo;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.Holder>  {

    private int position = -1;
    private ItemClickListener mClickListener;
    List<EmployeeInfo> employeeLis = new ArrayList<EmployeeInfo>();

    public EmployeeAdapter(Context context, List<EmployeeInfo> employeeListTemp) {
        Log.e("Emp size", employeeListTemp.size() + "");
        this.employeeLis = employeeListTemp;
    }

    public void setBackgroundItemClick(int pos) {
        this.position = pos;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_cardview, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        EmployeeInfo employeeInfo = employeeLis.get(i);
        holder.textEmpId.setText(employeeInfo.SaleCode);
        holder.textEmpName.setText(employeeInfo.FirstName + " " + employeeInfo.LastName);
        if (i == position) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#21afef"));
            holder.textEmpId.setTextColor(Color.WHITE);
            holder.textEmpName.setTextColor(Color.WHITE);
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
            holder.textEmpId.setTextColor(Color.BLACK);
            holder.textEmpName.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return employeeLis.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textEmpId, textEmpName;
        CardView cardView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            textEmpId = (TextView) itemView.findViewById(R.id.txtEmpID);
            textEmpName = (TextView) itemView.findViewById(R.id.txtEmpName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public EmployeeInfo getItem(int id) {
        return employeeLis.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
