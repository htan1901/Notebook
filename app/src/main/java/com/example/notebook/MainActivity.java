package com.example.notebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.notebook.database.NoteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 10;

    private Button addButton;
    private RecyclerView rcvNotes;

    private NoteAdapter noteAdapter;
    private List<Note> listNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        noteAdapter = new NoteAdapter(new NoteAdapter.OnItemClicked() {
            @Override
            public void deleteNote(Note note) {
                clickDeleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                clickUpdateNote(note);
            }
        });
        loadData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rcvNotes.setLayoutManager(linearLayoutManager);
        rcvNotes.setAdapter(noteAdapter);

        addButton.setOnClickListener(view -> clickCreateNote());
    }

    private void initUI() {
        addButton = findViewById(R.id.add_button);
        rcvNotes = findViewById(R.id.rcv_list_note);
    }

    private void loadData() {
        listNote = NoteDatabase.getInstance(this).noteDAO().getAllNote();
        noteAdapter.setData(listNote);
    }

    private void clickCreateNote() {
        Note note = null;
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("note_data",note);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);
    }

    private void clickDeleteNote(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Delete \"" + note.getTitle() + "\"?")
                .setPositiveButton("No", null)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NoteDatabase.getInstance(MainActivity.this).noteDAO().delete(note);
                        Toast.makeText(MainActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .show();
    }

    private void clickUpdateNote(Note note) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("note_data",note);
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }
}