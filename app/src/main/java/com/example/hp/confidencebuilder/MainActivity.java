package com.example.hp.confidencebuilder;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
   static ArrayList <String>  notes=new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;
   static ArrayList <String>newList=new ArrayList<>();
    static ArrayList <String>id=new ArrayList<>();
    ListView listView;
    static SQLiteDatabase myDataBase;





       static public void datashow() {
        // myDataBase.execSQL("INSERT INTO diary(noteId,notes)VALUES(125,'ASHUTOSH')");
           Cursor c = myDataBase.rawQuery("SELECT * FROM diary ", null);
           int noteIdIndex = c.getColumnIndex("noteId");
           int notesIndex = c.getColumnIndex("notes");
           Log.i("error---",Boolean.toString(c.moveToFirst()));
           c.moveToFirst();
           if (c!=null) {

               while(!c.isAfterLast()){
                   newList.add(c.getString(notesIndex));
                   id.add(c.getString(noteIdIndex));
                   c.moveToNext();
               }

               Log.i("error---", "12");
               c.close();
           }
       }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.add_note:
                Intent intent=new Intent(this,EditActivity.class);
                startActivity(intent);
                return   true;
            default:
                return false;
        }

    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            listView=findViewById(R.id.listView);


          myDataBase =this.openOrCreateDatabase("diary",MODE_PRIVATE,null);
            myDataBase.execSQL("CREATE TABLE IF NOT EXISTS diary(noteId Int(3),notes VARCHAR)");

            datashow();


            Log.i("error---",Integer.toString(newList.size()));
            Log.i("error--",newList.toString());

          if(newList.size()!=0) {
              Log.i("error---", newList.toString());
              notes = newList;
          }
          else{
              if(notes.size()==0) {
                  Log.i("error---", newList.toString());
                  notes.add("example");
              }
          }
            arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notes);
            listView.setAdapter(arrayAdapter);




            listView.setLongClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                    intent.putExtra("noteId", position);
                    startActivity(intent);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.dialog_frame)
                            .setTitle("Are you sure Buddy?")
                            .setMessage("Do you want to delete this")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    notes.remove(position);
                                    arrayAdapter.notifyDataSetChanged();


                                    myDataBase.delete("diary","noteId=?",new String[]{Integer.toString(position)});


                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                    Log.i("commit","commit");
                    return true;

                }
            });

        }
}
