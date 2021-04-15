package com.example.inone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.inone.network.RestAPI;
import com.example.inone.prefs.SharedPreferencesManager;
import com.example.inone.vo.InOneData;
import com.example.inone.vo.InOneDataAll;

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

    private static void resetAlarm(Context context){
        AlarmManager resetAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(context, logic.class);
        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, 0);

        /*resetAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                AlarmManager.INTERVAL_HOUR, resetSender);*/

        resetAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 10 * 1000,
                10 * 1000, resetSender);

        System.out.println("되냐!!!!!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resetAlarm(this);



        Button valueBtn = (Button) findViewById(R.id.callValueBtn);
        valueBtn.setOnClickListener(new View.OnClickListener() {//버튼 이벤트 처리
            @Override
            public void onClick(View view) {
                EditText klayEdit = findViewById(R.id.klayEdit);
                EditText kspEdit = findViewById(R.id.kspEdit);
                SharedPreferencesManager.setFloat(null, "klay",Float.parseFloat(klayEdit.getText().toString()));
                SharedPreferencesManager.setFloat(null, "ksp", Float.parseFloat(kspEdit.getText().toString()));
                //callAllValue();
            }
        });
    }

    public void callAllValue(){
        restAPI.getAll().enqueue(new Callback<InOneDataAll>(){
            @Override
            public void onResponse(@NonNull Call<InOneDataAll> call, @NonNull Response<InOneDataAll> response){
                if(response.isSuccessful()){
                    InOneDataAll inOneDataAll = response.body();
                    klayLast = Float.parseFloat(inOneDataAll.getKlayInfo().getLast());
                    kspLast = Float.parseFloat(inOneDataAll.getKspInfo().getLast());
                    showAlarm();
                }
            }
            @Override
            public void onFailure(Call<InOneDataAll> call, Throwable t){
                t.printStackTrace();
            }
        });
    }

    public void showAlarm(){

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
        /*Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);*/

        //진동
        /*long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);*/

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        manager.notify(1, builder.build());
    }

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