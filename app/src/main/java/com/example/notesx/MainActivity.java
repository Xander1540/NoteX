package com.example.notesx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnCreateNote;
    FloatingActionButton fabAdd;
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    LinearLayout llNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVar();


        showNotes();

        fabAdd.setOnClickListener(v -> {

            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.add_note_lay);

            EditText edtTitle, edtContent;
            Button btnAdd;

            edtTitle = dialog.findViewById(R.id.edtTitle);
            edtContent= dialog.findViewById(R.id.edtContent);
            btnAdd= dialog.findViewById(R.id.btnAdd);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = edtTitle.getText().toString();
                    String content = edtContent.getText().toString();

                    if(!content.equals("")){

                        databaseHelper.noteDao().addNote(new Note(title, content));
                        showNotes();

                        dialog.dismiss();

                    }else{
                        Toast.makeText(MainActivity.this, "Please add some content!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            dialog.show();
        });

        btnCreateNote.setOnClickListener(v -> {
            fabAdd.performClick();
        });
    }

    public void showNotes() {

        ArrayList<Note> arrNotes = (ArrayList<Note>) databaseHelper.noteDao().getNotes();

        if(arrNotes.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            llNotes.setVisibility(View.GONE);

            recyclerView.setAdapter(new RecyclerNotesAdapter(this, arrNotes, databaseHelper));

        }else{
            llNotes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    private void initVar() {
        btnCreateNote = findViewById(R.id.btnCreateNote);
        fabAdd = findViewById(R.id.fabAdd);
        recyclerView = findViewById(R.id.recyclerNotes);
        llNotes = findViewById(R.id.llNotes);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        databaseHelper = DatabaseHelper.getInstance(this);

    }

}