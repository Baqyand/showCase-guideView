package com.nexsoft.showcaseviewlib;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * Created by NEXSOFT on 03/05/2018.
 */

public class ActivityHelper {

    public final static void showActivity(final Activity activityFrom, final Class<?> activityTo,boolean activityFromFinish,String key, String putExtra) {
        Intent i = new Intent(activityFrom, activityTo);
        i.putExtra(key, putExtra);
        activityFrom.startActivity(i);
        activityFrom.overridePendingTransition(0, 0);
        if(activityFromFinish){
            activityFrom.finish();
        }

    }

    public final static void showActivity(final Activity activityFrom, final String link,boolean activityFromFinish) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        activityFrom.startActivity(i);
        activityFrom.overridePendingTransition(0, 0);
        if(activityFromFinish){
            activityFrom.finish();
        }

    }

    public final static void showActivity(final Activity activityFrom, final Class<?> activityTo,boolean activityFromFinish) {
        Intent i = new Intent(activityFrom, activityTo);
        activityFrom.startActivity(i);
        activityFrom.overridePendingTransition(0, 0);
        if(activityFromFinish){
            activityFrom.finish();
        }

    }

    public final static void showActivity(final Activity activityFrom, final Class<?> activityTo, final int animStart, final int animEnd,boolean activityFromFinish) {
        Intent i = new Intent(activityFrom, activityTo);
        activityFrom.startActivity(i);
        activityFrom.overridePendingTransition(animStart, animEnd);
        if(activityFromFinish){
            activityFrom.finish();
        }
    }
    public final static void showActivityCloseMenu(final Activity activityFrom, final Class<?> activityTo,boolean activityFromFinish, boolean closeMenu ) {
        Intent i = new Intent(activityFrom, activityTo);
        activityFrom.startActivity(i);
        if (activityFromFinish){
            activityFrom.finish();
        }
        if(closeMenu){
//            NavigationDrawerFragment.closeMenu();
        }
    }


    public final static void callService(final Activity activityFrom, final Class<?> serviceClass) {
        Intent intent = new Intent(activityFrom,serviceClass);
        activityFrom.startService(intent);
    }


    public final static void showFragment(final FragmentActivity fragmentActivity, final Fragment fragmentTo) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction;
                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                //TransactionFragment trans = new TransactionFragment();
//                fragmentTransaction.replace(R.id.fragment, fragmentTo);
//                fragmentTransaction.commit();
            }
        }, 200);
    }

}


