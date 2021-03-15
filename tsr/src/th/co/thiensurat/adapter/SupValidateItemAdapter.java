package th.co.thiensurat.adapter;

import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.R;
import th.co.thiensurat.data.info.SupvalidateItemInfo;

public class SupValidateItemAdapter extends RecyclerView.Adapter<SupValidateItemAdapter.Holder> {
    private Context context;
    private ItemClickListener mClickListener;
    private List<SupvalidateItemInfo> supvalidateItemInfoList = new ArrayList<>();

    public SupValidateItemAdapter(Context context, List<SupvalidateItemInfo> supvalidateItemInfoList) {
        this.context = context;
        this.supvalidateItemInfoList = supvalidateItemInfoList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.supvalidate_item_carview, viewGroup, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        SupvalidateItemInfo supvalidateItemInfo = supvalidateItemInfoList.get(i);
        Glide.with(context)
                .load(supvalidateItemInfo.getImageUrl())
                .into(holder.imgValidate);
        String imageType = "";
        if (supvalidateItemInfo.getImageType().equals("IDCARD")) {
            imageType = "รูปบัตรประชาชน";
        } else if (supvalidateItemInfo.getImageType().equals("PAYMENTCARD")) {
            imageType = "รูปวาดแผนที่";
        } else if (supvalidateItemInfo.getImageType().equals("MAP")) {
            imageType = "รูปแผนที่ GPS";
        } else if (supvalidateItemInfo.getImageType().equals("PRODUCT")) {
            imageType = "รูปเครื่องเทิร์น";
        } else if (supvalidateItemInfo.getImageType().equals("MAPPAYMENT")) {
            imageType = "รูปติดตั้งเครื่อง";
        } else {
            imageType = "รูปบ้านลูกค้า";
        }
        holder.txtImgType.setText(imageType);
        holder.txtProblemName.setText(supvalidateItemInfo.getImageComment());
    }

    @Override
    public int getItemCount() {
        return supvalidateItemInfoList.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RecyclerView recyclerview;
        ImageView imgValidate;
        TextView txtImgType, txtProblemName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imgValidate = (ImageView) itemView.findViewById(R.id.imgValidate);
            txtImgType = (TextView) itemView.findViewById(R.id.txtImgType);
            txtProblemName = (TextView) itemView.findViewById(R.id.txtProblemName) ;
            recyclerview = (RecyclerView) itemView.findViewById(R.id.recyclerview);
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
