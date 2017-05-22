package urrutia.benavides.jorge.stayawareadulto;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.message.Acceleration;
import com.punchthrough.bean.sdk.message.BatteryLevel;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.LedColor;
import com.punchthrough.bean.sdk.message.ScratchBank;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static urrutia.benavides.jorge.stayawareadulto.alertReceiver.notification_id;


public class alertDispatcher extends Service {
    String texto = null;
    String LOGIN_MAC = "https://www.bancademia.net/StayAware/macLogin.php";
    String id_AdultoMayor = "";
    private StringBuilder sb = new StringBuilder();
    @Nullable
    //@Override
    //public alertDispatcher() {
    //}
    public void onCreate() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private void userLogin(final String username) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject obj = new JSONObject(s);
                    Toast.makeText(getApplicationContext(),"Conectado Exitosamente",Toast.LENGTH_LONG).show();
                    id_AdultoMayor = obj.getString("AdultoMayor_id_AdultoMayor");

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"El Dispositivo no se encuentra registrado",Toast.LENGTH_LONG).show();
                    stopService(new Intent(alertDispatcher.this, alertDispatcher.class));
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("mac", params[0]);

                HttpQuerys ruc = new HttpQuerys();

                String result = ruc.sendPostRequest(LOGIN_MAC, data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        final String funcion = "addAlerta";
        final String idAdultoMayor = intent.getExtras().getString("idAdultoMayor");
        Toast.makeText(getApplicationContext(), "Intentado conexión...", Toast.LENGTH_SHORT).show();
        final Bean beanConnect = (Bean) intent.getExtras().get("connect");
        final String MacBean = beanConnect.getDevice().getAddress();
        final LedColor green = LedColor.create(0, 255, 0);
        final LedColor blue = LedColor.create(0,0,255);
        final LedColor red = LedColor.create(255,0,0);
        final LedColor off = LedColor.create(0, 0, 0);



        final BeanListener beanListener = new BeanListener() {

            @Override
            public void onConnected() {
                Toast.makeText(getApplicationContext(), "Enlanzado con dispositivo...", Toast.LENGTH_SHORT).show();
                String mac = beanConnect.getDevice().getAddress();
                String macc = mac.replace(":","");
                Toast.makeText(getApplicationContext(),macc,Toast.LENGTH_LONG).show();

                final Handler handler = new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        // TODO Auto-generated method stub
                        super.handleMessage(msg);
                        beanConnect.readTemperature(new com.punchthrough.bean.sdk.message.Callback<Integer>() {
                            @Override
                            public void onResult(Integer result) {
                                MainActivity.firmware.setText("Temperatura: "+ result);

                                if(result>31){
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http//www.unab.cl"));
                                    PendingIntent pendingIntent = PendingIntent.getActivity(alertDispatcher.this,0,intent,0);

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(alertDispatcher.this);
                                    builder.setSmallIcon(R.mipmap.ic_launcher);
                                    builder.setContentIntent(pendingIntent);
                                    builder.setAutoCancel(true);
                                    builder.setVibrate(new long[] {1000,1000,1000,1000,1000,1000});
                                    builder.setTicker("Alerta de Temperatura Alta! - StayAware");
                                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
                                    builder.setContentTitle("Alerta de Temperatura Alta!");
                                    builder.setContentTitle("El adulto mayor esta sobre la temperatura normal");
                                    builder.setSubText("Toque para atender alerta");

                                    NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                    notificationManager.notify(notification_id,builder.build());
                                }else if(result<27){
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http//www.unab.cl"));
                                    PendingIntent pendingIntent = PendingIntent.getActivity(alertDispatcher.this,0,intent,0);

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(alertDispatcher.this);
                                    builder.setSmallIcon(R.mipmap.ic_launcher);
                                    builder.setContentIntent(pendingIntent);
                                    builder.setAutoCancel(true);
                                    builder.setVibrate(new long[] {1000,1000,1000,1000,1000,1000});
                                    builder.setTicker("Alerta de Temperatura Baja! - StayAware");
                                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
                                    builder.setContentTitle("Alerta de Temperatura Baja!");
                                    builder.setContentTitle("El adulto mayor esta bajo la temperatura normal");
                                    builder.setSubText("Toque para atender alerta");

                                    NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                                    notificationManager.notify(notification_id,builder.build());
                                }
                            }
                        });
                        beanConnect.readAcceleration(new com.punchthrough.bean.sdk.message.Callback<Acceleration>() {
                            @Override
                            public void onResult(Acceleration result) {
                                double x = result.x() *100;
                                double y = result.y() *100;
                                double z = result.z() *100;
                                MainActivity.software.setText("Eje X: "+x+"\r\n Eje Y: "+y+"\r\nEje Z: "+z);
                            }
                        });

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
            }

            @Override
            public void onConnectionFailed() {
                beanConnect.setLed(red);
                Toast.makeText(getApplicationContext(), "Error de Conexion", Toast.LENGTH_SHORT).show();
                beanConnect.setLed(off);
            }

            @Override
            public void onDisconnected() {
            }
            @Override
            public void onSerialMessageReceived(byte[] data) {

                beanConnect.setLed(off);
                String s = new String(data);
                texto = s;

                /*sb.append(s);                                                // append string
                int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                if (endOfLineIndex > 0) {                                            // if end-of-line,
                    String sbprint = sb.substring(0, endOfLineIndex);               // extract string
                    sb.delete(0, sb.length());                                      // and clear
                    if(sbprint.equals("CAIDA")) {
                        for(int i=1;i<10;i++) {
                            beanConnect.setLed(red);
                            beanConnect.setLed(blue);
                            beanConnect.setLed(red);
                            beanConnect.setLed(blue);
                            beanConnect.setLed(red);
                        }
                        beanConnect.setLed(off);


                    }
                }*/

            }

            @Override
            public void onScratchValueChanged(ScratchBank bank, byte[] value) {

            }

            @Override
            public void onError(BeanError error) {
                Toast.makeText(getApplicationContext(), "Error Bean: " + error, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onReadRemoteRssi(int rssi) {

            }
        };
        beanConnect.connect(this, beanListener);
        return START_NOT_STICKY; //indica que el servicio no debe recrearse al ser destruido sin importar que haya quedado un trabajo pendiente.
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Servicio destruido...", Toast.LENGTH_SHORT).show();

    }


}
