package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notebook.database.NoteDatabase;

public class NoteActivity extends AppCompatActivity {

    private Button saveButton;
    private Button discardButton;

    private EditText titleEDT;
    private EditText contentEDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initUI();

        Note note = (Note)getIntent().getExtras().get("note_data");
        if(note != null) {
            titleEDT.setText(note.getTitle());
            contentEDT.setText(note.getContent());
        }

        saveButton.setOnClickListener(view -> onSaveButtonClicked(note));
        discardButton.setOnClickListener(view -> finish());
    }

    private void initUI() {
        saveButton = findViewById(R.id.btn_save);
        discardButton = findViewById(R.id.btn_discard);
        titleEDT = findViewById(R.id.edt_title);
        contentEDT = findViewById(R.id.edt_content);
    }

    private void onSaveButtonClicked(Note note) {
        String title = titleEDT.getText().toString();
        String content = contentEDT.getText().toString();

        if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Title and content must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(note == null) {
            note = new Note("", "");
            note.setTitle(title);
            note.setContent(content);

            NoteDatabase.getInstance(this).noteDAO().insert(note);

            Toast.makeText(this,"Create successfully", Toast.LENGTH_SHORT).show();

            Intent intentResult = new Intent();
            setResult(Activity.RESULT_OK, intentResult);
            finish();
            return;
        }
        note.setTitle(title);
        note.setContent(content);
        NoteDatabase.getInstance(this).noteDAO().update(note);
        Toast.makeText(this,"Update successfully", Toast.LENGTH_SHORT).show();

        Intent intentResult = new Intent();
        setResult(Activity.RESULT_OK, intentResult);
        finish();
    }
}