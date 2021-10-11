package algonquin.cst2335.pate0864;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.BreakIterator;

public class MainActivity<clk> extends AppCompatActivity {


    private static String TAG = "MainActivity";
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        EditText edit_Text= findViewById(R.id.editTextEmailAddress3);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(clk -> {
            String emailEditText = edit_Text.getText().toString();
            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra( "EmailAddress", emailEditText);
            nextPage.putExtra("Age", 21);
            nextPage.putExtra("Name", "Bhishmang");
            nextPage.putExtra("PostalCode", "K2B7S9");
            startActivity( nextPage);
        });
        }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "Waring Message");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "Waring Message");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.w(TAG, "Waring Message");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "Waring Message");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "Waring Message");
    }
}
