package th.co.thiensurat.fragments.sales.sales_quotation.adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import th.co.thiensurat.R;
import th.co.thiensurat.adapter.CustomerStatusAdapter;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;

public class RecyclerViewDataAdapter_sale_Q extends RecyclerView.Adapter<RecyclerViewDataAdapter_sale_Q.ItemRowHolder>   {


    List<get_product_sale_q> selectedProductList;
    get_product_sale_q getDataAdapter1;
    private Context context;

    private ItemClickListener mClickListener;


    public  static LinearLayout linear_down;
    public  static ImageView image_status;
    public RecyclerView my_recycler_view2,my_recycler_view;
    String date_new_format_thai,date_new_format_thai2;
    String dateThai_year,dateThai_month,dateThai_day,dateThai_month1;
    int converted_dateThai11;
    private Date oneWayTripDate;
    String s1,s2,s3;
    private int layout1 = 100;
    private int layout2 = 101;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public void setSelectedProductList(List<get_product_sale_q> selectedProductList) {
        this.selectedProductList = selectedProductList;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lead_product_s_q, viewGroup, false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ItemRowHolder Viewholder, int i) {
        get_product_sale_q product = selectedProductList.get(i);
        Viewholder.txtNumber.setText(String.valueOf((i + 1)) + ". ");
        Viewholder.txtproductName.setText(product.getProduct_name());
        Viewholder.txtproductQty.setText(product.getProduct_qty());
    }

    @Override
    public int getItemCount() {
        return (null != selectedProductList ? selectedProductList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder implements  View.OnClickListener  {

        protected ImageView btnRemove;
        protected TextView txtNumber, txtproductName, txtproductQty;
        protected Button btnDecrease, btnIncrease;

        public ItemRowHolder(View view) {
            super(view);
            txtNumber = (TextView) view.findViewById(R.id.txtNumber);
            txtproductName = (TextView) view.findViewById(R.id.txtProductName);
            txtproductQty = (TextView) view.findViewById(R.id.txtProductQty);
            btnRemove = (ImageView) view.findViewById(R.id.button_remove_item);
            btnDecrease = (Button) view.findViewById(R.id.btn_decrease);
            btnIncrease = (Button) view.findViewById(R.id.btn_increase);

            itemView.setOnClickListener(this);
            btnDecrease.setOnClickListener(this);
            btnIncrease.setOnClickListener(this);
            btnRemove.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_decrease) {
                mClickListener.onDecrease(v, getAdapterPosition());
            } else if (v.getId() == R.id.btn_increase) {
                mClickListener.onIncrease(v, getAdapterPosition());
            } else if (v.getId() == R.id.button_remove_item) {
                mClickListener.onRemove(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onDecrease(View view, int position);
        void onIncrease(View view, int position);
        void onRemove(View view, int position);
    }
}