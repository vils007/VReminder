package ru.vostokeko.vremind.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ru.vostokeko.vremind.MainActivity;
import ru.vostokeko.vremind.MyAplication;
import ru.vostokeko.vremind.R;

/**
 * Created by ilya on 07.10.2015.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        long timeStamp = intent.getLongExtra("title_stamp", 0);
        int color = intent.getIntExtra("color", 0);

        Intent resultIntent = new Intent(context,MainActivity.class);

        if (MyAplication.isActivityVisible()) {
            resultIntent = intent;
            // если активити видимо то алярм ресивир будет использовать поступивший интент на входе
            // и не будет пересоздавать активити
            // а если приложение закрыто то будет создавать ноое активити
        }
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) timeStamp,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // тут передем данные сервесу для реализации оповещения

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Vremind");
        builder.setContentText(title);

        builder.setColor(context.getResources().getColor(color));
        builder.setSmallIcon(R.drawable.ic_check_white_48dp);

        builder.setDefaults(Notification.DEFAULT_ALL); // указывает какие будут унаследованны по умолчанию
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // с этим флагом ведомление будет отменяться при нажании пользователем

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // этот класс и отвечает за уведомление пользователя
         //
        notificationManager.notify((int) timeStamp, notification);
        // идентификатор тайм стамп и само уведомление

    }
}
