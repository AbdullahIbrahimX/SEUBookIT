package seu.seniorproject.com.seubookit.Shared;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import seu.seniorproject.com.seubookit.Doctor.ManageAvTime;
import seu.seniorproject.com.seubookit.R;

public class Register extends AppCompatActivity implements View.OnClickListener {
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@seu\\.edu\\.sa$"
            );
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +"(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\S+$)" + ".{4,}" +
                    "$");

    private EditText etEmail,etPassword,etFirstname,etLastname,etPhonenumber,etRoom,etId;
    private Button btRegister, btCancel;
    private FirebaseAuth mAuth;
    private Spinner spType,spBranch,spDepartment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String Email,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_register);

        etEmail = findViewById(R.id.ETemail);
        etPassword = findViewById(R.id.ETpassword);
        etFirstname = findViewById(R.id.ETfirstname);
        etLastname = findViewById(R.id.ETlastname);
        etPhonenumber = findViewById(R.id.ETphonenumber);
        etRoom = findViewById(R.id.ETroom);
        etId = findViewById(R.id.ETid);
        spType = findViewById(R.id.SPtype);
        spBranch =findViewById(R.id.SPbranch);
        spDepartment =findViewById(R.id.Spdepartment);

        //all about dropdown menus
        String[] types = new String[]{"Type","Student","Doctor"};
        String[] branches = new String[]{"Branch","Dammam","Riyadh","Jeddah"};
        String[] departments = new String[]{"Department","IT","Commerce","Law"};
        ArrayAdapter<CharSequence> spAdapter = new ArrayAdapter(this,R.layout.spinner_layout,types);
        ArrayAdapter<CharSequence> spBranchAdp = new ArrayAdapter(this,R.layout.spinner_layout,branches);
        ArrayAdapter<CharSequence> spDepAdp = new ArrayAdapter(this,R.layout.spinner_layout,departments);
        spAdapter.setDropDownViewResource(R.layout.spinner_layout);
        spBranchAdp.setDropDownViewResource(R.layout.spinner_layout);
        spDepAdp.setDropDownViewResource(R.layout.spinner_layout);
        spType.setAdapter(spAdapter);
        spBranch.setAdapter(spBranchAdp);
        spDepartment.setAdapter(spDepAdp);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Student")){
                    etRoom.setVisibility(View.GONE);
                }else{
                    etRoom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mAuth = FirebaseAuth.getInstance();

        btRegister = findViewById(R.id.BTregister);
        btCancel = findViewById(R.id.BTcancel);

        btRegister.setOnClickListener(this);
        btCancel.setOnClickListener(this);
    }


    private void registerNew() {
        //register the user
        if (checkValid ()){
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User is registered succesfully please validate your email ", Toast.LENGTH_LONG).show();
                        //update user info
                        FirebaseUser usr = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileupdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(etFirstname.getText().toString() + " " + etLastname.getText().toString())
                                .build();
                        usr.updateProfile(profileupdate);
                        String Uid = usr.getUid();
                        //add user to database
                        Map<String, Object> reg = new HashMap<>();
                        reg.put("Document ID", etId.getText().toString());
                        reg.put("Firstname", etFirstname.getText().toString());
                        reg.put("Lastname", etLastname.getText().toString());
                        reg.put("Phonenumber", etPhonenumber.getText().toString());
                        reg.put("Email", etEmail.getText().toString());
                        reg.put("Department", spDepartment.getSelectedItem().toString());
                        reg.put("Branch", spBranch.getSelectedItem().toString());
                        reg.put("Type", spType.getSelectedItem().toString());
                        if (spType.getSelectedItem().toString().equals("Doctor")) {
                            reg.put("Room", etRoom.getText().toString());
                        }
                        db.collection("users").document(Uid)
                                .set(reg).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "User is added to DB", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "database error: " + e, Toast.LENGTH_LONG).show();
                            }
                        });
                        finish ();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error, Something went wrong, try again later", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        FirebaseUser user = mAuth.getCurrentUser ();
        user.sendEmailVerification ();
    }
    private boolean checkValid(){
        Email = etEmail.getText().toString().trim();
        Password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(Email)) {
            etEmail.setError("Please enter an E-mail.");
            return false;
        }else if (TextUtils.isEmpty(Password)) {
            etPassword.setError("please choose a password");
            return false;
        }else if (!EMAIL_PATTERN.matcher(Email).matches()){
            etEmail.setError("Please enter a valid email");
            return false;
        }else if(!PASSWORD_PATTERN.matcher(Password).matches()){
            etPassword.setError("Password has to be more than 6 characters and contain upper and lower case characters,no spaces and at least one spacial character @#$%^&+= ");
            return false;
        }else if (TextUtils.isEmpty(spType.getSelectedItem ().toString ())) {
            Toast.makeText(getApplicationContext(), "Please Chose a user type", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(spBranch.getSelectedItem ().toString ())) {
            Toast.makeText(getApplicationContext(), "Please Chose a branch", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(spDepartment.getSelectedItem ().toString ())) {
            Toast.makeText(getApplicationContext(), "Please Chose a department", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    @Override
    public void onClick(View v) {
        if (v == btCancel){
            finish();
        }
        if (v == btRegister){
            registerNew();
        }
    }
}
