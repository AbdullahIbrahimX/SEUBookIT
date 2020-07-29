package seu.seniorproject.com.seubookit.Shared;

import android.content.Context;
import android.content.Intent;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import seu.seniorproject.com.seubookit.R;

public class MeetingView extends AppCompatActivity implements View.OnClickListener{
    private TextView meetingtvName,meetingtvDate,meetingtvTime,meetingtvPurpose;
    private Button requestbtAccept, requestbtReject;
    private FirebaseFirestore db;
    private DocumentReference file;
    private CollectionReference dfile,sfile;
    private String A;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_meeting_view);
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Intent bec = getIntent();
        A = bec.getStringExtra("filename");
        file = db.collection("users").document(usr.getUid());

        //variables to views
        meetingtvDate = findViewById(R.id.meetingTVDate);
        meetingtvTime = findViewById(R.id.meetingTVtime);
        meetingtvName = findViewById(R.id.meetingTVname);
        meetingtvPurpose = findViewById(R.id.meetingTVpurpose);
        requestbtAccept =findViewById(R.id.meetingBTaccept);
        requestbtReject = findViewById(R.id.meetingBTreject);

        //Doctor or Student
        file.get ().addOnSuccessListener ( new OnSuccessListener <DocumentSnapshot> () {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString ( "Type" ).equals ( "Student" )){
                    requestbtAccept.setVisibility ( View.GONE );
                }else if (documentSnapshot.getString ( "Type" ).equals ( "Doctor" )){
                    requestbtAccept.setOnClickListener ( new View.OnClickListener () {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> reg = new HashMap<> (  );
                            reg.put ( "Status", "A" );
                            reg.put ( "StudentID" , "");
                            reg.put ( "Name" , "");
                            reg.put ( "DoctorID" ,"");
                            reg.put ( "Purpose","" );
                            dfile.document (A).update ( reg );
                            Map<String,Object> sreg = new HashMap <> (  );
                            sreg.put ( "Status", "N" );
                            sfile.document (A).update ( sreg );
                            finish ();
                        }
                    } );
                }
            }
        } );


        //bring data
        file.collection("ARM").document(A).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            meetingtvName.setText(doc.getString("Name"));
                            meetingtvDate.setText(doc.getString("Date"));
                            meetingtvTime.setText(doc.getString("Time"));
                            meetingtvPurpose.setText(doc.getString("Purpose"));
                            dfile = db.collection ( "users" ).document (doc.getString ( "DoctorID" )).collection ( "ARM" );
                            sfile = db.collection ( "users" ).document (doc.getString ( "StudentID" )).collection ( "ARM" );
                        }else{
                            Toast.makeText(getApplicationContext(), "getting request failed"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        requestbtReject.setOnClickListener ( this );

    }

    @Override
    public void onClick(View v) {
        if (v == requestbtReject){
            this.finish ();
        }
    }
}
