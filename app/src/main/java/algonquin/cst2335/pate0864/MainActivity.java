package algonquin.cst2335.pate0864;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {

    private String stringURL;
    Bitmap image = null;
    float oldSize = 14;
    TextView tv;
    TextView currentTemp;
    TextView maxTemp;
    TextView minTemp;
    TextView humidity;
    TextView description;
    ImageView icon;
    EditText cityTextField;
    Button forecastBtn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forecastBtn= findViewById(R.id.forecastButton);
        cityTextField = findViewById(R.id.cityTextField);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Drawer Layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation View
        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item) -> {

           onOptionsItemSelected(item);
           drawer.closeDrawer(GravityCompat.START);
                return false;
        });

        currentTemp = findViewById(R.id.currentTemp);
        maxTemp = findViewById(R.id.maxTemp);
        minTemp = findViewById(R.id.minTemp);
        humidity = findViewById(R.id.humidity);
        description = findViewById(R.id.description);
        icon = findViewById(R.id.icon);

            forecastBtn.setOnClickListener(click -> {

                String cityName = cityTextField.getText().toString();
                myToolbar.getMenu().add(0, 5, 0, cityName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                runForecast(cityName);
            });
    }

    private void runForecast(String cityName){
                Executor newThread = Executors.newSingleThreadExecutor();
                newThread.execute(() -> {
                    try {

                        stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                                + URLEncoder.encode(cityName, "UTF-8")
                                + "&appid=47565c755963e5a4aeba20ce90f3c0e9&units=metric";

                        URL url = new URL(stringURL);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                        String text = (new BufferedReader(
                                new InputStreamReader(in, StandardCharsets.UTF_8)))
                                .lines()
                                .collect(Collectors.joining("\n"));

                        JSONObject theDocument = new JSONObject(text);

                        JSONArray weatherArray = theDocument.getJSONArray("weather");
                        JSONObject position0 = weatherArray.getJSONObject(0);

                        String description = position0.getString("description");
                        String iconName = position0.getString("icon");

                        JSONObject mainObject = theDocument.getJSONObject("main");
                        double current = mainObject.getDouble("temp");
                        double min = mainObject.getDouble("temp_min");
                        double max = mainObject.getDouble("temp_max");
                        int humidity = mainObject.getInt("humidity");


                        File file = new File(getFilesDir(), iconName + ".png");
                        if (file.exists()) {
                            image = BitmapFactory.decodeFile(getFilesDir() + "/" + iconName + ".png");
                        } else {
                            URL imgURL = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                            HttpURLConnection connection = (HttpURLConnection) imgURL.openConnection();
                            connection.connect();
                            int responseCode = connection.getResponseCode();
                            if (responseCode == 200) {
                                image = BitmapFactory.decodeStream(connection.getInputStream());
                                image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                            }
                        }


                        runOnUiThread(() -> {
                            tv = findViewById(R.id.currentTemp);
                            tv.setText("The current temperature is " + current);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.minTemp);
                            tv.setText("The min temperature is " + current);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.maxTemp);
                            tv.setText("The max temperature is " + current);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.humidity);
                            tv.setText("The humidity is " + humidity + "%");
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.description);
                            tv.setText(description);
                            tv.setVisibility(View.VISIBLE);

                            ImageView iv = findViewById(R.id.icon);
                            iv.setImageBitmap(image);
                            iv.setVisibility(View.VISIBLE);
                        });


                    } catch (IOException | JSONException ioe) {
                        Log.e("Connection error:", ioe.getMessage());
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case 5:
                String cityName = item.getTitle().toString();
                runForecast(cityName);
                break;

            case R.id.hide_views:
                currentTemp.setVisibility(View.INVISIBLE);
                maxTemp.setVisibility(View.INVISIBLE);
                minTemp.setVisibility(View.INVISIBLE);
                humidity.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);
                icon.setVisibility(View.INVISIBLE);
                cityTextField.setText("");
                break;

            case R.id.id_increase:
                oldSize++;
                currentTemp.setTextSize(oldSize);
                minTemp.setTextSize(oldSize);
                maxTemp.setTextSize(oldSize);
                humidity.setTextSize(oldSize);
                description.setTextSize(oldSize);
                cityTextField.setTextSize(oldSize);
                break;

            case R.id.id_decrease:
                oldSize = Float.max(oldSize-1, 5);
                currentTemp.setTextSize(oldSize);
                minTemp.setTextSize(oldSize);
                maxTemp.setTextSize(oldSize);
                humidity.setTextSize(oldSize);
                description.setTextSize(oldSize);
                cityTextField.setTextSize(oldSize);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}