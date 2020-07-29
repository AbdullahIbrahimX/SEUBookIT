package seu.seniorproject.com.seubookit.Shared;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import seu.seniorproject.com.seubookit.R;

public class RequestView extends AppCompatActivity implements View.OnClickListener {
    private TextView requesttvName,requesttvDate,requesttvTime,requesttvPurpose;
    private Button requestbtAccept, requestbtReject;
    private FirebaseFirestore db;
    private CollectionReference dfile,sfile;
    private String A;
    private FirebaseUser usr;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_request_view);

        context = getApplicationContext ();
        usr = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Intent bec = getIntent();
        A = bec.getStringExtra("filename");
        dfile = db.collection("users").document(usr.getUid()).collection("ARM");


        //variables to views
        requesttvDate = findViewById(R.id.requestvTVDate);
        requesttvTime = findViewById(R.id.requestvTVtime);
        requesttvName = findViewById(R.id.requestvTVname);
        requesttvPurpose = findViewById(R.id.requestvTVpurpose);
        requestbtAccept =findViewById(R.id.requestvBTaccept);
        requestbtReject = findViewById(R.id.requestvBTreject);

        //Student or Doctor
        db.collection ( "users" ).document (usr.getUid ()).get ().addOnSuccessListener ( new OnSuccessListener <DocumentSnapshot> () {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString ( "Type" ).equals ( "Student" )){
                    requestbtAccept.setVisibility ( View.GONE );
                    requestbtReject.setText ( "Withdraw" );
                    requestbtReject.setOnClickListener ( new View.OnClickListener () {
                        @Override
                        public void onClick(View v) {
                            db.collection ( "users" ).document (usr.getUid ()).collection ( "ARM" ).document (A).delete ();
                            finish ();
                        }
                    } );
                }else if (documentSnapshot.getString ( "Type" ).equals ( "Doctor" )){
                    requestbtAccept.setOnClickListener ( new View.OnClickListener () {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> reg = new HashMap <> (  );
                            reg.put ( "Status","M" );
                            reg.put ( "Time",requesttvTime.getText ().toString () );
                            sfile.document (A).update ( reg );
                            dfile.document (A).update ( reg );
                        }
                    } );
                    requestbtReject.setOnClickListener ( new View.OnClickListener () {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> reg = new HashMap <> (  );
                            reg.put ( "Status", "A" );
                            reg.put ( "StudentID" , "");
                            reg.put ( "Name" , "");
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
        dfile.document(A).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            sfile = db.collection ( "users" ).document (doc.getString ( "StudentID" )).collection ( "ARM" );
                            requesttvName.setText(doc.getString("Name"));
                            requesttvDate.setText(doc.getString("Date"));
                            requesttvTime.setText(doc.getString("Time"));
                            requesttvPurpose.setText(doc.getString("Purpose"));

                        }else{
                            Toast.makeText(getApplicationContext(), "getting request failed"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v==requestbtAccept){

        }else if(v==requestbtReject) {
                    }

    }

}
