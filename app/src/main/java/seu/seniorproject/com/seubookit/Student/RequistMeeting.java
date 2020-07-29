package seu.seniorproject.com.seubookit.Student;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import seu.seniorproject.com.seubookit.R;

public class RequistMeeting extends AppCompatActivity implements View.OnClickListener{
    private String datefile , doctorfile;
    private TextView reqTVname,reqTVDate,reqTVtime;
    private EditText reqETpurpose;
    private Button reqBTsend,reqBTback;
    private FirebaseFirestore db;
    private String stuname = "xx",date,time, doctorname;
    FirebaseAuth mAuth;
    FirebaseUser usr;
    private static final String TAG = RequistMeeting.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_requist_meeting);

        Intent bac = getIntent ();
        datefile = bac.getStringExtra ( "datefile" );
        doctorfile = bac.getStringExtra ( "doctorfile" );

        reqTVDate = findViewById ( R.id.reqTVDate );
        reqTVname = findViewById ( R.id.reqTVname );
        reqTVtime = findViewById ( R.id.reqTVtime );
        reqETpurpose = findViewById ( R.id.reqETpurpose );
        reqBTsend = findViewById ( R.id.reqBTsend );
        reqBTback = findViewById ( R.id.reqBTback );
        db = FirebaseFirestore.getInstance ();
        mAuth = FirebaseAuth.getInstance ();
         usr = mAuth.getCurrentUser();


        reqBTsend.setOnClickListener ( this );
        reqBTback.setOnClickListener ( this );

        db.collection ( "users" ).document (doctorfile).get ().addOnSuccessListener ( new OnSuccessListener <DocumentSnapshot> () {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                doctorname = documentSnapshot.getString ( "Firstname" )+" "+documentSnapshot.getString ( "Lastname" );
                reqTVname.setText ( doctorname );
            }
        } );

        db.collection ( "users" ).document (doctorfile).collection ( "ARM" ).document (datefile)
                .get ().addOnSuccessListener ( new OnSuccessListener <DocumentSnapshot> () {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                time = documentSnapshot.getString ( "Time" );
                date = documentSnapshot.getString ( "Date" );
                reqTVtime.setText ( time );
                reqTVDate.setText ( date );

            }
        } );
        db.collection ( "users" ).document (usr.getUid ()).get ().addOnSuccessListener ( new OnSuccessListener <DocumentSnapshot> () {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                stuname = documentSnapshot.getString ( "Firstname" ) + " "+ documentSnapshot.getString ( "Lastname" );
            }
        } );
    }

    @Override
    public void onClick(View v) {
        if (v == reqBTsend){
            sendRequst ( doctorfile );
            keepticket ( usr.getUid () );
            finish ();
        }else if (v == reqBTback){
            finish ();
        }

   }

    private void sendRequst(String x) {
        FirebaseUser usr = mAuth.getCurrentUser();
        Map<String, Object> req = new HashMap<>();
        req.put ( "Date" ,date);
        req.put ( "Time" , time );
        req.put ( "Filename", datefile );
        req.put ( "Name", stuname );
        req.put("Purpose",reqETpurpose.getText().toString());
        req.put ( "Status", "R");
        req.put("StudentID",usr.getUid());
        db.collection("users").document(x).collection("ARM")
                .document (datefile).update ( req ).addOnSuccessListener ( new OnSuccessListener <Void> () {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Request has been sent.", Toast.LENGTH_LONG).show();
            }
        } );
    }
    private void keepticket (String stuid){
        Map<String, Object> req = new HashMap<>();
        req.put ( "Date" ,date);
        req.put ( "Time" , time );
        req.put ( "DoctorID",doctorfile );
        req.put ( "Filename", datefile );
        req.put ( "Name", doctorname );
        req.put("Purpose",reqETpurpose.getText().toString());
        req.put ( "Status", "R");
        req.put("StudentID",usr.getUid());
        db.collection("users").document(stuid).collection("ARM")
                .document (datefile).set ( req ).addOnSuccessListener ( new OnSuccessListener <Void> () {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Request has been sent.", Toast.LENGTH_LONG).show();
            }
        } );
    }

}
