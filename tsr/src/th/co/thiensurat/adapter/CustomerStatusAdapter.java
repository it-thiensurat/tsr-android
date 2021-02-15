package th.co.thiensurat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.R;
import th.co.thiensurat.data.info.CustomerStatusInfo;

public class CustomerStatusAdapter extends RecyclerView.Adapter<CustomerStatusAdapter.Holder> {

    private JSONArray CustomerAddress;
    private List<CustomerStatusInfo> customerStatusInfoList = new ArrayList<CustomerStatusInfo>();
    private ItemClickListener mClickListener;

    public CustomerStatusAdapter(List<CustomerStatusInfo> list, JSONArray array) {
        this.customerStatusInfoList = list;
        this.CustomerAddress = array;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_status_cardview, viewGroup, false);
        CustomerStatusAdapter.Holder holder = new CustomerStatusAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        CustomerStatusInfo customerStatusInfo = customerStatusInfoList.get(i);
        holder.txtNumber.setText("เลขที่สัญญา: " + customerStatusInfo.getCONTNO().trim());
        holder.txtDate.setText(customerStatusInfo.getCustomerStatus().trim() + " (" + customerStatusInfo.getAgingCumulativeDetail().trim() + ")\n" + customerStatusInfo.getStDate().trim());
        holder.txtInstall.setText("วันที่ติดตั้ง: " + customerStatusInfo.getEffDate().trim());
        holder.txtName.setText(customerStatusInfo.getPrefixName().trim() + customerStatusInfo.getCustomerName().trim());
        holder.txtProduct.setText(customerStatusInfo.getProductName().trim());
        holder.txtModel.setText(customerStatusInfo.getProductModel().trim());
        try {
            JSONObject jsonObject = customerStatusInfo.getCustomerAddress().getJSONObject(0);
            String address = jsonObject.getString("AddressDetail")  + " " + jsonObject.getString("AddressDetail2") + " "
                    + jsonObject.getString("AddressDetail3") + " " + jsonObject.getString("AddressDetail4") + "\n"
                    + jsonObject.getString("District") + " " + jsonObject.getString("Amphur") + "\n"
                    + jsonObject.getString("Province") + " " + jsonObject.getString("Zipcode");
            holder.txtAddress.setText(address.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Exception address", e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        return customerStatusInfoList.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtProduct, txtModel;
        TextView txtNumber, txtDate, txtName, txtInstall, txtAddress;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtInstall = (TextView) itemView.findViewById(R.id.txtInstall);
            txtProduct = (TextView) itemView.findViewById(R.id.txtProduct);
            txtModel = (TextView) itemView.findViewById(R.id.txtModel);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
