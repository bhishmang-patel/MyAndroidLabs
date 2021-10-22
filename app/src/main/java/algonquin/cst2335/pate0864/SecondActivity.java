package algonquin.cst2335.pate0864;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SecondActivity extends AppCompatActivity {

    ImageView profileImage;
    ActivityResultLauncher <Intent> cameraResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override

                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        File whereIam = getFilesDir();
                        Bitmap thumbnail = data.getParcelableExtra("data");

                        try {

                            FileOutputStream fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        catch(IOException ioe){
                            Log.w("IOException", "Can't get PNG");
                        }

                        Log.i("Got bitmap", "image");
                        profileImage.setImageBitmap( thumbnail );
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED)
                        Log.w("Got bitmap", "User refused the image");

                }

            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        int age = fromPrevious.getIntExtra("Age", 0);
        String name = fromPrevious.getStringExtra("Name");
        TextView wlc_msg = findViewById(R.id.textView3);
        wlc_msg.setText("Welcome Back" + " " + emailAddress);
        String pCode = fromPrevious.getStringExtra("PostalCode");

        //Shared Preferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        prefs.getString("LoginName", emailAddress);
        prefs.getInt("Age", age);
        prefs.getString("Name", name);
        prefs.getString("Postalcode", pCode);
        String edit_Text = prefs.getString("LoginName", "");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LoginName", emailAddress);
        editor.putInt("Age", age);
        editor.putString("Name", name);
        editor.putString("PostalCode", pCode);
        editor.apply();

        // Click listener method
        EditText number = findViewById(R.id.editTextPhone);
        Button dialer = findViewById(R.id.button2);

        dialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:"+number.getText().toString()));
                startActivity(call);
            }
        });

        ImageView img = findViewById(R.id.imageView);
        img.setOnClickListener(clk -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);
        });
        Button change_pic = findViewById(R.id.button);
        change_pic.setOnClickListener(clk -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);
        });
    }
}