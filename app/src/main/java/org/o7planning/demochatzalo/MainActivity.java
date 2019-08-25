package org.o7planning.demochatzalo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText edtUn;
    Button btn;
    // soi day noi giua android va nodejs
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.96:3000");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocket.connect();
        mSocket.on("ketquadangkyUn", onNewketqua);
        edtUn =(EditText)findViewById(R.id.editTextUserName);
        btn =(Button)findViewById(R.id.buttonDangKy);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mSocket.emit("client-gui-username",edtUn.getText().toString());
            }
        });
    }
    private Emitter.Listener onNewketqua = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data =(JSONObject)args[0];
                    String noidung;
                    try {
                        noidung=data.getString("noidung");
                        if (noidung=="true"){
                            Toast.makeText(getApplicationContext(),"dang ky thang cong" ,Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"dang ky that bai" ,Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };
}
