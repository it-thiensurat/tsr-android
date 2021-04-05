package th.co.thiensurat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.R;
import th.co.thiensurat.data.info.ProductRecomendInfo;

public class ProductRecomdAdapter extends RecyclerView.Adapter<ProductRecomdAdapter.Holder> {

    private Context context;
    private ItemClickListener mClickListener;
    private List<ProductRecomendInfo> productRecomendInfoLis = new ArrayList<ProductRecomendInfo>();

    public ProductRecomdAdapter(List<ProductRecomendInfo> productRecomendInfoList, Context context) {
        this.context = context;
        this.productRecomendInfoLis = productRecomendInfoList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.productrecomend_cardview, viewGroup, false);
        ProductRecomdAdapter.Holder holder = new ProductRecomdAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        ProductRecomendInfo productRecomendInfo = productRecomendInfoLis.get(i);
        Glide.with(context)
                .load(productRecomendInfo.getImgPath())
                .into(holder.imageProduct);
        holder.brandProduct.setText(productRecomendInfo.getBrandName());
        holder.codeProduct.setText(productRecomendInfo.getProductCode());
        holder.nameProduct.setText(productRecomendInfo.getProductName());
        holder.priceProduct.setText(BHUtilities.numericFormat(productRecomendInfo.getRetailPrice()));
    }

    @Override
    public int getItemCount() {
        return productRecomendInfoLis.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageProduct;
        TextView brandProduct, codeProduct, nameProduct, priceProduct;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
            brandProduct = (TextView) itemView.findViewById(R.id.brandProduct);
            codeProduct = (TextView) itemView.findViewById(R.id.codeProduct);
            nameProduct = (TextView) itemView.findViewById(R.id.nameProduct);
            priceProduct = (TextView) itemView.findViewById(R.id.priceProduct);
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
