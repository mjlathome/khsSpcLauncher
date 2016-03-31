package com.khs.spclauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    private Intent mSpcIntent;
    private int mExitCLick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    // stop back button from functioning
    @Override
    public void onBackPressed() {
        // i.e. nothing
        // super.onBackPressed();
    }

    // handle activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            // check password request
            case REQUEST_CODE_PSWD:
                // check password matched
                if (resultCode == RESULT_OK) {
                    clearPreferred();
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

}
