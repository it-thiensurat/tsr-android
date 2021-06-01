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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import th.co.thiensurat.R;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;

public class RecyclerViewDataAdapter_sale_Q extends RecyclerView.Adapter<RecyclerViewDataAdapter_sale_Q.ItemRowHolder>   {


    List<get_product_sale_q> getDataAdapter;
    get_product_sale_q getDataAdapter1;
    private Context context;


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



    public RecyclerViewDataAdapter_sale_Q(List<get_product_sale_q> getDataAdapter, Context context) {
        super();
      //  this.dataList = dataList;
      //  this.dataList2 = dataList2;
        this.context = context;
        this.getDataAdapter = getDataAdapter;
     //   userFilter = new UserFilter(com.tsr.tsrproblemreport_tossticket_checker.test.adapters.RecyclerViewDataAdapter.this,getDataAdapter);
    }


    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lead_product_s_q, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ItemRowHolder Viewholder, int i) {






                try {
                    getDataAdapter1 =  getDataAdapter.get(i);
                    Viewholder.txtproductName.setText(getDataAdapter1.getProduct_name());
                }
                catch (Exception ex){

                }






    }




    @Override
    public int getItemCount() {
       // return 0;
        return (null != getDataAdapter ? getDataAdapter.size() : 0);


    }


/*    @Override
    public int getItemViewType(int position) {
        return dataList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }*/





    public class ItemRowHolder extends RecyclerView.ViewHolder implements  View.OnClickListener  {

        protected TextView txtproductName;



        public ItemRowHolder(View view) {
            super(view);

            this.txtproductName = (TextView) view.findViewById(R.id.txtproductName);




            itemView.setOnClickListener(this);







        }


        @Override
        public void onClick(View v) {

        }
    }





}