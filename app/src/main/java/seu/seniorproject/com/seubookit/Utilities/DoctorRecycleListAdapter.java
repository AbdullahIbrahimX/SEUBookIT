package seu.seniorproject.com.seubookit.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import seu.seniorproject.com.seubookit.R;
import seu.seniorproject.com.seubookit.Student.DoctorView;

public class DoctorRecycleListAdapter extends RecyclerView.Adapter<DoctorRecycleListAdapter.DRviewHolder> implements Filterable {

    List<DoctorVH> doctorVHList;
    List<DoctorVH> doctorVHListfull;
    Context context;
     public DoctorRecycleListAdapter(List<DoctorVH> doctorVHList,Context context) {
        this.doctorVHList= doctorVHList;
        doctorVHListfull = new ArrayList <> ( doctorVHList );
        this.context = context;
    }

    public void updateList(List<DoctorVH> newlist){
         doctorVHListfull = new ArrayList <> ( newlist );
    }

    @NonNull
    @Override
    public DRviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.professor_listview,viewGroup,false);
        DRviewHolder dRviewHolder = new DRviewHolder(view);
        return dRviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DRviewHolder viewHolder, final int i) {
        final String doctorfile = doctorVHList.get(i).getDoctorFile();
        viewHolder.drName.setText(doctorVHList.get(i).getDoctorName());
        viewHolder.drBranch.setText(doctorVHList.get(i).getDoctorBranch());
        viewHolder.drDepartment.setText(doctorVHList.get(i).getDoctorDepartment());
        //onClick listenr
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorView.class);
                intent.putExtra("Doctorfiles",doctorfile);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorVHList.size();
    }

    public class DRviewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView drName,drBranch,drDepartment;
        public ImageView drImage;
        public DRviewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            drName = mView.findViewById(R.id.professorlistTVname);
            drBranch = mView.findViewById(R.id.professorlistTVbranch);
            drDepartment = mView.findViewById(R.id.professorlistTVdepartment);
            drImage = mView.findViewById(R.id.professorlistImage);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter () {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DoctorVH> filterdlist = new ArrayList <> (  );

            if (constraint == null || constraint.length () == 0 || constraint==""){
                filterdlist.addAll ( doctorVHListfull );
            }else{
                String filterpattern = constraint.toString ().toLowerCase ().trim ();
                for (DoctorVH doctor :doctorVHListfull) {
                    if (doctor.getDoctorName ().toLowerCase ().contains ( filterpattern )){
                        filterdlist.add ( doctor );
                    }
                }
            }
            FilterResults results = new FilterResults ();
            results.values = filterdlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults r) {
            doctorVHList.clear ();
            doctorVHList.addAll ( (List<DoctorVH>)r.values );
            notifyDataSetChanged ();
        }
    };
}
