package com.sawaikatyou.androidservicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MainServiceConnection mMainServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMainServiceConnection != null) {
            unbindService(mMainServiceConnection);
            mMainServiceConnection = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        reloadButtonState();
    }

    void reloadButtonState() {
        boolean isbinded = (mMainServiceConnection != null);
        if (isbinded) {
            findViewById(R.id.button_bindservice).setEnabled(false);
            findViewById(R.id.button_executeservice).setEnabled(true);
            findViewById(R.id.button_unbindservice).setEnabled(true);
        } else {
            findViewById(R.id.button_bindservice).setEnabled(true);
            findViewById(R.id.button_executeservice).setEnabled(false);
            findViewById(R.id.button_unbindservice).setEnabled(false);
        }
    }

    static class MainServiceConnection implements ServiceConnection {
        MainActivity mMainActivity;
        MainService mConnectedService;

        MainServiceConnection(MainActivity act) {
            mMainActivity = act;
            mConnectedService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainService.LocalBinder binder = (MainService.LocalBinder) service;
            mConnectedService = binder.getService();
            mMainActivity.reloadButtonState();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMainActivity.reloadButtonState();
            mConnectedService = null;
        }

        public MainService getConnectedService() {
            return mConnectedService;
        }
    }

    public void onClickButton(View view) {
        Intent intent;
        intent = new Intent(getApplication(), MainService.class);
        int id = view.getId();
        switch (id) {
            case R.id.button_syncapicall:
                break;
            case R.id.button_startservice:
                startService(intent);
                break;
            case R.id.button_stopservice:
                stopService(intent);
                break;
            case R.id.button_bindservice:
                if (mMainServiceConnection != null) {
                    unbindService(mMainServiceConnection);
                    mMainServiceConnection = null;
                }
                mMainServiceConnection = new MainServiceConnection(this);
                bindService(intent, mMainServiceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.button_executeservice:
                MainService service = mMainServiceConnection.getConnectedService();
                if (service != null) {
                    Toast.makeText(this, "value=" + service.testmethod(), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.button_unbindservice:
                unbindService(mMainServiceConnection);
                mMainServiceConnection = null;
                reloadButtonState();
                break;
            default:
                break;
        }
    }
}
