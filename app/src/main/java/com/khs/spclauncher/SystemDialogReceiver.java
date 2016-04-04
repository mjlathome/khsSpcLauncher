package com.khs.spclauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SystemDialogReceiver extends BroadcastReceiver {

    // constants
    private static final String TAG = "SystemDialogReceiver";
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

    public SystemDialogReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.d(TAG, "onReceive");
        if(intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
            String dialogType = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            Log.d(TAG, "dialogType = " + dialogType);
            if(dialogType != null && dialogType.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)){
                Log.d(TAG, "sendBroadcast");
                Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.sendBroadcast(closeDialog);

            }
        }
    }
}
