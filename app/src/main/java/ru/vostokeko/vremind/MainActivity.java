package ru.vostokeko.vremind;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.vostokeko.vremind.adapter.TabAdapter;
import ru.vostokeko.vremind.alarm.AlarmHelper;
import ru.vostokeko.vremind.database.DBHelper;
import ru.vostokeko.vremind.dialog.AddingTaskDialogFragment;
import ru.vostokeko.vremind.dialog.EditTaskDialogFragment;
import ru.vostokeko.vremind.fragment.CurrentTaskFragment;
import ru.vostokeko.vremind.fragment.DoneTaskFragment;
import ru.vostokeko.vremind.fragment.SplashFragment;
import ru.vostokeko.vremind.fragment.TaskFragment;
import ru.vostokeko.vremind.model.ModelTask;

public class MainActivity extends AppCompatActivity
        implements AddingTaskDialogFragment.AddingTaskListener,
        CurrentTaskFragment.OnTaskDoneListener , DoneTaskFragment.OnTaskRestoreListener,
        EditTaskDialogFragment.EditingTaskListener {

    FragmentManager fragmentManager;

    PreferenceHelper preferenceHelper;
    TabAdapter tabAdapter;
    TaskFragment currentTaskFragment;
    TaskFragment doneTaskFragment;

    SearchView searchView;
    public DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();

        AlarmHelper.getInstance().init(getApplicationContext());

        dbHelper = new DBHelper(getApplicationContext());

        fragmentManager = getFragmentManager();
        runSplash();

        setUI(); // запускаем при старте приложения

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyAplication.activityResumed();

    }

    @Override
    protected void onPause() {
        super.onPause();
    MyAplication.activityPaused();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem splashItem = menu.findItem(R.id.action_splash);
        splashItem.setChecked(preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_splash) {
            item.setChecked(!item.isChecked());
            preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE,item.isChecked());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void  runSplash() {
        if (!preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)){



        SplashFragment splashFragment = new SplashFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, splashFragment)
                .addToBackStack(null)
                .commit();
        }

    }
// устанавливаем тулбар
    private void setUI() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));// цвет текста заголовка
             setSupportActionBar(toolbar);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task)); // то магия, иди в стрингс хмл
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TabAdapter(fragmentManager, 2);      // 2 - кол-во вкладок

        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { // выбран
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { //уже не выбран

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { // выбран ранее выбраный

            }


        });

        currentTaskFragment = (CurrentTaskFragment) tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        doneTaskFragment = (DoneTaskFragment) tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentTaskFragment.findTasks(newText);
                doneTaskFragment.findTasks(newText);
                return false;
            }
        });

        // магия с кнопкой
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
                addingTaskDialogFragment.show(fragmentManager, "AddingTaskDialogFragment");
            }
        });
    }


    @Override
    public void onTaskAdded(ModelTask newTask) {
        currentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddedCancel() {
        Toast.makeText(this, "Task adding cancel", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskDone(ModelTask task) {
        doneTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        currentTaskFragment.addTask(task, false);

    }

    @Override
    public void onTaskEdited(ModelTask updatedTask) {
        currentTaskFragment.updateTask(updatedTask);
        dbHelper.update().task(updatedTask);
    }
}
