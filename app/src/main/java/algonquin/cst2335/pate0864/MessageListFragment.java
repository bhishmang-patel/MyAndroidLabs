package algonquin.cst2335.pate0864;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageListFragment extends Fragment {

    Button send_btn, receive_btn, send;
    EditText editText;
    RecyclerView chatList;
    MyChatAdapter adt;
    TextView view, t_view;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View chatLayout = inflater.inflate(R.layout.chatlayout, container, false);
        chatList = chatLayout.findViewById(R.id.myRecycler);
        chatList.setAdapter( new MyChatAdapter() );
        editText = chatLayout.findViewById(R.id.editText);
        view = chatLayout.findViewById(R.id.message);
        t_view = chatLayout.findViewById(R.id.time);
        send = chatLayout.findViewById(R.id.button1);


        MyOpenHelper opener = new MyOpenHelper(getContext());
        db = opener.getWritableDatabase();

        // When Send button is clicked
        send_btn = chatLayout.findViewById(R.id.button1);
        send_btn.setOnClickListener(Click -> {
            String messageTyped = editText.getText().toString();
            Date time = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());

            // convert the Date into String
            String currentDateandTime = sdf.format(time);

            ChatMessage thisMessage = new ChatMessage(messageTyped, 0, currentDateandTime);
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());

            Long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);

            // adds the line in a row
            messages.add(thisMessage);

            // clear the text
            editText.setText("");

            // Notify
            adt.notifyItemInserted(messages.size() - 1);
        });

        // When Receive button is clicked
        receive_btn = chatLayout.findViewById(R.id.button2);
        receive_btn.setOnClickListener(Click -> {
            String messageTyped = editText.getText().toString();

            Date time = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());

            // convert the Date into String
            String currentDateandTime = sdf.format(time);

            ChatMessage thisMessage = new ChatMessage(messageTyped, 1, currentDateandTime);
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());

            Long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            thisMessage.setId(newId);


            // adds the line in a row
            messages.add(thisMessage);

            // clear the text
            editText.setText("");

            // Notify
            adt.notifyItemInserted(messages.size() - 1);

        });

        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);
        int _idCol = results.getColumnIndex("_id");
        int _messageCol = results.getColumnIndex(MyOpenHelper.col_message);
        int _sendCol = results.getColumnIndex(MyOpenHelper.col_send_receive);
        int _timeCol = results.getColumnIndex(MyOpenHelper.col_time_sent);

        while(results.moveToNext()) {
            long id = results.getInt(_idCol);
            String message = results.getString(_messageCol);
            String timeSent = results.getString(_timeCol);
            int sendOrReceive = results.getInt(_sendCol);
            messages.add(new ChatMessage(message, sendOrReceive, timeSent, id));
        }
        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        return chatLayout;
    }

    public void notifyMessageDeleted(ChatMessage chosenMessage, int chosenPosition) {

                AlertDialog.Builder builder = new AlertDialog.Builder( getContext());
                builder.setMessage("Are you sure you want to delete the message:" + chosenMessage.getMessage())
                        .setTitle("Question:")
                        .setNegativeButton("Cancel",(dialog, cl) -> {})
                        .setPositiveButton("Delete",(dialog, cl) -> {
                            ChatMessage removedMessage = messages.get(chosenPosition);
                            messages.remove(chosenPosition);
                            adt.notifyItemRemoved(chosenPosition);

                            db.delete(MyOpenHelper.TABLE_NAME, " _id = ?", new String[]{
                                    Long.toString(removedMessage.getId())});

                            Snackbar.make(send, "You deleted message #"+ chosenPosition, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {
                                        db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                                "','" + removedMessage.getMessage() +
                                                "','" + removedMessage.getSendOrReceive() +
                                                "','" + removedMessage.getTimeSent() + "');");
                                        messages.add(chosenPosition, removedMessage);
                                        adt.notifyItemInserted(chosenPosition);
                                    })
                                    .show();
                        }).create().show();
    }

    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews>{
        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            int LayoutID;
            if (viewType == 0)
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
            return messages.get(position).getSendOrReceive();
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

                ChatRoom parentActivity = (ChatRoom)getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(messages.get(position), position);


            });
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
    public class ChatMessage{
        String message;
        int sendOrReceive;
        String timeSent;
        Long id;

        public ChatMessage(String message, int sendOrReceive, String timeSent, long id) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
            setId(id);
        }

        public void setId(Long l) {
            id = l;
        }

        public Long getId() {
            return id;
        }

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
