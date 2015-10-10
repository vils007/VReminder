package ru.vostokeko.vremind.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import ru.vostokeko.vremind.model.ModelTask;

/**
 * Created by ilya on 07.10.2015.
 */
public class AlarmHelper {

    // синглтон из ПП

    private static AlarmHelper instance;
    private Context context;
    private AlarmManager alarmManager;

    public static AlarmHelper getInstance() {
        if (instance == null) {
            instance = new AlarmHelper();
        }
        return instance;
        }

    public void init(Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);


    }
    public void setAlarm(ModelTask task) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", task.getTitle());
        intent.putExtra("time_stamp", task.getTimeStamp());
        intent.putExtra("color", task.getPriorityColor());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                (int) task.getTimeStamp(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //флаг указывает что если пендинг интент существует то новый не будет создаваться,
        // а будет использоваться текущий, но с обнавленными данными

        alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDate(), pendingIntent);

    }

    public void removeAlarm(long taskTimeStamp) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,(int) taskTimeStamp,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent); // отменяем оповещение

    }

}
