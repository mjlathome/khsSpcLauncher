package com.khs.spclauncher;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Mark on 3/29/2016.
 */
public class HomeActivity extends Activity {
    private static final String TAG = "HomeActivity";

    private static final String SPC_PACKAGE = "com.khs.spcmeasure";
    protected static final String PASSWORD = "@m@zon01";

    private static final int REQUEST_CODE_PSWD = 1;
    private static final int EXIT_COUNT = 10;

    private PackageManager manager;
    private CustomViewGroup mStatusBarView;

    private Intent mSpcIntent;
    private int mExitCLick = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    // private GoogleApiClient client;

    // stop recent task/task manager overlay
    // seesee
    private Handler windowCloseHandler = new Handler();
    private Runnable windowCloserRunnable = new Runnable() {
        @Override
        public void run() {
            ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

            Toast.makeText(getApplicationContext(), cn.getClassName(), Toast.LENGTH_LONG).show();
            if (cn != null && cn.getClassName().equals("com.android.systemui.recent.RecentsActivity")) {
                toggleRecents();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blockStatusBarPullDown();
        setContentView(R.layout.activity_home);

        // build intent for spc measure
        manager = getPackageManager();
        mSpcIntent = manager.getLaunchIntentForPackage(SPC_PACKAGE);
        if (mSpcIntent == null) {
            finish();
        } else {
            try {
                // get spc measure app info
                ApplicationInfo appInfo = manager.getApplicationInfo(SPC_PACKAGE, 0);

                // draw app icon
                ImageView appIcon = (ImageView) findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(manager.getApplicationIcon(SPC_PACKAGE));

                // display app label
                TextView appLabel = (TextView) findViewById(R.id.item_app_label);
                appLabel.setText(manager.getApplicationLabel(appInfo));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    // stop back button from functioning
    @Override
    public void onBackPressed() {
        // i.e. nothing
        // super.onBackPressed();
    }

    // stop recent task/task manager overlay
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.d(TAG, "Focus changed = " + hasFocus);

        if (!hasFocus) {
            // windowCloseHandler.postDelayed(windowCloserRunnable, 250);
            // Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            // sendBroadcast(closeDialog);
        }
    }

    // handle activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // check password request
            case REQUEST_CODE_PSWD:
                // check password matched
                if (resultCode == RESULT_OK) {
                    clearPreferred();
                    allowStatusBarPullDown();
                    finish();
                }
        }
    }

    // launch spc measure
    public void runSpcMeasure(View v) {
        if (mSpcIntent != null) {
            startActivity(mSpcIntent);
        }
    }

    // exit to main home screen
    public void doExit(View v) {
        mExitCLick++;
        if (mExitCLick == EXIT_COUNT) {
            mExitCLick = 0;
            Intent intent = new Intent(this, PasswordActivity.class);
            startActivityForResult(intent, REQUEST_CODE_PSWD);
        }
    }

    // clear package preferred activities
    private void clearPreferred() {
        manager = getPackageManager();
        manager.clearPackagePreferredActivities(this.getPackageName());
    }

    // block the status bar from being pulled down
    // see:
    // http://stackoverflow.com/questions/25284233/prevent-status-bar-for-appearing-android-modified?answertab=active#tab-top
    // https://gist.github.com/sarme/7e4dc90e2478ade310e6
    private void blockStatusBarPullDown() {

        WindowManager manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (50 * getResources()
                .getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        mStatusBarView = new CustomViewGroup(this);

        manager.addView(mStatusBarView, localLayoutParams);
    }

    // allow the status bar to be pulled down
    private void allowStatusBarPullDown() {

        WindowManager manager = ((WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        if (mStatusBarView != null) {
            manager.removeView(mStatusBarView);
        }
    }

    // stop recent task/task manager overlay
    private void toggleRecents() {
        Intent closeRecents = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
        closeRecents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        ComponentName recents = new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity");
        closeRecents.setComponent(recents);
        this.startActivity(closeRecents);
    }
}
