package seu.seniorproject.com.seubookit.Utilities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class SEUNotification extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    
    @Override
    public void onCreate() {
        super.onCreate ();
        
        createNotificationchannels();
    }

    private void createNotificationchannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel (
                    CHANNEL_1_ID,
                    "channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription ( "defoult test" );

            NotificationManager manager = getSystemService ( NotificationManager.class );
            manager.createNotificationChannel ( channel1 );

        }
    }
}
