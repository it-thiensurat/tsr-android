package th.co.thiensurat.fragments.sales.lead_online.adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.CustomerStatusAdapter;
import th.co.thiensurat.fragments.sales.lead_online.LEAD_ONLINE;
import th.co.thiensurat.fragments.sales.lead_online.models.Getdata;
import th.co.thiensurat.fragments.sales.lead_online.models.GetdataStampCode;

import static th.co.bighead.utilities.BHFragment.showWarningDialog;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {


    List<Getdata> getDataAdapter;
    Getdata getDataAdapter1;
    List<GetdataStampCode> getDataAdapterSC;
    GetdataStampCode getdataStampCodesc;
    LEAD_ONLINE lead_online2 = new LEAD_ONLINE();
    private Context context;

SwipeRefreshLayout swipeRefreshLayout;
    public static LinearLayout linear_down;
    public static ImageView image_status;
    public RecyclerView my_recycler_view2, my_recycler_view;
    private int layout1 = 100;
    private int layout2 = 101;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    ReadJSON readJson;
    private ItemClickListener mClickListener;
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leadonline, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onAccept(View v, int position);
        void onSearch(View v, int postion);
        void onCancel(View v, int postion);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


    @Override
    public void onBindViewHolder(@NotNull ItemRowHolder Viewholder, int i) {

        try {
            getDataAdapter1 = getDataAdapter.get(i);
            Viewholder.txtId.setText(getDataAdapter1.getId());
            Viewholder.txtHeadId.setText("เลขที่ :"+getDataAdapter1.getId());
            Viewholder.txtCreateDate.setText(getDataAdapter1.getCreateDate());
            Viewholder.txtcusName.setText(getDataAdapter1.getCustomerName());
            Viewholder.txtTel.setText(getDataAdapter1.getTel());
            Viewholder.txtProvinceName.setText(getDataAdapter1.getProvince());
            Viewholder.txtDetail.setText(getDataAdapter1.getDetails());
            Viewholder.txtProductName.setText(getDataAdapter1.getProduct());
            Viewholder.txtEmail.setText(getDataAdapter1.getEmail());
            Viewholder.txtIDLine.setText(getDataAdapter1.getIDLine());
            Viewholder.txtidprovince.setText(getDataAdapter1.getIDProvince());
            Viewholder.txtnameimage.setText(getDataAdapter1.getPicture());
            Glide.with(context)
                    .load(getDataAdapter1.getPicture())
                    //.load("https://www.safealkaline.com/media/catalog/product/cache/1/image/750x750/9df78eab33525d08d6e5fb8d27136e95/s/a/safe_uv_alkaline_front.png")
                    .placeholder(R.drawable.barcode) //5
                    .error(R.drawable.bg_splash) //6
//                    .fallback(R.drawable.barcode) //7
                    .into(Viewholder.pictureURL);
            if(getDataAdapter1.getStatusWork().equals("")) {
                Viewholder.twobntLinerLayout.setVisibility(View.VISIBLE);
                Viewholder.btnSearchLinerLayout.setVisibility(View.GONE);
                Viewholder.StampLinearLayout.setVisibility(View.GONE);
                Viewholder.statusLeadonline.setText("รอรับงาน");
            }  else if(getDataAdapter1.getStatusWork().equals("1")){
                Viewholder.twobntLinerLayout.setVisibility(View.VISIBLE);
                Viewholder.btnSearchLinerLayout.setVisibility(View.GONE);
                Viewholder.StampLinearLayout.setVisibility(View.GONE);
                Viewholder.statusLeadonline.setText("รอรับงาน");
           }else if(getDataAdapter1.getStatusWork().equals("2")){
                Viewholder.twobntLinerLayout.setVisibility(View.GONE);
                Viewholder.btnSearchLinerLayout.setVisibility(View.VISIBLE);
                Viewholder.statusLeadonline.setText("กำลังดำเนินการ");
                Viewholder.StampLinearLayout.setVisibility(View.GONE);
           }else if(getDataAdapter1.getStatusWork().equals("3")){
                Viewholder.twobntLinerLayout.setVisibility(View.GONE);
                Viewholder.btnSearchLinerLayout.setVisibility(View.GONE);
                Viewholder.StampLinearLayout.setVisibility(View.VISIBLE);
                Viewholder.statusLeadonline.setText("ดำเนินการเรียบร้อย");
                String cus="";
                if(getDataAdapter1.getCodeStamp().equals("I01")){
                    cus="I01-ปิดการขายติดตั้งเรียบร้อยแล้ว";
                }else if(getDataAdapter1.getCodeStamp().equals("A01")){
                    cus="A01-ไม่รับสาย";
                }else if(getDataAdapter1.getCodeStamp().equals("A02")){
                    cus="A02-เบอร์โทรผิด";
                }else if(getDataAdapter1.getCodeStamp().equals("A03")){
                    cus="A03-โทรไม่ติด,ปิดเครื่อง";
                }else if(getDataAdapter1.getCodeStamp().equals("A4")){
                    cus="A04-ลูกค้าไม่สะดวกคุยจะติดต่อกลับ";
                }else if(getDataAdapter1.getCodeStamp().equals("B01")){
                    cus="B01-รอตัดสินใจ";
                }else if(getDataAdapter1.getCodeStamp().equals("B02")){
                    cus="B02-รอปรึกษาครอบครัวก่อน";
                }else if(getDataAdapter1.getCodeStamp().equals("B03")){
                    cus="B03-ลูกค้านัดวันติดตั้ง/ (สามารถเลือกระบุวันนัดในปฏิทินของระบบ เพื่อแจ้งเตือนได้เมื่อถึงวันนัด)";
                }else if(getDataAdapter1.getCodeStamp().equals("B04")){
                    cus="B04-สอบถามข้อมูลให้เพื่อนหรือญาติ";
                }else if(getDataAdapter1.getCodeStamp().equals("B05")){
                    cus="B05-ลูกค้าให้ส่งข้อมูลเพิ่มเติมทางไลน์";
                }else if(getDataAdapter1.getCodeStamp().equals("B06")){
                    cus="B06-ลูกค้าจะส่งข้อมูลให้แอดมินเพิ่มเติม (เช่น Location,เบอร์โทรอื่นเพิ่มเติม) / แอดมินสามารถEditแก้ไขหรือเพิ่มข้อมูลในNoteได้";
                }else if(getDataAdapter1.getCodeStamp().equals("F01")){
                    cus="F01-ไม่สนใจสินค้า";
                }else if(getDataAdapter1.getCodeStamp().equals("F02")){
                    cus="F02-ซื้อยี่ห้ออื่นมาแล้ว";
                }else if(getDataAdapter1.getCodeStamp().equals("F03")){
                    cus="F03-ลูกค้าใช้เซฟอยู่แล้วทักมาสอบถามเฉยๆ";
                }else if(getDataAdapter1.getCodeStamp().equals("F04")){
                    cus="F04-ต้องการสินค้าอื่นๆ";
                }else if(getDataAdapter1.getCodeStamp().equals("E01")){
                    cus="E01-ไม่ต้องการเทิร์น ต้องการเปลี่ยนสารกรอง / (ต้องระบุรุ่นที่จะเปลี่ยนสาร)";
                }else if(getDataAdapter1.getCodeStamp().equals("E02")){
                    cus="E02-นอกเขตการขาย,พื้นที่ปิดการขาย / (ต้องระบุพื้นที่ของลูกค้าที่แจ้งว่านอกเขต)";
                }else if(getDataAdapter1.getCodeStamp().equals("E03")){
                    cus="E03-ลูกค้ามีสถานะถอดT / (ต้องแนบรูปภาพการตรวจสถานะจากระบบ)";
                }else if(getDataAdapter1.getCodeStamp().equals("E04")){
                    cus="E04-ลูกค้ามีสถานะหนี้สูญR / (ต้องแนบรูปภาพการตรวจสถานะจากระบบ)";
                }else if(getDataAdapter1.getCodeStamp().equals("E05")){
                    cus="E05-ฝ่ายขายพิจารณาแล้วสภาพบ้านไม่ผ่าน / (ต้องแนบรูปภาพถ่ายบ้านลูกค้า)";
                }else if(getDataAdapter1.getCodeStamp().equals("E06")){
                    cus="E06-มีฝ่ายขายอื่นของTSRติดตั้งไปแล้ว / (ต้องแนบรูปภาพถ่ายเครื่องกรองของลูกค้าที่พึ่งติดตั้งใหม่)";
                }
                Viewholder.statusStampcode.setText(cus);
           }
//            Viewholder.btnCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String work="0";
//                    String cus="";
//                    String id = Viewholder.txtId.getText().toString();
//                    String cusname =Viewholder.txtcusName.getText().toString();
//                    String province= Viewholder.txtidprovince.getText().toString();
//                    String title = "แจ้งเตือน";
//                    String message = "คุณต้องการยกเลิกข้อมูลชุดนี้ใช่หรือไม่";
//                    showWarningDialog(title, message);
////                    lead_online2.update_data_lead(id,work,cus,cusname,province );
////                    lead_online2.load_data_lead();
//                }
//            });
//
//            Viewholder.btnAccept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String work= "2";
//                    String cus="";
//                    String id = Viewholder.txtId.getText().toString();
//                    String cusname =Viewholder.txtcusName.getText().toString();
//                    String province= Viewholder.txtidprovince.getText().toString();
//                    String title = "แจ้งเตือน";
//                    String message = "ยืนยันการอัพเดทสานะการทำงาน";
//                    showWarningDialog(title, message);
//                    lead_online2.update_data_lead(id,work,cus,cusname,province );
//                }
//            });
//
//            Viewholder.btnSearch.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view ) {
//
//                  //  Toast.makeText(context,"Your Shared (ImageID = " + Viewholder.txtId.getText() + ")",Toast.LENGTH_LONG).show();
//                    String work="3";
//                    String id = Viewholder.txtId.getText().toString();
//                    String cusname =Viewholder.txtcusName.getText().toString();
//                    String province= Viewholder.txtidprovince.getText().toString();
////                    lead_online2.dialogspinner(id,work,cusname,province);
////                    ((LEAD_ONLINE) context).dialogspinner(id,work,cusname,province);
//                }
//
//            });
            Viewholder.pictureURL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lead_online2.openimage(Viewholder.txtnameimage.getText().toString());
                }
            });

        } catch (Exception ex) {
        }
    }

    @Override
    public int getItemCount() {
        // return 0;
        return (null != getDataAdapter ? getDataAdapter.size() : 0);
    }
   @Override
    public int getItemViewType(int position) {
        return getDataAdapter.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    public class ItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView txtId;
        protected TextView txtHeadId;
        protected TextView txtCreateDate;
        protected TextView txtcusName;
        protected TextView txtTel;
       // protected ImageView pictureURL;
        protected TextView txtProvinceName;
        protected TextView txtProductName;
        protected TextView txtDetail;
        protected Button btnAccept;
        protected Button btnSearch;
        protected Button btnCancel;
        protected Spinner statuscus;
        protected LinearLayout StampLinearLayout;
        protected LinearLayout twobntLinerLayout;
        protected LinearLayout btnSearchLinerLayout;
        protected TextView statusLeadonline;
        protected TextView txtEmail;
        protected TextView txtIDLine;
        protected TextView statusStampcode;
        protected TextView txtnameimage;
        protected TextView txtidprovince;
        protected SwipeRefreshLayout swipeRefreshLayout;
        protected ImageButton pictureURL;
        public ItemRowHolder(View view) {
            super(view);
            this.txtId = (TextView) view.findViewById(R.id.txtId);
            this.txtHeadId = (TextView) view.findViewById(R.id.txtHeadId);
            this.txtCreateDate = (TextView) view.findViewById(R.id.txtCreateDate);
            this.txtcusName = (TextView) view.findViewById(R.id.txtcusName);
            this.txtTel = (TextView) view.findViewById(R.id.txtTel);
            this.txtProvinceName = (TextView) view.findViewById(R.id.txtProvinceName);
            this.txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            this.txtDetail = (TextView) view.findViewById(R.id.txtDetail);
            this.txtEmail = (TextView) view.findViewById(R.id.txtemail);
            this.txtIDLine =(TextView) view.findViewById(R.id.txtidline);
           // this.pictureURL = (ImageView) view.findViewById(R.id.pictureURL);
            this.pictureURL=(ImageButton)view.findViewById(R.id.pictureURL);
            this.statusLeadonline = (TextView) view.findViewById(R.id.statusLeadonline);
            this.btnAccept = (Button) view.findViewById(R.id.btnAccept);
            this.btnCancel = (Button) view.findViewById(R.id.btnCancel);
            this.btnSearch = (Button) view.findViewById(R.id.btnSearch);
            this.statusStampcode = (TextView) view.findViewById(R.id.statusStampcode);
            this.StampLinearLayout = (LinearLayout) view.findViewById(R.id.StampLinearLayout);
            this.twobntLinerLayout = (LinearLayout) view.findViewById(R.id.twobntLinerLayout);
            this.btnSearchLinerLayout = (LinearLayout) view.findViewById(R.id.btnSerchLinerLayout);
            //this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            this.txtnameimage = (TextView) view.findViewById(R.id.txtnameimage);
            this.txtidprovince = (TextView) view.findViewById(R.id.txtidprovince);
            itemView.setOnClickListener(this);
            btnAccept.setOnClickListener(this);
            btnSearch.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnAccept) {
                mClickListener.onAccept(v, getAdapterPosition());
            }  else if (v.getId() == R.id.btnSearch) {
                mClickListener.onSearch(v, getAdapterPosition());
            } else if (v.getId() == R.id.btnCancel) {
                mClickListener.onCancel(v, getAdapterPosition());
            }
        }
    }
}