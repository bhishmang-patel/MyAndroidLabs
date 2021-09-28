package algonquin.cst2335.pate0864;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView Object
        TextView topView = findViewById(R.id.hellotext);

        // EditText Object
        EditText myedit = findViewById(R.id.myedittext);

        // CheckBox Object
        CheckBox cb = findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener( ( b, c) ->{
          Toast.makeText(getApplicationContext(), "You Clicked on the Checkbox" , Toast.LENGTH_LONG).show();
        });

        // Switch Object
        Switch sw = findViewById(R.id.switch5);
        sw.setOnCheckedChangeListener( ( b, c) ->{
            Toast.makeText(getApplicationContext(), "You Clicked on the Switch" , Toast.LENGTH_SHORT).show();
        });


        // RadioButton Object
        RadioButton rb = findViewById(R.id.radioButton);
        rb.setOnCheckedChangeListener( ( b, c) ->{
            Toast.makeText(getApplicationContext(), "You selected the RadioButton" , Toast.LENGTH_SHORT).show();
        });


        // Button Object
        Button btn = findViewById(R.id.mybutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topView.setText("Your edit text has : " + myedit.getText());
            }
        });

        // Image object
        ImageView myimage = findViewById(R.id.logo);
        myimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ImageButton imgbtn = findViewById( R.id.myimagebutton );
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int width = imgbtn.getWidth();
                int height = view.getHeight();
                Toast.makeText(getApplicationContext(), "The width = " + width + " and height = " + height, Toast.LENGTH_LONG).show();
            }
        });
    }
}