package seu.seniorproject.com.seubookit.Shared;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import seu.seniorproject.com.seubookit.Doctor.ManageAvTime;
import seu.seniorproject.com.seubookit.R;
import seu.seniorproject.com.seubookit.Utilities.RecyclerListAdaptter;
import seu.seniorproject.com.seubookit.Student.FindProfessor;
import seu.seniorproject.com.seubookit.Utilities.UserDH;
import static seu.seniorproject.com.seubookit.Utilities.SEUNotification.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView tvFullname,tvEmail;
    private String usrFile;
    private String kinsd = "f";
    private RecyclerView recyclerView;
    private RecyclerListAdaptter recyclerListAdaptter;
    private List<UserDH> recycleviewlist;
    public FloatingActionButton fab;
    private Menu menu;
    private Context context;
    private NotificationManagerCompat notificationManager;
    private int j = 1;
    private FirebaseUser usr;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View header_nav;
        setContentView( R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        notificationManager = NotificationManagerCompat.from ( this );

        context = getApplicationContext ();
        recycleviewlist =new ArrayList<>();
        recyclerListAdaptter = new RecyclerListAdaptter(recycleviewlist, context);

        recyclerView = findViewById(R.id.Listmain);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerListAdaptter);

        usr = FirebaseAuth.getInstance ().getCurrentUser ();
        usrFile =usr.getUid ();

        fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        header_nav = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        tvFullname = header_nav.findViewById(R.id.Tvfullname);
        tvEmail = header_nav.findViewById(R.id.Tvemail);
        menu = navigationView.getMenu ();

        loadUserinfo(usrFile);
        getDates(usrFile);


    }

    private void loadUserinfo(final String userId) {
        //insiate user variables
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userdata = db.collection("users").document(userId);

        userdata.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tvEmail.setText ( documentSnapshot.getString ( "Email" ) );
                tvFullname.setText(documentSnapshot.getString("Firstname") +" "+ documentSnapshot.getString("Lastname"));
                String type = documentSnapshot.getString ( "Type" );
                kinsd = type;
                if (type.equals("Student")){
                        fab.setVisibility(View.VISIBLE);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent int_usr = new Intent(getApplicationContext(), FindProfessor.class);
                                int_usr.putExtra("userFile",usrFile );
                                startActivity(int_usr);}
                        });
                        menu.findItem ( R.id.nav_Avtime ).setVisible ( false );
                } else if (documentSnapshot.getString ( "Type" ).equals ( "Doctor" )){
                        fab.setVisibility ( View.GONE );
                }
            }
        });
    }

    public void getDates(String userId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userdata = db.collection("users").document(userId);
        CollectionReference ARM = userdata.collection("ARM");


        ARM.addSnapshotListener ( new EventListener <QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    //TODO
                    return;
                }
                if (queryDocumentSnapshots.isEmpty ()){
                    return;
                }
                for ( final DocumentChange doc:queryDocumentSnapshots.getDocumentChanges ())
                    switch (doc.getType ()) {
                        case ADDED:
                            UserDH usrInf =  doc.getDocument ().toObject ( UserDH.class );
                            usrInf.setFilename(doc.getDocument ().getId());
                            usrInf.setReqormet ( doc.getDocument ().getString ( "Status" ) );
                            usrInf.setKind ( kinsd );
                            recycleviewlist.add(usrInf);
                            recyclerListAdaptter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            UserDH usrInfm = doc.getDocument ().toObject ( UserDH.class );
                            usrInfm.setFilename ( doc.getDocument ().getId () );
                            usrInfm.setReqormet ( doc.getDocument ().getString ( "Status" ) );
                            usrInfm.setKind ( kinsd );
                            String notificationstatus = usrInfm.getReqormet ();
                            if (notificationstatus.equals ( "A" )){
                                return;
                            }else if (notificationstatus.equals ( "R" )){
                                Notification notification = new NotificationCompat.Builder(context , CHANNEL_1_ID)
                                        .setSmallIcon ( R.drawable.logo )
                                        .setContentTitle ("New meeting request from "+usrInfm.getName ())
                                        .setStyle ( new NotificationCompat.BigTextStyle (  ).bigText ( usrInfm.getDate () + " " +usrInfm.getTime () + "\n"+ usrInfm.getPurpose ()) )
                                        .setPriority ( NotificationCompat.PRIORITY_HIGH )
                                        .setCategory ( NotificationCompat.CATEGORY_MESSAGE)
                                        .build ();
                                notificationManager.notify (j, notification );
                                j++;
                            }else if(notificationstatus.equals ( "M" )){
                                if(kinsd.equals ( "Student" )){
                                    Notification notification = new NotificationCompat.Builder(context , CHANNEL_1_ID)
                                            .setSmallIcon ( R.drawable.logo )
                                            .setContentText ( usrInfm.getPurpose ())
                                            .setContentTitle ( "A meeting with Dr. "+usrInfm.getName () + " has been accepted")
                                            .setPriority ( NotificationCompat.PRIORITY_HIGH )
                                            .setCategory ( NotificationCompat.CATEGORY_MESSAGE)
                                            .build ();
                                    notificationManager.notify (j, notification );
                                    j++;
                                }else if(kinsd.equals ( "Doctor" )){
                                    return;
                                }
                            }else if (notificationstatus.equals ( "N" )){
                                Notification notification = new NotificationCompat.Builder(context , CHANNEL_1_ID)
                                        .setSmallIcon ( R.drawable.logo )
                                        .setContentText ( usrInfm.getPurpose ())
                                        .setContentTitle ( "Dr. "+ usrInfm.getName () + " rejected your request for the meeting")
                                        .setPriority ( NotificationCompat.PRIORITY_HIGH )
                                        .setCategory ( NotificationCompat.CATEGORY_MESSAGE)
                                        .build ();
                                notificationManager.notify (j, notification );
                                j++;
                            }
                            for (Iterator<UserDH> i = recycleviewlist.iterator (); i.hasNext (); ){
                                UserDH rm = i.next ();
                                if(doc.getDocument ().getString ( "Filename" ).equals ( rm.getFilename () )){
                                    i.remove ();
                                }
                            }
                            recycleviewlist.add ( usrInfm );
                            recyclerListAdaptter.notifyDataSetChanged ();
                            break;
                        case REMOVED:
                            for (Iterator<UserDH> i = recycleviewlist.iterator (); i.hasNext (); ){
                                UserDH rm = i.next ();
                                if(doc.getDocument ().getString ( "Filename" ).equals ( rm.getFilename () )){
                                    i.remove ();
                                }
                            }
                            recyclerListAdaptter.notifyDataSetChanged ();
                            break;
                    }
            }


        } );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_user) {
            Intent int_usr = new Intent(getApplicationContext(), User_Information.class);
            int_usr.putExtra("userFile",usrFile );
            startActivity(int_usr);
        } else if (id == R.id.nav_Avtime) {
            Intent int_usr = new Intent(getApplicationContext(), ManageAvTime.class);
            int_usr.putExtra("userFile", usrFile);
            startActivity(int_usr);
        } else if (id == R.id.nav_Requests) {
            FirebaseAuth firebaseAuth =FirebaseAuth.getInstance ();
            firebaseAuth.signOut ();
            startActivity ( new Intent ( this,Login.class ) );
            finish ();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
