package seu.seniorproject.com.seubookit.Shared;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import seu.seniorproject.com.seubookit.R;

public class User_Information extends AppCompatActivity {
    private String userFile,userName,userType,userBranch,userDepartment,userEmail,userPhone,userID;
    private TextView tvFull, tvID, tvBranch,tvDep,tvPhone,tvEmail,tvType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_user__information);

        tvFull =findViewById(R.id.TVfull);
        tvID =findViewById(R.id.TVid);
        tvBranch =findViewById(R.id.TVbranch);
        tvDep =findViewById(R.id.TVdep);
        tvPhone =findViewById(R.id.TVphone);
        tvEmail =findViewById(R.id.TVemail1);
        tvType =findViewById(R.id.TVtype);

        Intent extras = getIntent();
        userFile = extras.getStringExtra("userFile");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDoc = db.collection("users").document(userFile);

        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userInfo = task.getResult();
                    userName = userInfo.getString("Firstname") + " " + userInfo.getString("Lastname");
                    userType = userInfo.getString("Type");
                    userBranch = userInfo.getString("Branch");
                    userDepartment = userInfo.getString("Department");
                    userEmail = userInfo.getString("Email");
                    userPhone = userInfo.getString("Phonenumber");
                    userID=userInfo.getString("Document ID");
                    tvFull.setText(userName);
                    tvID.setText(userID);
                    tvBranch.setText(userBranch);
                    tvDep.setText(userDepartment);
                    tvPhone.setText(userPhone);
                    tvEmail.setText(userEmail);
                    tvType.setText(userType);
                }else{
                    Toast.makeText(getApplicationContext(), "Loading document failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
