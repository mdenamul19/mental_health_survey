package forms_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.icddrb.mental_health_survey.R;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Common.Connection;
import Common.Global;
import Utility.MySharedPreferences;
import forms_datamodel.Household_DataModel;
import forms_datamodel.Member_DataModel;


public class Member_list extends AppCompatActivity {
    boolean networkAvailable=false;
   // Location currentLocation;
    double currentLatitude,currentLongitude;
    private String MemID;
    private String DSSID;
    private String Name;

    private String GeoLevel7;
    private String GeoLevel7Name;
    private String VillCode;
    private String VillName;
    private String CompoundCode;
    private String CompoundName;
    private String HHNO;
    private String HHHead;
    private String MSlNo;
    private String BDate;
    private String Age;
    private String FaName;
    private String MoName;



    //Disabled Back/Home key
    //--------------------------------------------------------------------------------------------------
    @Override
    public boolean onKeyDown(int iKeyCode, KeyEvent event)
    {
        if(iKeyCode == KeyEvent.KEYCODE_BACK || iKeyCode == KeyEvent.KEYCODE_HOME)
        { return false; }
        else { return true;  }
    }
    String VariableID;
    private int mDay;
    private int mMonth;
    private int mYear;
    static final int DATE_DIALOG = 1;
    static final int TIME_DIALOG = 2;

    Connection C;
    Global g;
    private List<Member_DataModel> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter mAdapter;
    static String TableName;


    TextView lblHeading;
    Button btnAdd;
    EditText txtSearch;
    EditText dtpFDate;
    EditText dtpTDate;
    Bundle IDbundle;
    Spinner spnLocation;
    Spinner spnVillage;
    Spinner spnCompound;
    Spinner spnHousehold;


    static String STARTTIME = "";
    static String DEVICEID  = "";

    static String ENTRYUSER = "";

    RelativeLayout secBari;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.member_list);
           // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            C = new Connection(this);
            g = Global.getInstance();
            STARTTIME = g.CurrentTime24();

          //  DEVICEID = MySharedPreferences.getValue(this, "deviceid");
          //  ENTRYUSER = MySharedPreferences.getValue(this, "userid");


            TableName = "Member";
          //  lblHeading = (TextView)findViewById(R.id.lblHeading);

            ImageButton cmdBack = (ImageButton) findViewById(R.id.cmdBack);
            cmdBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(Member_list.this);
                    adb.setTitle("Close");
                    adb.setMessage("Do you want to close this form[Yes/No]?");
                    adb.setNegativeButton("No", null);
                    adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }});
                    adb.show();
                }});


            IDbundle = getIntent().getExtras();
            MemID    = IDbundle.getString("MemID");
            GeoLevel7 =IDbundle.getString("GeoLevel7");
            GeoLevel7Name =IDbundle.getString("GeoLevel7Name");
            VillCode =IDbundle.getString ("VillCode");
            VillName =IDbundle.getString("VillName");
            CompoundCode =IDbundle.getString("CompoundCode");
            CompoundName =IDbundle.getString("CompoundName");
            HHNO =IDbundle.getString("HHNO");
            HHHead =IDbundle.getString("HHHead");
            MSlNo =IDbundle.getString("MSlNo");
            Name =IDbundle.getString("Name");
            HHHead =IDbundle.getString("HHHead");
            DSSID =IDbundle.getString ("DSSID");
            BDate =IDbundle.getString ("BDate");
            Age =IDbundle.getString ("Age");
            FaName =IDbundle.getString("FaName");
            MoName =IDbundle.getString("MoName");

            spnLocation = (Spinner)findViewById(R.id.spnLocation);
            spnVillage = (Spinner)findViewById(R.id.spnVillage);
            spnCompound = (Spinner)findViewById(R.id.spnCompound);
            spnHousehold = (Spinner)findViewById(R.id.spnHousehold);


            spnLocation.setAdapter(C.getArrayAdapter("Select '"+ GeoLevel7 +"-"+ GeoLevel7Name +"'"));
            spnVillage.setAdapter(C.getArrayAdapter("Select '"+ VillCode +"-"+ VillName +"'"));
            spnCompound.setAdapter(C.getArrayAdapter("Select '"+ CompoundCode +"-"+ CompoundName +"'"));
            spnHousehold.setAdapter(C.getArrayAdapter("Select '"+ HHNO +"-"+ HHHead +"'"));

            // spnLocation.setEnabled(false);



            // Populate Location Spinner
            //  spnLocation.setAdapter(C.getArrayAdapter("SELECT GeoLevel1 || '-' || GeoLevel1Name FROM Location"));
            //spnLocation.setEnabled(false);




            // Listener to populate Village Spinner
         spnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                 String selectedLocation = parent.getSelectedItem().toString().split("-")[0];
//                 spnVillage.setAdapter(C.getArrayAdapter(
//                         "SELECT VillCode || '-' || VillName FROM Village WHERE LocID='" + selectedLocation + "'"));
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {}
         });


         spnVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             //    String selectedVillage = parent.getSelectedItem().toString().split("-")[0];
               //  spnCompound.setAdapter(C.getArrayAdapter(
               //          "SELECT CompoundCode || '-' || CompoundName FROM Compound WHERE VillID='" + selectedVillage + "'"));
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {}
         });

// Listener to populate Household Spinner
         spnCompound.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //  String selectedCompound = parent.getSelectedItem().toString().split("-")[0];
               //  spnHousehold.setAdapter(C.getArrayAdapter(
                 //        "SELECT HHNO || '-' || HHHead FROM Household WHERE CompoundID='" + selectedCompound + "'"));
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {}
         });




         spnHousehold.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String selectedHousehold = parent.getSelectedItem().toString().split("-")[0];
              //   String query = "SELECT * FROM Member WHERE HHID='" + selectedHousehold + "'";
               //  List<Member_DataModel> members = C.fetchMembers(query); // Create a fetchMembers() method in Connection
             //   MemberAdapter.updateList(members); // Update RecyclerView

             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {}
         });



           // txtSearch = (EditText)findViewById(R.id.txtSearch);

            recyclerView = (RecyclerView)findViewById(R.id.recyclerViewMembers);
            mAdapter = new DataAdapter(dataList);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

          //  Connection.LocalizeLanguage(Member_list.this, MODULEID, LANGUAGEID);

        }
        catch(Exception  e)
        {
            Connection.MessageBox(Member_list.this, e.getMessage());
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  tmpBariNo = "";
       // DataSearch();
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        } else {
            //DataSearch(txtSearch.getText().toString());
        }
    }

    private void DataSearch()
    {
        try
        {

            GeoLevel7 = spnLocation.getSelectedItem().toString().split("-")[0];
            VillName = spnVillage.getSelectedItem().toString().split("-")[0];
            CompoundCode = spnCompound.getSelectedItem().toString().split("-")[0];
            HHNO = spnHousehold.getSelectedItem().toString().split("-")[0];

         //   BARI = spnHousehold.getSelectedItemPosition()==0 ?"%" : spnBari.getSelectedItem().toString().split("-")[0];

            Member_DataModel d = new Member_DataModel();


            String SQL = "Select h.DCode, h.UPCode, h.UNCode,h.Cluster, h.VCode, h.Bari, h.HHNo, h.HHHead, h.Mobile1, h.Mobile2, h.VisitStatus, ifnull(bn.BariName,'')BariName,b.BariLoc," +
                    " (case when h.totalmember is null or length(ifnull(h.totalmember,''))=0 then 0 else h.totalmember end) totalmember, " +
                    " (case when h.cmwratotal is null or length(ifnull(h.cmwratotal,''))=0 then 0 else h.cmwratotal end) cmwratotal," +
                    " '' indexhh " +
                    //" ifnull(i.hhno,'')indexhh " +
                    " from Household h\n" +
                    " left outer join (select b.DCode,b.UPCode,b.UNCode,b.cluster,b.VCode,b.Bari,b.BariName,min(h.HHNo)hhno from Bari b inner join Household h on b.DCode=h.DCode and b.UPCode=h.UPCode and b.Cluster=h.Cluster and b.UNCode=h.UNCode and b.VCode=h.VCode and b.Bari=h.Bari\n" +
                    "      where b.DCode='"+ GeoLevel7 +"' and b.UPCode='"+ VillCode +"' and b.UNCode='"+ CompoundCode +"' and b.VCode='"+ HHNO +"'\n" +
                    "      group by b.DCode,b.UPCode,b.UNCode,b.VCode,b.Bari,b.BariName)bn on h.dcode=bn.dcode and h.upcode=bn.upcode and h.uncode=bn.uncode and h.cluster=bn.cluster and h.vcode=bn.vcode and h.bari=bn.bari and h.hhno=bn.hhno" +
                    " inner join Bari b on h.DCode=b.DCode and h.UPCode=b.UPCode and h.UNCode=b.UNCode and h.Cluster=b.Cluster and h.VCode=b.VCode and h.Bari=b.Bari\n" +
                    //" left outer join Index_Household i on h.DCode=i.DCode and h.UPCode=i.UPCode and h.UNCode=i.UNCode and h.Cluster=i.Cluster and h.VCode=i.VCode and h.Bari=i.Bari and h.hhno=i.hhno" +
                    " Where h.DCode='"+ GeoLevel7 +"' and h.UPCode='"+ VillCode +"' and h.UNCode='"+ CompoundCode +"' and h.Cluster='"+ HHNO +"' and h.VCode='"+ HHNO +"' and h.Bari like('"+ HHNO +"') " +
                    " order by h.vcode, h.bari,h.hhno";


            List<Member_DataModel> data = d.SelectAll(this, SQL);
            dataList.clear();

            dataList.addAll(data);
            try {
                mAdapter.notifyDataSetChanged();
                lblHeading.setText("খানার তালিকা (মোট খানা: "+ String.valueOf(dataList.size()) +")");
            }catch ( Exception ex){
                Connection.MessageBox(Member_list.this,ex.getMessage());
            }
        }
        catch(Exception  e)
        {
            Connection.MessageBox(Member_list.this, e.getMessage());
            return;
        }
    }





    public class DataAdapter  extends RecyclerView.Adapter<Member_list.DataAdapter.ViewHolder> {
        private List<Member_DataModel> dataList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, age, gender;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
               // name = itemView.findViewById(R.id.memberName);
                name =itemView.findViewById(R.id.MemberName);
                age = itemView.findViewById(R.id.MemberAge);
                gender = itemView.findViewById(R.id.MemberGender);
            }
        }

        public DataAdapter(List<Member_DataModel> datalist) {

            this.dataList = datalist;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           final Member_DataModel data = dataList.get(position);
            //  holder.name.setText(data.getName());
            //  holder.age.setText("Age: " + data.getAge());
            //  holder.gender.setText("Gender: " + data.getSex());
        }




        public int getItemCount() {
            return dataList.size();
        }



    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };
        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
        private Drawable mDivider;
        private int mOrientation;
        public DividerItemDecoration(Context context, int orientation) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
        }
        public void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }
        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }
        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
        interface ClickListener {
            void onClick(View view, int position);
            void onLongClick(View view, int position);
        }
    }


    protected Dialog onCreateDialog(int id) {
        final Calendar c = Calendar.getInstance();
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mDateSetListener,g.mYear,g.mMonth-1,g.mDay);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year; mMonth = monthOfYear+1; mDay = dayOfMonth;
            EditText dtpDate;
            dtpDate = (EditText)findViewById(R.id.dtpFDate);
            if (VariableID.equals("dtpFDate"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpFDate);
            }
            else if (VariableID.equals("dtpTDate"))
            {
                dtpDate = (EditText)findViewById(R.id.dtpTDate);
            }
            dtpDate.setText(new StringBuilder()
                    .append(Global.Right("00"+mDay,2)).append("/")
                    .append(Global.Right("00"+mMonth,2)).append("/")
                    .append(mYear));
        }
    };


}