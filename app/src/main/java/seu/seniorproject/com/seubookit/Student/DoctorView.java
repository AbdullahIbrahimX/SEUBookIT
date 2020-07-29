package seu.seniorproject.com.seubookit.Student;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import seu.seniorproject.com.seubookit.Utilities.DoctorViewRecAdapter;
import seu.seniorproject.com.seubookit.R;
import seu.seniorproject.com.seubookit.Utilities.UserDH;

public class DoctorView extends AppCompatActivity {
    private ImageView doctorviewImage;
    private TextView doctorviewName, doctorviewDep,doctorviewBranch;
    private RecyclerView doctorviewDateList;
    private DoctorViewRecAdapter doctorViewRecAdapter;
    private FirebaseFirestore db;
    private List<UserDH> dateList;
    private String doctorFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_doctor_view);

        Intent bac = getIntent();
        doctorFile = bac.getStringExtra("Doctorfiles");

        doctorviewImage = findViewById(R.id.doctorviewImage);
        doctorviewName = findViewById(R.id.doctorviewName);
        doctorviewDep = findViewById(R.id.doctorviewDepartment);
        doctorviewBranch = findViewById ( R.id.doctorviewBranch );
        doctorviewDateList =findViewById(R.id.doctorviewDateList);

        doctorviewDep.clearFocus();
        dateList =new ArrayList<>();
        doctorViewRecAdapter = new DoctorViewRecAdapter(dateList,this);

        doctorviewDateList.setHasFixedSize(true);
        doctorviewDateList.setLayoutManager(new LinearLayoutManager(this));
        doctorviewDateList.setAdapter(doctorViewRecAdapter);



        db = FirebaseFirestore.getInstance();

        db.collection("users").document(doctorFile).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                doctorviewName.setText(documentSnapshot.getString("Firstname")+" "+documentSnapshot.getString("Lastname"));
                doctorviewDep.setText(documentSnapshot.getString("Department"));
                doctorviewBranch.setText ( documentSnapshot.getString ( "Branch" ) );

            }
        });
        Query av = db.collection ( "users" ).document (doctorFile).collection ( "ARM" ).whereEqualTo ( "Status","A" );

        av.get ().addOnSuccessListener ( new OnSuccessListener <QuerySnapshot> () {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty ()){
                    for (DocumentSnapshot doc:queryDocumentSnapshots) {
                        UserDH date = new UserDH ( doc.getString ( "Date" ), Color.WHITE,"Available" , doc.getString ( "DoctorID" ),doc.getString ( "Time" ));
                        date.setTime ( doc.getString ( "Time" ) );
                        date.setFilename ( doc.getString ( "Filename" ) );
                        dateList.add ( date );
                        doctorViewRecAdapter.notifyDataSetChanged ();
                    }
                }
            }
        } );
    }
}
