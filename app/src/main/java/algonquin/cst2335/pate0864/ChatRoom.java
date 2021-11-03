package algonquin.cst2335.pate0864;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    Button send_btn, receive_btn;
    EditText editText;
    RecyclerView chatList;
    MyChatAdapter adt = new MyChatAdapter();
    TextView view, t_view;
    ArrayList <ChatMessage> messages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.chatlayout );
        chatList = findViewById(R.id.myRecycler);
        chatList.setAdapter( new MyChatAdapter() );
        editText = findViewById(R.id.editText);
        view = findViewById(R.id.message);
        t_view = findViewById(R.id.time);

        // When Send button is clicked
        send_btn = findViewById(R.id.button1);
        send_btn.setOnClickListener(Click -> {
            String messageTyped = editText.getText().toString();
            int SEND = 0;
            Date time = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());

            // convert the Date into String
            String currentDateandTime = sdf.format(time);

            ChatMessage thisMessage = new ChatMessage(messageTyped, SEND, currentDateandTime);
            // adds the line in a row
            messages.add(thisMessage);

            // clear the text
            editText.setText("");

            // Notify
            adt.notifyItemInserted(messages.size() - 1);
        });

        // When Receive button is clicked
        receive_btn = findViewById(R.id.button2);
        receive_btn.setOnClickListener(Click -> {
            String messageTyped = editText.getText().toString();
            int RECEIVE = 1;
            Date time = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());

            // convert the Date into String
            String currentDateandTime = sdf.format(time);

            ChatMessage thisMessage = new ChatMessage(messageTyped, RECEIVE, currentDateandTime);

            // adds the line in a row
            messages.add(thisMessage);

            // clear the text
            editText.setText("");

            // Notify
            adt.notifyItemInserted(messages.size() - 1);

        });

        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(this));

    }
    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews>{
        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            int LayoutID;
            if (viewType == 1)
                LayoutID = R.layout.sent_message;
            else
                LayoutID = R.layout.receive_message;

            View loadedRow = inflater.inflate(LayoutID, parent, false);

            MyRowViews initRow = new MyRowViews(loadedRow);
            return new MyRowViews(loadedRow);
        }


        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {

            holder.messageText.setText(messages.get(position).getMessage());
            holder.timeText.setText(messages.get(position).getTimeSent());
        }
        public int getItemViewType(int position){
            messages.get(position).getSendOrReceive();
            return position;
        }
        @Override
        public int getItemCount() {
            return messages.size();
        }
    }
    private class MyRowViews extends RecyclerView.ViewHolder{
        TextView messageText;
        TextView timeText;

        public MyRowViews(View itemView) {
            super(itemView);

            itemView.setOnClickListener( click ->{

                int position = getAbsoluteAdapterPosition();

                MyRowViews newRow = adt.onCreateViewHolder(null, adt.getItemViewType(position));

                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message:" + messageText.getText())
                       .setTitle("Question:")
                       .setNegativeButton("No",(dialog, cl) -> {})
                       .setPositiveButton("Yes",(dialog, cl) -> {
                        ChatMessage removedMessage = messages.get(position);
                        messages.remove(position);
                        adt.notifyItemRemoved(position);

                           Snackbar.make(messageText, "You deleted message #"+ position, Snackbar.LENGTH_LONG)
                                   .setAction("Undo", clk -> {

                                       messages.add(position, removedMessage);
                                       adt.notifyItemInserted(position);
                                   })
                                   .show();
                }).create().show();
            });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
    public class ChatMessage{
        String message;
        int sendOrReceive;
        String timeSent;


        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public String getTimeSent() {
            return timeSent;
        }
    }
}
