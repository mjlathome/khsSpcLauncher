package com.khs.spclauncher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {

    private EditText password;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // extract views
        password = (EditText) findViewById(R.id.txtPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    // perform the password check
    public void doCheckPassword(View v) {

        // gather password
        String input = password.getText().toString();
        input = input.trim();

        // check password matches
        if (input.equals(HomeActivity.PASSWORD)) {
            // return success
            Intent result = new Intent();
            setResult(RESULT_OK, result);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.password_incorrect), Toast.LENGTH_LONG).show();
        }
    }
}
