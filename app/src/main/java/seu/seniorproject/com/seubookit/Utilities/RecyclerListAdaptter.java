package seu.seniorproject.com.seubookit.Utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import seu.seniorproject.com.seubookit.R;
import seu.seniorproject.com.seubookit.Shared.MeetingView;
import seu.seniorproject.com.seubookit.Shared.RequestView;

public class RecyclerListAdaptter extends RecyclerView.Adapter<RecyclerListAdaptter.ViewHolder> {
    List<UserDH> userDHList;
    Context context;

    public RecyclerListAdaptter(List<UserDH> userDHList ,Context context) {

        this.userDHList = userDHList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.date_listview,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final String kind = userDHList.get(i).getReqormet();
        viewHolder.Name.setText(userDHList.get(i).getName());
        viewHolder.Date.setText(userDHList.get(i).getDate());
        String purpose = userDHList.get(i).getPurpose();

        if (purpose.length ()>= 26) {
            viewHolder.Purpose.setText(purpose.substring ( 0,25 ));
        }else{
            viewHolder.Purpose.setText ( purpose );
        }

        viewHolder.Time.setText ( userDHList.get ( i ).getTime () );
        switch (kind){
            case "A":
                viewHolder.BGcolor.setColorFilter( Color.WHITE );
                break;
            case "R":
                viewHolder.BGcolor.setColorFilter( Color.GRAY );
                break;
            case "M":
                viewHolder.BGcolor.setColorFilter( Color.GREEN );
                break;
            case "N":
                viewHolder.BGcolor.setColorFilter( Color.RED );
                break;
        }
        viewHolder.mView.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                switch (kind){
                    case "A":
                        viewHolder.BGcolor.setColorFilter( Color.WHITE );
                        Intent intenta = new Intent ( context,Delete.class );
                        intenta.putExtra ( "filename" , userDHList.get ( i ).getFilename ());
                        intenta.putExtra ( "kind", userDHList.get ( i ).getKind () );
                        viewHolder.mView.getContext ().startActivity ( intenta );
                        break;
                    case "R":
                        viewHolder.BGcolor.setColorFilter( Color.GRAY );
                        Intent intent = new Intent ( context,RequestView.class );
                        intent.putExtra ( "filename" , userDHList.get ( i ).getFilename ());
                        intent.putExtra ( "kind", userDHList.get ( i ).getKind () );
                        viewHolder.mView.getContext ().startActivity ( intent );
                        break;
                    case "M":
                        viewHolder.BGcolor.setColorFilter( Color.GREEN );
                        Intent intentm = new Intent ( context,MeetingView.class );
                        intentm.putExtra ( "filename" , userDHList.get ( i ).getFilename ());
                        intentm.putExtra ( "kind", userDHList.get ( i ).getKind () );
                        viewHolder.mView.getContext ().startActivity ( intentm );
                        break;
                    case "N":
                        break;
                }
            }
        } );


    }

    @Override
    public int getItemCount() {
        return userDHList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView Name,Date,Purpose,Time;
        public ImageView BGcolor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            Time = mView.findViewById ( R.id.datelistviewTime );
            Name = mView.findViewById(R.id.doctorviewEmpty);
            Date = mView.findViewById(R.id.dateTVdate);
            Purpose = mView.findViewById(R.id.dateTVpurpose);
            BGcolor = mView.findViewById(R.id.doctorviewBGcolor);

        }
    }
}
