package ru.vostokeko.vremind.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import ru.vostokeko.vremind.fragment.CurrentTaskFragment;
import ru.vostokeko.vremind.fragment.DoneTaskFragment;

/**
 * Created by ilya on 27.09.2015.
 */
public class TabAdapter extends FragmentStatePagerAdapter{

    private int numberOfTabs;
    public static final int CURRENT_TASK_FRAGMENT_POSITION = 0;
    public static final int DONE_TASK_FRAGMENT_POSITION = 1;

    private CurrentTaskFragment currentTaskFragment;
    private DoneTaskFragment doneTaskFragment;


    public TabAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs; // управляет вкладками
        currentTaskFragment = new CurrentTaskFragment();
        doneTaskFragment = new DoneTaskFragment();
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return currentTaskFragment;
            case 1:
                return doneTaskFragment;
            default:
                return null;
        }
    }


// этим методом будем возвращать -- запомнить!!!
    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
