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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MainServiceConnection mMainServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainServiceConnection = new MainServiceConnection(this);
    }


    static class MainServiceConnection implements ServiceConnection {
        Activity mParentActivity;
        MainService mConnectedService;

        MainServiceConnection(Activity act) {
            mParentActivity = act;
            mConnectedService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainService.LocalBinder binder = (MainService.LocalBinder) service;
            mConnectedService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
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
                break;
            default:
                break;
        }
    }
}
