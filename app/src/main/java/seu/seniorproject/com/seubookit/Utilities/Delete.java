package seu.seniorproject.com.seubookit.Utilities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import seu.seniorproject.com.seubookit.R;

public class Delete extends AppCompatActivity implements View.OnClickListener {
    private TextView deleteName,deleteDate,deleteTime,deletePurpose;
    private Button deleteRemove, deleteBack;
    private FirebaseFirestore db;
    private CollectionReference dfile;
    private FirebaseUser usr;
    private String A;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_delete );

        deleteName =findViewById ( R.id.deleteName );
        deleteDate = findViewById ( R.id.deleteDate );
        deleteTime = findViewById ( R.id.deleteTime );
        deletePurpose = findViewById ( R.id.deletePurpose );
        deleteRemove =findViewById ( R.id.deleteRemove );
        deleteBack = findViewById ( R.id.deleteBack );

        usr = FirebaseAuth.getInstance ().getCurrentUser ();
        db = FirebaseFirestore.getInstance ();
        Intent bac = getIntent ();
        A = bac.getStringExtra ( "filename" );
        dfile = db.collection("users").document(usr.getUid()).collection("ARM");


        dfile.document (A).get ().addOnSuccessListener ( new OnSuccessListener <DocumentSnapshot> () {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                deleteName.setText ( documentSnapshot.getString ( "Name" ) );
                deleteDate.setText ( documentSnapshot.getString ( "Date" ) );
                deleteTime.setText ( documentSnapshot.getString ( "Time" ) );
                deletePurpose.setText ( documentSnapshot.getString ( "Purpose" ) );
            }
        } );
        deleteRemove.setOnClickListener ( this );

    }

    @Override
    public void onClick(View v) {
        if ( v==deleteRemove){
            dfile.document (A).delete ();
            finish ();
        }else if (v==deleteBack){
            finish ();
        }
    }
}
