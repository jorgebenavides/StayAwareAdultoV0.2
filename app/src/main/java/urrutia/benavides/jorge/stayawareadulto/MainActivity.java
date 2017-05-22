package urrutia.benavides.jorge.stayawareadulto;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnconectar, btnalerta;

    public static TextView firmware, software;
    private static final int BluetoothON = 1;

    BluetoothAdapter wwmBluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnconectar = (Button)(findViewById(R.id.btnconectarMain));
        firmware = (TextView) findViewById(R.id.firmware);
        software = (TextView) findViewById(R.id.software);
        btnalerta = (Button)(findViewById(R.id.btnpanicoMain));
        btnalerta.setVisibility(View.INVISIBLE);

        wwmBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(wwmBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Your phone does not support bluetooth", Toast.LENGTH_LONG).show();
        } else if(!wwmBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BluetoothON);
        }

        btnconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent abrirListabb = new Intent(MainActivity.this, RouterDevices.class);
                startActivity(abrirListabb);
            }
        });
    }

    public Context myContext(){
        return MainActivity.this;
    }

    public void setTextViewText(int id, String text){
        TextView tv = (TextView) findViewById(id);
        tv.setText(text);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case BluetoothON:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "Bluetooth enabled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth could not be activated, application will be closed.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }
}
