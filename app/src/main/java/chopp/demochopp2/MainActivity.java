package chopp.demochopp2;

import android.Manifest;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "AC6184873b9746c24f0615b192bb3194cd";
    public static final String AUTH_TOKEN = "1424d881a14d0328fec55bcd21b4a6fa";
    EditText editphone;
    Button btnsend;
    private String TAG ="Chopp On";
    private HidesoftKeyBoard hidesoftKeyBoard;
    /**
     * Regex to validate the mobile number
     * mobile number should be of 10 digits length
     *
     * @param mobile
     * @return
     */
    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        String regEx11 = "^[0-9]{11}$";
        return (mobile.matches(regEx) || (mobile.matches(regEx11)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chopp_activity);
        CallControl();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy( new StrictMode.ThreadPolicy.Builder().permitAll().build() );
        }
        CallHideKeyBoard();
    }
    private void CallHideKeyBoard() {
        hidesoftKeyBoard = new HidesoftKeyBoard(this);
        hidesoftKeyBoard.setupUI(findViewById(R.id.parent));
    }

    private void CallControl() {
        editphone = (EditText) findViewById(R.id.edtphone);
        btnsend = (Button) findViewById(R.id.btnsend);
        btnsend.setOnClickListener(this);
    }

    private void validateForm() {
        String mobile = editphone.getText().toString();
        //sendSms(mobile,"helu");
        if (isValidPhoneNumber(mobile)) {
            //call to method request to server
            sendSms(mobile,"helu");

        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSms(String toPhoneNumber, String message){
        String fromPhoneNumber ="+16173000422";
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.twilio.com/2010-04-01/Accounts/"+ACCOUNT_SID+"/SMS/Messages";
        String base64EncodedCredentials = "Basic " + Base64.encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes(), Base64.NO_WRAP);
        String phone = "+84"+toPhoneNumber.substring(1);
        RequestBody body = new FormBody.Builder()
                .add("From", fromPhoneNumber)
                .add("To", phone)
                .add("Body", message)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", base64EncodedCredentials)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d(TAG, "sendSms: "+ response.body().string());
            Toast.makeText(this,"thanh cong",Toast.LENGTH_SHORT).show();
        } catch (IOException e) { e.printStackTrace(); }


    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnsend){
            validateForm();
        }
    }
}
