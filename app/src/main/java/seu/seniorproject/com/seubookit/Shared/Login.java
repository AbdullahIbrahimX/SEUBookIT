package seu.seniorproject.com.seubookit.Shared;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import seu.seniorproject.com.seubookit.R;


public class Login extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail, etPassword;
    private Button btLogin;
    private TextView tvRegiste,loginError;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login);

        etEmail = findViewById(R.id.ETemail);
        etPassword= findViewById(R.id.ETpassword);
        tvRegiste =findViewById(R.id.TVregister);
        btLogin = findViewById(R.id.BTlogin);
        loginError = findViewById ( R.id.loginError );

        firebaseAuth = FirebaseAuth.getInstance();

        btLogin.setOnClickListener(this);
        tvRegiste.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart ();

        FirebaseUser fast = firebaseAuth.getCurrentUser ();
        if (fast != null) {
            finish ();
            startActivity ( new Intent ( this, MainActivity.class ) );
        }
    }

    private void userLogin(){
        //extract strings from fields
        String Email = etEmail.getText().toString().trim();
        String Password = etPassword.getText().toString().trim();
        //enforce field filling (No Empty check)
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(getApplicationContext(), "Please enter an E-mail.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Password)) {
            Toast.makeText(getApplicationContext(), "Please enter a Password", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(Email,Password).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.toString ().contains ("network")){
                    loginError.setText ( "Login failed: Network error" );
                }else if (e.toString ().contains ( "email" ) || e.toString ().contains ( "password" )){
                    etEmail.setError ( "" );
                    etPassword.setError ( "" );
                    loginError.setText ( "Login failed: Email or password is wrong" );
                }
            }
        } );
        user = firebaseAuth.getCurrentUser ();
        if (user.isEmailVerified ()){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }else if (!user.isEmailVerified ()){
            loginError.setVisibility ( View.VISIBLE );
            loginError.setText ( "Email is not verified. Press here to send it again" );
            loginError.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification ();
                    Toast.makeText(getApplicationContext(), "Verification email is sent.", Toast.LENGTH_SHORT).show();
                    loginError.setVisibility ( View.GONE );
                }
            } );
        }
    }

    @Override
    public void onClick(View v) {
        if (v == tvRegiste){
            startActivity(new Intent(getApplicationContext(), Register.class));
        }
        if (v == btLogin){
            userLogin();
        }

    }
}
