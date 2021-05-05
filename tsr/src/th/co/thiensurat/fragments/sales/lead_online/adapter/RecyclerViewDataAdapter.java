package th.co.thiensurat.fragments.sales.lead_online.adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import th.co.thiensurat.R;
import th.co.thiensurat.fragments.sales.lead_online.models.Getdata;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder>   {


    List<Getdata> getDataAdapter;
    Getdata getDataAdapter1;
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



    public RecyclerViewDataAdapter(List<Getdata> getDataAdapter, Context context) {
        super();
      //  this.dataList = dataList;
      //  this.dataList2 = dataList2;
        this.context = context;
        this.getDataAdapter = getDataAdapter;
     //   userFilter = new UserFilter(com.tsr.tsrproblemreport_tossticket_checker.test.adapters.RecyclerViewDataAdapter.this,getDataAdapter);
    }


    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leadonline, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ItemRowHolder Viewholder, int i) {






                try {
                    getDataAdapter1 =  getDataAdapter.get(i);
                    Viewholder.txtcusName.setText(getDataAdapter1.getCusName());
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

        protected TextView txtcusName;



        public ItemRowHolder(View view) {
            super(view);

            this.txtcusName = (TextView) view.findViewById(R.id.txtcusName);




            itemView.setOnClickListener(this);







        }


        @Override
        public void onClick(View v) {

        }
    }





}