package seu.seniorproject.com.seubookit.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import seu.seniorproject.com.seubookit.R;

public class AVtimeAdapter extends RecyclerView.Adapter<AVtimeAdapter.ViewHolder>{
    private List<Map<String, Object>> dateList;
    private Context context;

    public AVtimeAdapter(List <Map<String, Object>> dateList, Context context) {
        this.dateList = dateList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate( R.layout.doctor_view_recyclerview,viewGroup,false);
        AVtimeAdapter.ViewHolder v = new AVtimeAdapter.ViewHolder (view);
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Map<String, Object> date = dateList.get ( i );
        viewHolder.doctorviewEmpty.setText ( date.get ( "Time" ).toString () );
        viewHolder.doctorviewDate.setText ( date.get ( "Date" ).toString () );
    }

    @Override
    public int getItemCount() {
        return dateList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView doctorviewDate,doctorviewEmpty;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            mView = itemView;
            doctorviewDate = mView.findViewById(R.id.doctorviewDate);
            doctorviewEmpty = mView.findViewById(R.id.doctorviewEmpty);
        }
    }
}
