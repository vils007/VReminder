package ru.vostokeko.vremind.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import ru.vostokeko.vremind.model.ModelTask;

/**
 * Created by ilya on 05.10.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "reminder_database";

    public static final String TASKS_TABLE = "tasks_table";

    public static final String TASK_TITLE_COLUMN = "tasks_title";
    public static final String TASK_DATE_COLUMN = "tasks_date";
    public static final String TASK_PRIORITY_COLUMN = "tasks_priority";
    public static final String TASK_STATUS_COLUMN = "tasks_status";
    public static final String TASK_TIME_STAMP_COLUMN = "tasks_time_stamp";

    private static final String TASKS_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            +  TASKS_TABLE + " (" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK_TITLE_COLUMN + " TEXT NOT NULL, "
            + TASK_DATE_COLUMN + " LONG, " + TASK_PRIORITY_COLUMN + " INTEGER, "
            + TASK_STATUS_COLUMN + " INTEGER, " + TASK_TIME_STAMP_COLUMN + " LONG);";

    // маленькая БД для маленькой програмки. )))

    public static final String SELECTION_STATUS = DBHelper.TASK_STATUS_COLUMN + " = ?";
    public static final String SELECTION_TIME_STAMP = TASK_TIME_STAMP_COLUMN + " = ?";
    public static final String SELECTION_LIKE_TITLE = TASK_TITLE_COLUMN + " LIKE ?";

    private DBQueryManager queryManager;
    private DBUpdateManager updateManager;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        queryManager = new DBQueryManager(getReadableDatabase());
        updateManager = new DBUpdateManager(getWritableDatabase());

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + TASKS_TABLE);
        onCreate(db);
    }


    public void saveTask(ModelTask task) {
        ContentValues newValues = new ContentValues();

        newValues.put(TASK_TITLE_COLUMN, task.getTitle());
        newValues.put(TASK_DATE_COLUMN, task.getDate());
        newValues.put(TASK_STATUS_COLUMN, task.getStatus());
        newValues.put(TASK_PRIORITY_COLUMN, task.getPriority());
        newValues.put(TASK_TIME_STAMP_COLUMN, task.getTimeStamp());

        getWritableDatabase().insert(TASKS_TABLE, null, newValues);

    }

    public DBQueryManager query() {
        return queryManager;
    }

    public DBUpdateManager update() {
        return  updateManager;
    }

    public void removeTask (long timeStamp) {
        getWritableDatabase().delete(TASKS_TABLE, SELECTION_TIME_STAMP, new String[]{Long.toString(timeStamp)});
    }
}
