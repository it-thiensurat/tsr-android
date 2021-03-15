package th.co.thiensurat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.R;
import th.co.thiensurat.data.info.SupvalidateInfo;

public class SupValidateAdapter extends RecyclerView.Adapter<SupValidateAdapter.Holder> {
    private static final int UNSELECTED = -1;

    private Context context;
    private int selectedItem = UNSELECTED;
    private ItemClickListener mClickListener;
    private List<SupvalidateInfo> supvalidateInfoList = new ArrayList<SupvalidateInfo>();

    private SupValidateItemAdapter supValidateItemAdapter;

    public SupValidateAdapter(Context context, List<SupvalidateInfo> supvalidateInfoList) {
        this.context = context;
        this.supvalidateInfoList = supvalidateInfoList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supvalidate_cardview, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        SupvalidateInfo supvalidateInfo = supvalidateInfoList.get(i);

        Date date = null;
        String pattern = "dd/mm/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            date = simpleDateFormat.parse(supvalidateInfo.effdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtContno.setText("เลขที่สัญญา " + supvalidateInfo.contno);
        holder.txtEffDate.setText("วันทำสัญญา " + supvalidateInfo.effdate.replace(".000", ""));
        holder.customerName.setText(supvalidateInfo.customername);
        holder.productName.setText(supvalidateInfo.productname);
        holder.productModel.setText(supvalidateInfo.productmodel);
        holder.productSerial.setText(supvalidateInfo.productserial);

        if (supvalidateInfo.getStatusId() == 3) {
            holder.header_layout.setBackgroundColor(context.getResources().getColor(R.color.bg_sub_title_red));
        } else {
            holder.header_layout.setBackgroundColor(context.getResources().getColor(R.color.new_primary_color));
        }

        holder.txtComment.setText("**" + supvalidateInfo.getStatusComment() + "**");
        holder.recyclerview.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerview.setHasFixedSize(true);
        supValidateItemAdapter = new SupValidateItemAdapter(context, supvalidateInfo.getSupvalidateItemInfoList());
        holder.recyclerview.setAdapter(supValidateItemAdapter);
        supValidateItemAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return supvalidateInfoList.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {

        TextView txtContno, txtEffDate, customerName, productName, productModel, productSerial, txtComment;
        ExpandableLayout expandable_layout;
        RecyclerView recyclerview;
        LinearLayout header_layout;

        public Holder(@NonNull View itemView) {
            super(itemView);
            txtComment = (TextView) itemView.findViewById(R.id.txtComment);
            txtContno = (TextView) itemView.findViewById(R.id.txtContno);
            txtEffDate = (TextView) itemView.findViewById(R.id.txtEffDate);
            customerName = (TextView) itemView.findViewById(R.id.customerName);
            productName = (TextView) itemView.findViewById(R.id.productName);
            productModel = (TextView) itemView.findViewById(R.id.productModel);
            productSerial = (TextView) itemView.findViewById(R.id.productSerial);
            expandable_layout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
            recyclerview = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            header_layout = (LinearLayout) itemView.findViewById(R.id.header_layout);

            expandable_layout.setInterpolator(new OvershootInterpolator());
            expandable_layout.setOnExpansionUpdateListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, getAdapterPosition());
                int position = getAdapterPosition();
                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                    expandable_layout.collapse();
                } else {
                    expandable_layout.expand();
                    selectedItem = position;
                }
            }
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            if (state == ExpandableLayout.State.EXPANDING) {
                recyclerview.smoothScrollToPosition(getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
