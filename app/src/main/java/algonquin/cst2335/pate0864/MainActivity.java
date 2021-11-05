package algonquin.cst2335.pate0864;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** Description: This is a simple login page application which checks the that the user input meets the requirements
 * for the password
 * @author Bhishmang Patel
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    TextView tv = null;
    EditText et = null;
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        /**
         * Using OnClickListener function for the login button that when user clicks the button then what it should do
         */
        btn.setOnClickListener( clk ->{
            String password = et.getText().toString();

            if(checkPasswordComplexity( password )){
                tv.setText("Your password is complex enough!");
                Toast.makeText(getApplicationContext(), "Congrats! Your password is set successfully", Toast.LENGTH_SHORT).show();
            }
            else
                tv.setText("You shall not pass!");
        });
    }

    /** This function checks if this string contains any uppercase letter, lowercase letter and any special symbol or not.
     *
     * @param pw The String that we are checking
     * @return true if the password is strong enough else it will return false.
     */
    private boolean checkPasswordComplexity( String pw ){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        /**
         * Using for loop checks the condition one by one
         */

        for (int i = 0; i<pw.length(); i++){
            char c = pw.charAt(i);
            Log.i("Looking at char:", ""+c);

            /**
             * Using elseif statement to check that the characters meets the condition or not
             */

            if  ( Character.isDigit(c))
                foundNumber = true;
             else if  (Character.isUpperCase(c))
                foundUpperCase = true;
            else if (Character.isLowerCase(c))
                foundLowerCase = true;
            else if (isSpecialCharacter(c))
                foundSpecial = true;
        }

        /**
         * Again using elseif statement the if the upper elseif condition doesn't work then it shows the popup message
         */

        if( ! foundNumber)
        {
            Toast.makeText(getApplicationContext(), "You are missing digits in your password" , Toast.LENGTH_SHORT).show();
        }

        else if( ! foundUpperCase)
        {
            Toast.makeText(getApplicationContext(), "You are missing upper case letter in your password" , Toast.LENGTH_SHORT).show();
            return false;
        }

        else if( ! foundLowerCase)
        {
            Toast.makeText(getApplicationContext(), "You are missing lower case in your password" , Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(! foundSpecial)
        {
            Toast.makeText(getApplicationContext(), "You are missing special characters in your password" , Toast.LENGTH_SHORT).show();
            return false;
        }

        else
            return true;

    return foundNumber && foundUpperCase && foundLowerCase && foundSpecial;
    }
    /**
     *
     * @param c
     * @return   It returns true if the character matches one of the special characters #$%^&*!@?
     * else it returns false
     */
    private boolean isSpecialCharacter(char c)
    {
        switch (c)
        {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
}