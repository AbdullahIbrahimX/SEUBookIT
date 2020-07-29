package seu.seniorproject.com.seubookit.Doctor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seu.seniorproject.com.seubookit.Utilities.AVtimeAdapter;
import seu.seniorproject.com.seubookit.R;
import seu.seniorproject.com.seubookit.Shared.MainActivity;

public class ManageAvTime extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = ManageAvTime.class.getName();


    private TextView stDate,enDate,sTime,endTime;
    private RadioGroup DOWgroup;
    private RadioButton checkedradiobutton;
    private Button mnAVsubmit,mnAVcancel,mnAVgenerate;
    private String DOW,usrfile;
    private Calendar calendar = Calendar.getInstance (), dummy;
    private DatePickerDialog.OnDateSetListener startDateSetListener,endDateSetListener;
    private TimePickerDialog.OnTimeSetListener startTimeSetListener,endimeSetListener;
    private List<Map<String, Object>> datelist;
    private RecyclerView dateview;
    private AVtimeAdapter dateviewadapter;
    private Calendar startdate = Calendar.getInstance (),enddate=Calendar.getInstance ();
    private Boolean allpass = true;
    private SimpleDateFormat date = new SimpleDateFormat ( "dd/MM/yyyy" );
    private SimpleDateFormat time = new SimpleDateFormat ( "HH:mm" );
    private SimpleDateFormat file = new SimpleDateFormat ( "yyyyMMddHHmm" );


    @Override
    protected void onCreate(final Bundle savedInstanceState)  {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_manage_av_time );
        Intent bac = getIntent ();
        usrfile = bac.getStringExtra ( "userFile" );

        stDate = findViewById ( R.id.stDate );
        enDate = findViewById ( R.id.enDate );
        sTime = findViewById ( R.id.sTime );
        endTime = findViewById ( R.id.endTime );
        mnAVgenerate = findViewById ( R.id.mnAVgenerate );
        mnAVsubmit =findViewById ( R.id.mnAVsubmit );
        mnAVcancel = findViewById ( R.id.mnAVcancel );
        DOWgroup = findViewById ( R.id.DOWgroup );
        dateview = findViewById ( R.id.addAVrecycler );
        datelist = new ArrayList <> ();
        dateviewadapter = new AVtimeAdapter ( datelist,this);

        dateview.setHasFixedSize(true);
        dateview.setLayoutManager(new LinearLayoutManager (this));
        dateview.setAdapter(dateviewadapter);


        stDate.setOnClickListener ( this );
        sTime.setOnClickListener ( this );
        endTime.setOnClickListener ( this );
        enDate.setOnClickListener ( this );
        mnAVcancel.setOnClickListener ( this );
        mnAVsubmit.setOnClickListener ( this );
        mnAVgenerate.setOnClickListener ( this );

        startDateSetListener = new DatePickerDialog.OnDateSetListener () {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startdate.set ( Calendar.YEAR,year );
                startdate.set ( Calendar.MONTH,month );
                startdate.set ( Calendar.DAY_OF_MONTH,dayOfMonth );
                stDate.setText ( date.format ( startdate.getTime () ) );
            }
        };
        endDateSetListener = new DatePickerDialog.OnDateSetListener () {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                enddate.set ( Calendar.YEAR,year );
                enddate.set ( Calendar.MONTH,month );
                enddate.set ( Calendar.DAY_OF_MONTH,dayOfMonth );
                enDate.setText (date.format ( enddate.getTime () ) );
            }
        };
        startTimeSetListener = new TimePickerDialog.OnTimeSetListener () {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startdate.set ( Calendar.HOUR_OF_DAY,hourOfDay );
                startdate.set ( Calendar.MINUTE,minute );
                sTime.setText ( time.format ( startdate.getTime () ));
            }
        };
        endimeSetListener = new TimePickerDialog.OnTimeSetListener () {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                enddate.set ( Calendar.HOUR_OF_DAY,hourOfDay );
                enddate.set ( Calendar.MINUTE,minute );
                endTime.setText (time.format ( enddate.getTime ()));
            }
        };


        DOWgroup.setOnCheckedChangeListener ( new RadioGroup.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                DOWgroup.setBackgroundColor ( Color.TRANSPARENT );
                checkedradiobutton = findViewById ( checkedId );
                DOW = checkedradiobutton.getText ().toString ();


            }
        } );

    }

    @Override
    public void onClick(View v) {
        if (v == stDate){
            stDate.setError ( null );
            int year = calendar.get ( Calendar.YEAR );
            int month = calendar.get ( Calendar.MONTH );
            int day = calendar.get ( Calendar.DAY_OF_MONTH );
            DatePickerDialog startDialog = new DatePickerDialog ( this, android.R.style.Theme_Holo_Light_Dialog,startDateSetListener,year,month,day );
            startDialog.show ();
        }else if (v == enDate){
            enDate.setError ( null );
            int year = calendar.get ( Calendar.YEAR );
            int month = calendar.get ( Calendar.MONTH );
            int day = calendar.get ( Calendar.DAY_OF_MONTH );
            DatePickerDialog startDialog = new DatePickerDialog ( this, android.R.style.Theme_Holo_Light_Dialog,endDateSetListener,year,month,day );
            startDialog.show ();
        }else if ( v == sTime ){
            sTime.setError ( null );
            TimePickerDialog timePickerDialog = new TimePickerDialog ( this, startTimeSetListener, 0,0,true );
            timePickerDialog.show ();

        }else if ( v == endTime){
            endTime.setError ( null );
            TimePickerDialog timePickerDialog = new TimePickerDialog ( this , endimeSetListener,0,0,true );
            timePickerDialog.show ();
        }else if ( v == mnAVcancel){
            finish ();
        }else if ( v == mnAVsubmit){
            if (datelist.size ()==0){
                Toast.makeText(getApplicationContext(), "You did not choose times to submit", Toast.LENGTH_LONG).show();
                return;
            }
            FirebaseFirestore db = FirebaseFirestore.getInstance ();
            for (Map<String,Object> datefile:datelist)
            {
                db.collection ( "users" ).document (usrfile)
                        .collection ( "ARM" ).document (datefile.get ( "Filename" ).toString ())
                        .set ( datefile ).addOnFailureListener ( new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        allpass = false;
                    }
                } );
            }
            if (allpass){
                finish ();
            }else{
                Toast.makeText(getApplicationContext(), "Error, Something went wrong, please contact admins", Toast.LENGTH_LONG).show();
            }


        }else if (v == mnAVgenerate){
            datelist.clear ();
            checkEmpty();
            checkValidity();
            startdate = setDayofweek(startdate);
            enddate = setDayofweek ( enddate );

            int shour = startdate.get ( Calendar.HOUR_OF_DAY );
            int sminute = startdate.get ( Calendar.MINUTE );
            dummy = startdate;

            while (dummy.before ( enddate )){
                while (dummy.get ( Calendar.HOUR_OF_DAY )< enddate.get(Calendar.HOUR_OF_DAY)){
                    Map<String,Object> datefile = new HashMap <> (  );
                    datefile.put ( "Filename", file.format ( dummy.getTime () ));
                    datefile.put ( "Name" ,"");
                    datefile.put ( "Purpose", "" );
                    datefile.put("Date",date.format ( dummy.getTime () ));
                    datefile.put ( "Time",time.format ( dummy.getTime () ));
                    datefile.put ( "Status", "A" );
                    datefile.put ( "DoctorID" ,usrfile );
                    datelist.add ( datefile );
                    dummy.add ( Calendar.MINUTE,30 );
                }
                dummy.set ( Calendar.HOUR_OF_DAY,shour);
                dummy.set ( Calendar.MINUTE,sminute );
                dummy.add ( Calendar.WEEK_OF_MONTH,1 );
            }
            dateviewadapter.notifyDataSetChanged ();
        }
    }

    private void checkValidity() {
        // check that start date < end date
        if (Integer.parseInt (file.format ( startdate.getTime ()).substring ( 0,8 )) > Integer.parseInt (file.format ( enddate.getTime()).substring ( 0,8 ))){
            stDate.setError ( "Start date has to be before end date" );
            enDate.setError ( "Start date has to be before end date" );
            return;
        }else if (Integer.parseInt ( file.format ( startdate.getTime () ).substring ( 8,12 ) )> Integer.parseInt ( file.format ( enddate.getTime () ).substring ( 8,12 ) )){
            sTime.setError ( "Start Time has to be before end time" );
            endTime.setError ( "Start Time has to be before end time" );
            return;
        }
    }

    private void checkEmpty() {
        if (stDate.getText ().toString ().equals ( "Click here" ) ){
            stDate.setError ( "please Enter a date" );
            return;
        }else if (enDate.getText ().toString ().equals ( "Click here" )){
            enDate.setError ( "Choose an end date" );
            return;
        }else if (sTime.getText ().toString ().equals ( "Click here" )){
            sTime.setError ( "Choose a start time" );
            return;
        }else if (endTime.getText ().toString () .equals ( "Click here" )){
            endTime.setError ( "Choose an end time" );
            return;
        }else if (DOW == null){
            DOWgroup.setBackgroundColor ( Color.RED );
            return;
        }
    }

    private Calendar setDayofweek(Calendar date){
        if (DOW.equals ( "Thursday" )){
            date.add ( Calendar.DAY_OF_MONTH,Calendar.THURSDAY );
            date.add ( Calendar.DATE,1 );
            return date;
        }else if (DOW.equals ("Sunday")){
            date.add ( Calendar.DAY_OF_MONTH,Calendar.SUNDAY );
            date.add ( Calendar.DATE,1 );
            return date;
        }else if (DOW.equals ("Monday")){
            date.add ( Calendar.DAY_OF_MONTH,Calendar.MONDAY );
            date.add ( Calendar.DATE,1 );
            return date;
        }else if (DOW.equals ( "Tuesday")){
            date.add ( Calendar.DAY_OF_MONTH,Calendar.TUESDAY );
            date.add ( Calendar.DATE,1 );
            return date;
        }else if (DOW.equals ( "Wednesday")){
            date.add ( Calendar.DAY_OF_MONTH,Calendar.WEDNESDAY );
            date.add ( Calendar.DATE,1 );
            return date;
        }else{
            return date;
        }
    }


}
