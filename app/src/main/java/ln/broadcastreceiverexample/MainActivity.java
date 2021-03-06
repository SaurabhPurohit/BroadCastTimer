package ln.broadcastreceiverexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    TextView textView, textContact;
    Intent intent, intent1, intentFetch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        textContact = (TextView) findViewById(R.id.txt_contact);
        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                intent.setClass(MainActivity.this, MyService.class);
                intent.setAction(MyService.ACTION_START);
                startService(intent);
            }
        });
        findViewById(R.id.button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent1 = new Intent();
                intent1.setClass(MainActivity.this, MyService.class);
                intent1.setAction(MyService.ACTION_STOP);
                startService(intent1);
            }
        });

        findViewById(R.id.btn_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentFetch = new Intent();
                intentFetch.setClass(MainActivity.this, MyService.class);
                intentFetch.setAction(MyService.ACTION_FETCH);
                startService(intentFetch);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceivers();
    }

    private void registerReceivers() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String value = intent.getStringExtra(MyService.EXTRA_VALUE);
                textView.setText(value);

                String contacts = intent.getStringExtra(MyService.EXTRA_FETCH);
                textContact.setText(contacts);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.ACTION_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);

        IntentFilter intentFilterFetch = new IntentFilter();
        intentFilterFetch.addAction(MyService.ACTION_FETCH);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterReceivers();
        stopService(intent);
        stopService(intent1);
        stopService(intentFetch);
    }

    private void unRegisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
