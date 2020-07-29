package seu.seniorproject.com.seubookit.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import seu.seniorproject.com.seubookit.R;
import seu.seniorproject.com.seubookit.Student.DoctorView;
import seu.seniorproject.com.seubookit.Student.RequistMeeting;

public class DoctorViewRecAdapter extends RecyclerView.Adapter<DoctorViewRecAdapter.ViewHolder> {
    private List<UserDH> dateList;
    private Context context;

    public DoctorViewRecAdapter(List<UserDH> dateList, Context context) {
        this.dateList = dateList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate( R.layout.doctor_view_recyclerview,viewGroup,false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final UserDH date = dateList.get(i);
        viewHolder.doctorviewDate.setText(date.getDate().substring(0,5));
        viewHolder.doctorviewEmpty.setText(date.getWord());
        viewHolder.doctorviewTime.setText ( date.getTime () );
        viewHolder.doctorviewBGcolor.setColorFilter(date.getBgColor());
        if (date.getWord().equals ("Available")) {
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RequistMeeting.class);
                    intent.putExtra("datefile", date.getFilename());
                    intent.putExtra("doctorfile", date.getParentfile());
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView doctorviewDate,doctorviewEmpty,doctorviewTime;
        ImageView doctorviewBGcolor;

        public ViewHolder( View itemView) {
            super(itemView);
            mView = itemView;
            doctorviewTime = mView.findViewById ( R.id.doctorviewTime );
            doctorviewDate = mView.findViewById(R.id.doctorviewDate);
            doctorviewEmpty = mView.findViewById(R.id.doctorviewEmpty);
            doctorviewBGcolor = mView.findViewById(R.id.doctorviewBGcolor);
        }
    }
}
