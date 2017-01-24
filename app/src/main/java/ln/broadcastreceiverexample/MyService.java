package ln.broadcastreceiverexample;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private static final String TAG = "MyService";
    public static String ACTION_START = "Start";
    public static String ACTION_STOP = "Stop";
    public static String ACTION_FETCH = "Fetch";
    String contacts = "blank";
    public static String ACTION_BROADCAST = "Broadcast";

    public static String EXTRA_VALUE = "Value";
    public static String EXTRA_FETCH = "FetchValue";

    String test;

    Timer timer;
    TimerTask timerTask;
    int counter = 0;

    public MyService() {
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG,"onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action.equals(ACTION_START)) {
            startTimer();
        } else if (action.equals(ACTION_STOP)) {
            stopTimer();
        }
        if (action.equals(ACTION_FETCH)) {
            contacts = getContacts();
            sendBroadcast(contacts);

        }
        Log.d(TAG,"");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                counter += 1;
                sendBroadcast(String.valueOf(counter));
            }
        };
        timer.schedule(timerTask, 0, 100);
    }

    private void stopTimer() {
        counter = 0;
        timer.cancel();
    }

    private void sendBroadcast(String value) {
        Intent intent = new Intent();
        intent.setAction(ACTION_BROADCAST);
        intent.putExtra(EXTRA_VALUE, value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        Intent intentfetch = new Intent();
        intentfetch.putExtra(EXTRA_FETCH, value);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentfetch);
    }

    private String getContacts() {
        // Run query
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.RawContacts.ACCOUNT_TYPE
        };
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        //MainActivity.textContact.setText(test);
        Cursor cursor = getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            test = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        //return getContentResolver().query(uri, projection, null, selectionArgs, sortOrder).toString();
        return test;
    }


}
