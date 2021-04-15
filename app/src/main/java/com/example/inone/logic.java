package com.example.inone;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.inone.network.RestAPI;
import com.example.inone.prefs.SharedPreferencesManager;
import com.example.inone.vo.InOneDataAll;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.NOTIFICATION_SERVICE;

public class logic extends BroadcastReceiver {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.coinone.co.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RestAPI restAPI = retrofit.create(RestAPI.class);

    float kspLast = 0.0f;
    float klayLast = 0.0f;

    @Override
    public void onReceive(Context context, Intent intent) {
        restAPI.getAll().enqueue(new Callback<InOneDataAll>(){
            @Override
            public void onResponse(@NonNull Call<InOneDataAll> call, @NonNull Response<InOneDataAll> response){
                if(response.isSuccessful()){
                    InOneDataAll inOneDataAll = response.body();
                    klayLast = Float.parseFloat(inOneDataAll.getKlayInfo().getLast());
                    kspLast = Float.parseFloat(inOneDataAll.getKspInfo().getLast());



                    float myKlayCount = SharedPreferencesManager.getFloat(null, "klay");//Float.parseFloat(klayEdit.getText().toString());
                    float myKspCount = SharedPreferencesManager.getFloat(null, "ksp");//Float.parseFloat(kspEdit.getText().toString());

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
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

                    builder.setSmallIcon(R.mipmap.ic_launcher);

                    builder.setContentTitle("비영구적손실");
                    builder.setContentText("1 : " + Float.toString(ratio));

                    //Intent intent = new Intent(context, logic.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(pendingIntent);

                    Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                    builder.setLargeIcon(largeIcon);

                    builder.setColor(Color.YELLOW);

                    //벨소리
                    /*Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(ringtoneUri);*/

                    //진동
                    /*long[] vibrate = {0, 100, 200, 300};
                    builder.setVibrate(vibrate);
                    builder.setAutoCancel(true);*/

                    NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        manager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                    }

                    manager.notify(1, builder.build());
                }
            }
            @Override
            public void onFailure(Call<InOneDataAll> call, Throwable t){
                t.printStackTrace();
            }
        });

    }

}
