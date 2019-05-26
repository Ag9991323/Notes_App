package com.example.hp.confidencebuilder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {
    EditText editText;
    int noteId;

    public void updatelist(){
        Log.i("error---","7");
        Cursor c= MainActivity.myDataBase.rawQuery("SELECT * FROM diary ",null);
        Log.i("error---","8");
        int noteIdIndex=c.getColumnIndex("noteID");
        Log.i("error---","9");
        int notesIndex=c.getColumnIndex("notes");
        Log.i("error---","10");
        c.moveToFirst();
        Log.i("error---",c.getString(notesIndex));
        MainActivity.notes.add(c.getString(notesIndex));
        Log.i("error---","11");
        MainActivity.arrayAdapter.notifyDataSetChanged();
        Log.i("error---","12");
        c.close();
        Log.i("error---","13");
    }
//    public  void trys(Void view){
//

//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
         editText=findViewById(R.id.editText);
        Intent intent =getIntent();
        noteId=intent.getIntExtra("noteId",-1);
        if(noteId!=-1){
            editText.setText(MainActivity.notes.get(noteId));
        }
      else{
            MainActivity.notes.add("");
           noteId= MainActivity.notes.size()-1;
           }
           editText.addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

               }

               @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {
                   //str.add(String.valueOf(s));
                   Log.i("aaaaa", String.valueOf(s));
                   MainActivity.notes.set(noteId, String.valueOf(s));
                   MainActivity.arrayAdapter.notifyDataSetChanged();


               }

               @Override
               public void afterTextChanged(Editable s){}
           });

        editText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // the user is done typing.
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("noteId", noteId); // string containing 2 eggs
                                contentValues.put("notes",editText.getText().toString()); // string containing 2 eggs
                                MainActivity.myDataBase.insert("diary", null, contentValues);
                                Log.i("aaaaa",editText.getText().toString());

                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );







    }
}
