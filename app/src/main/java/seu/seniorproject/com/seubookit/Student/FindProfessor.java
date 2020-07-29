package seu.seniorproject.com.seubookit.Student;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import seu.seniorproject.com.seubookit.R;
import seu.seniorproject.com.seubookit.Utilities.DoctorRecycleListAdapter;
import seu.seniorproject.com.seubookit.Utilities.DoctorVH;

public class FindProfessor extends AppCompatActivity{
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private DoctorRecycleListAdapter doctorRecycleListAdapter;
    private List<DoctorVH> doctorlist;
    private Query searchf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_find_professor);

        doctorlist = new ArrayList<>();

        doctorRecycleListAdapter = new DoctorRecycleListAdapter(doctorlist,this);

        recyclerView = findViewById(R.id.Doctorlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(doctorRecycleListAdapter);


        db =FirebaseFirestore.getInstance();

        searchf = db.collection("users").whereEqualTo("Type","Doctor");
        searchf.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        DoctorVH doctor = new DoctorVH(doc.getString("Firstname") + " " + doc.getString("Lastname"),
                                doc.getString("Branch")
                                , doc.getString("Department"));
                        doctor.setDoctorFile(doc.getId ());
                        doctorlist.add(doctor);
                        doctorRecycleListAdapter.notifyDataSetChanged();
                    }
                    doctorRecycleListAdapter.updateList ( doctorlist );
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater ();
        inflater.inflate ( R.menu.doctor_seaerch_menu,menu );

        MenuItem search = menu.findItem ( R.id.search );
        SearchView searchView = (SearchView) MenuItemCompat.getActionView ( search );

        searchView.setOnQueryTextListener ( new SearchView.OnQueryTextListener () {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doctorRecycleListAdapter.getFilter ().filter ( newText );
                doctorRecycleListAdapter.notifyDataSetChanged ();
                return true;
            }
        } );
        return true;
        //return super.onCreateOptionsMenu ( menu );
    }
}