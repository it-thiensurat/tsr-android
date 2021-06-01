package th.co.thiensurat.fragments.sales.lead_online.adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.thiensurat.R;
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


    public static LinearLayout linear_down;
    public static ImageView image_status;
    public RecyclerView my_recycler_view2, my_recycler_view;
    String date_new_format_thai, date_new_format_thai2;
    String dateThai_year, dateThai_month, dateThai_day, dateThai_month1;
    int converted_dateThai11;
    private Date oneWayTripDate;
    String s1, s2, s3;
    private int layout1 = 100;
    private int layout2 = 101;
    private String statuswork;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private ArrayList<HashMap<String, String>> MyArrListRegions = new ArrayList<HashMap<String,String>>();
    ReadJSON readJson;

    public RecyclerViewDataAdapter(List<Getdata> getDataAdapter, Context context) {
        super();
        //  this.dataList = dataList;
        //  this.dataList2 = dataList2;
        this.context = context;
        this.getDataAdapter = getDataAdapter;
        //   userFilter = new UserFilter(com.tsr.tsrproblemreport_tossticket_checker.test.adapters.RecyclerViewDataAdapter.this,getDataAdapter);
    }
private void SetStatusCus(){


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
            getDataAdapter1 = getDataAdapter.get(i);
            Viewholder.txtId.setText("เลขที่ : " + getDataAdapter1.getId());
            Viewholder.txtCreateDate.setText(getDataAdapter1.getCreateDate());
            Viewholder.txtcusName.setText(getDataAdapter1.getCustomerName());
            Viewholder.txtTel.setText(getDataAdapter1.getTel());
            Viewholder.txtProvinceName.setText(getDataAdapter1.getProvince());
            Viewholder.txtDetail.setText(getDataAdapter1.getDetails());
            Viewholder.txtProductName.setText(getDataAdapter1.getProduct());
          //Viewholder.pictureURL.setImageURI(getDataAdapter1.getPicture());
            String compareValue = getDataAdapter1.getStatusWork();

            Glide.with(context)
                    .load(getDataAdapter1.getPicture())
                    .into(Viewholder.pictureURL);
                if (compareValue != null) {
            }

            if(getDataAdapter1.getStatusWork().equals("")) {
                Viewholder.statusLeadonline.setSelected(true);
                Viewholder.twobntLinerLayout.setVisibility(View.VISIBLE);
                Viewholder.btnSerchLinerLayout.setVisibility(View.GONE);
                Viewholder.StampLinearLayout.setVisibility(View.GONE);
                Viewholder.statusLeadonline.setText("รอรับงาน");
            }  else if(getDataAdapter1.getStatusWork().equals("1")){
                Viewholder.twobntLinerLayout.setVisibility(View.VISIBLE);
                Viewholder.btnSerchLinerLayout.setVisibility(View.GONE);
                Viewholder.StampLinearLayout.setVisibility(View.GONE);
                Viewholder.statusLeadonline.setText("รอรับงาน");
           }else if(getDataAdapter1.getStatusWork().equals("2")){
                Viewholder.twobntLinerLayout.setVisibility(View.GONE);
                Viewholder.btnSerchLinerLayout.setVisibility(View.VISIBLE);
                Viewholder.statusLeadonline.setText("กำลังดำเนินการ");
                Viewholder.StampLinearLayout.setVisibility(View.GONE);
           }else if(getDataAdapter1.getStatusWork().equals("3")){
                Viewholder.twobntLinerLayout.setVisibility(View.GONE);
                Viewholder.btnSerchLinerLayout.setVisibility(View.GONE);
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
            Viewholder.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String work="0";
                    String cus="";
                    String title = "แจ้งเตือน";
                    String message = "คุณต้องการยกเลิกข้อมูลชุดนี้ใช่หรือไม่";
                    showWarningDialog(title, message);
                    lead_online2.update_data_lead(getDataAdapter1.getId(),work,cus,getDataAdapter1.getCusName(),getDataAdapter1.getProvince() );
                }
            });
            Viewholder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String work= "2";
                    String cus="";
                    String title = "แจ้งเตือน";
                    String message = "ยืนยันการอัพเดทสานะการทำงาน";
                    showWarningDialog(title, message);

                    lead_online2.update_data_lead(getDataAdapter1.getId(),work,cus,getDataAdapter1.getCusName(),getDataAdapter1.getProvince() );

                }
            });
            Viewholder.btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String work="3";

                    lead_online2.dialogspinner(getDataAdapter1.getId(),work,getDataAdapter1.getCusName(),getDataAdapter1.getProvince() );

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


/*    @Override
    public int getItemViewType(int position) {
        return dataList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }*/


    public class ItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView txtId;
        protected TextView txtCreateDate;
        protected TextView txtcusName;
        protected TextView txtTel;
        protected ImageView pictureURL;
        protected TextView txtProvinceName;
        protected TextView txtProductName;
        protected TextView txtDetail;
        protected Button btnAccept;
        protected Button btnSearch;
        protected Button btnCancel;
        protected Spinner statuscus;
        protected LinearLayout StampLinearLayout;
        protected LinearLayout twobntLinerLayout;
        protected LinearLayout btnSerchLinerLayout;
        protected TextView statusLeadonline;
        protected TextView txtStatusWork;
        protected TextView statusStampcode;
        public ItemRowHolder(View view) {
            super(view);
            this.txtId = (TextView) view.findViewById(R.id.txtId);
            this.txtCreateDate = (TextView) view.findViewById(R.id.txtCreateDate);
            this.txtcusName = (TextView) view.findViewById(R.id.txtcusName);
            this.txtTel = (TextView) view.findViewById(R.id.txtTel);
            this.txtProvinceName = (TextView) view.findViewById(R.id.txtProvinceName);
            this.txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            this.txtDetail = (TextView) view.findViewById(R.id.txtDetail);
            this.pictureURL = (ImageView) view.findViewById(R.id.pictureURL);
            this.statusLeadonline = (TextView) view.findViewById(R.id.statusLeadonline);
            this.btnAccept = (Button) view.findViewById(R.id.btnAccept);
            this.btnCancel = (Button) view.findViewById(R.id.btnCancel);
            this.btnSearch = (Button) view.findViewById(R.id.btnSearch);
            this.statusStampcode = (TextView) view.findViewById(R.id.statusStampcode);
            this.StampLinearLayout = (LinearLayout) view.findViewById(R.id.StampLinearLayout);
            this.twobntLinerLayout = (LinearLayout) view.findViewById(R.id.twobntLinerLayout);
            this.btnSerchLinerLayout = (LinearLayout) view.findViewById(R.id.btnSerchLinerLayout);
            itemView.setOnClickListener(this);


        }
        public ArrayList<HashMap<String, String>> GetRegions(){
            HashMap<String, String> map;
            readJson = new ReadJSON();
            String  url = "https://tssm.thiensurat.co.th/api/api-leadonlineCoeStamp.php"; //+ Id;ห้ามลืมตอนเสร็จ
            String jsonResult = readJson.getHttpGet(url);
            try {
                JSONObject jsonObMain = new JSONObject(jsonResult);
                JSONArray data = jsonObMain.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.put("id", c.getString("id"));
                    map.put("CodeStamp", c.getString("CodeStamp")+"-"+c.getString("CodeStampTxt"));
                    MyArrListRegions.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return MyArrListRegions;
        }

        @Override
        public void onClick(View v) {

        }

        private void addUpdatestatuswork() {

        }
    }


}