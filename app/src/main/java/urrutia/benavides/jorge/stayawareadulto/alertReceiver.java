package urrutia.benavides.jorge.stayawareadulto;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class alertReceiver extends Service {
    public static final int notification_id = 1;
    String id = "";
    // Handler that receives messages from the thread

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "Servicio monitor iniciado...", Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);

                id = intent.getExtras().getString("idRedApoyo");
                final String funcion = "viewAlertas";


            }

        };


        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true)
                {
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(0);

                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }
        }).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio Monitoreo Detenido!", Toast.LENGTH_SHORT).show();
    }

}
