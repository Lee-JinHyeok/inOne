package com.example.inone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inone.network.RestAPI;
import com.example.inone.prefs.SharedPreferencesManager;
import com.example.inone.vo.InOneData;
import com.example.inone.vo.InOneDataAll;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.coinone.co.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RestAPI restAPI = retrofit.create(RestAPI.class);

    float kspLast = 0.0f;
    float klayLast = 0.0f;

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView valueRatioTextView = findViewById(R.id.valueRatioTextView);
        TextView valueTimeTextView = findViewById(R.id.valueTimeTextView);

        EditText klayEdit = findViewById(R.id.klayEdit);
        EditText kspEdit = findViewById(R.id.kspEdit);

        float sharedKlayCheck = SharedPreferencesManager.getFloat(mContext, "klay");
        float sharedKspCheck = SharedPreferencesManager.getFloat(mContext, "ksp");

        if(sharedKlayCheck != 0.0 || sharedKspCheck != 0.0){
            klayEdit.setText(""+sharedKlayCheck);
            kspEdit.setText(""+sharedKspCheck);
            showRatio();
        }

        Button saveValueBtn = findViewById(R.id.saveValueBtn);
        Button valueResetBtn = findViewById(R.id.valueResetBtn);
        Button callValueBtn = findViewById(R.id.callValueBtn);

        //저장버튼클릭
        saveValueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(klayEdit.getText().toString().matches("")){
                    Toast.makeText(mContext, "Klay값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(kspEdit.getText().toString().matches("")){
                    Toast.makeText(mContext, "Ksp값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferencesManager.setFloat(mContext, "klay", Float.parseFloat(klayEdit.getText().toString()));
                SharedPreferencesManager.setFloat(mContext, "ksp", Float.parseFloat(kspEdit.getText().toString()));
            }
        });

        //초기화
        valueResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesManager.clear(mContext);
                klayEdit.setText("");
                kspEdit.setText("");
                valueRatioTextView.setText("");
                valueTimeTextView.setText("");

            }
        });

        callValueBtn.setOnClickListener(new View.OnClickListener() {//버튼 이벤트 처리
            @Override
            public void onClick(View view) {
                float klayValue = SharedPreferencesManager.getFloat(mContext, "klay");
                float kspValue = SharedPreferencesManager.getFloat(mContext, "ksp");
                if( klayValue == 0 || kspValue == 0){
                    Toast.makeText(mContext, "먼저 정보 저장을 해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                showRatio();
            }
        });
    }

    //api값 받아와 비율계산하기
    public void showRatio(){
        restAPI.getAll().enqueue(new Callback<InOneDataAll>(){
            @Override
            public void onResponse(@NonNull Call<InOneDataAll> call, @NonNull Response<InOneDataAll> response){
                if(response.isSuccessful()){
                    InOneDataAll inOneDataAll = response.body();
                    klayLast = Float.parseFloat(inOneDataAll.getKlayInfo().getLast());
                    kspLast = Float.parseFloat(inOneDataAll.getKspInfo().getLast());

                    float myKlayValue = SharedPreferencesManager.getFloat(mContext, "klay") * klayLast;
                    float myKspValue = SharedPreferencesManager.getFloat(mContext, "ksp") * kspLast;

                    double ratio = 0.0f;
                    if(myKlayValue > myKspValue){
                        ratio = myKlayValue / myKspValue;
                    } else {
                        ratio = myKspValue / myKlayValue;
                    }
                    TextView valueRatioTextView = findViewById(R.id.valueRatioTextView);
                    TextView valueTimeTextView = findViewById(R.id.valueTimeTextView);

                    valueRatioTextView.setText("1 : "+ratio);

                    long timestamp = Long.parseLong(inOneDataAll.getTimestamp()) * 1000L;
                    valueTimeTextView.setText("갱신 시간 : "+getDate(timestamp));
                }
            }
            @Override
            public void onFailure(Call<InOneDataAll> call, Throwable t){
                t.printStackTrace();
            }
        });
    }

    // timeStamp -> Date
    private String getDate(long timeStamp){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "시간 가져오기 실패";
        }
    }

    //api에서 값 가져와서 저장
    public void callAllValue(){
        restAPI.getAll().enqueue(new Callback<InOneDataAll>(){
            @Override
            public void onResponse(@NonNull Call<InOneDataAll> call, @NonNull Response<InOneDataAll> response){
                if(response.isSuccessful()){
                    InOneDataAll inOneDataAll = response.body();
                    klayLast = Float.parseFloat(inOneDataAll.getKlayInfo().getLast());
                    kspLast = Float.parseFloat(inOneDataAll.getKspInfo().getLast());
                    showRatio();
                }
            }
            @Override
            public void onFailure(Call<InOneDataAll> call, Throwable t){
                t.printStackTrace();
            }
        });
    }

    //알림등록
    /*public void showAlarm(){

        EditText klayEdit = findViewById(R.id.klayEdit);
        EditText kspEdit = findViewById(R.id.kspEdit);

        float myKlayCount = Float.parseFloat(klayEdit.getText().toString());
        float myKspCount = Float.parseFloat(kspEdit.getText().toString());

        float myKlayValue = myKlayCount * klayLast;
        float myKspValue = myKspCount * kspLast;

        float ratio = 0.0f;
        if(myKlayValue > myKspValue){
            ratio = myKlayValue / myKspValue;
        } else {
            ratio = myKspValue / myKlayValue;
        }

        System.out.println("myKlayValue : " +myKlayValue + ", myKspValue :" + myKspValue);
        System.out.println("KlayLast : " + klayLast + ", KspLast : " + kspLast);

        System.out.println(ratio);
        System.out.println(ratio);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);

        builder.setContentTitle("비영구적손실");
        builder.setContentText("1 : " + Float.toString(ratio));

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);

        builder.setColor(Color.YELLOW);

        //벨소리
        *//*Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);*//*

        //진동
        *//*long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);*//*

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        manager.notify(1, builder.build());
    }*/

    /*private static void resetAlarm(Context context){
        AlarmManager resetAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(context, logic.class);
        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, 0);

        *//*resetAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                AlarmManager.INTERVAL_HOUR, resetSender);*//*

        resetAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 10 * 1000,
                10 * 1000, resetSender);

        System.out.println("되냐!!!!!");
    }*/

    public void callKlayValue(){
        restAPI.getKlay().enqueue(new Callback<InOneData>(){
            @Override
            public void onResponse(@NonNull Call<InOneData> call, @NonNull Response<InOneData> response){
                if(response.isSuccessful()){
                    InOneData data = response.body();
                }
            }
            @Override
            public void onFailure(Call<InOneData> call, Throwable t){
                t.printStackTrace();
            }
        });
    }

    public void callKspValue(){
        restAPI.getKsp().enqueue(new Callback<InOneData>(){
            @Override
            public void onResponse(@NonNull Call<InOneData> call, @NonNull Response<InOneData> response){
                if(response.isSuccessful()){
                    InOneData data = response.body();
                }
            }
            @Override
            public void onFailure(Call<InOneData> call, Throwable t){
                t.printStackTrace();
            }
        });
    }
}