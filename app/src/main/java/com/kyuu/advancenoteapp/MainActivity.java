package com.kyuu.advancenoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener{
    private static final int REQUEST_ADD_NOTE = 1;
    private static final int REQUEST_EDIT_NOTE = 1;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        Button btnAddNote = findViewById(R.id.btnAddNote);

        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerViewNotes.setAdapter(noteAdapter);

        btnAddNote.setOnClickListener(view -> {
            // Handle add note button click
            startNewEditNoteActivity(false, "", "", -1);
        });
    }

    @Override
    public void onEditClick(int position) {
        if (position >= 0 && position < noteList.size()) {
            // Jika posisi valid, lakukan edit catatan
            editNote(position);
        } else {
            // Penanganan kesalahan jika posisi tidak valid
            Toast.makeText(this, "Invalid position", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteClick(int position) {
        // Handle delete button click
        deleteNote(position);
    }

    private void editNote(int position) {
        // Mendapatkan catatan yang dipilih
        if (position >= 0 && position < noteList.size()) {
            Note selectedNote = noteList.get(position);

            // Mengirim data catatan yang akan diedit ke NewEditNoteActivity
            startNewEditNoteActivity(true, selectedNote.getNoteText(), selectedNote.getImageUri(), position);
        }
    }

    private void deleteNote(int position) {
        // menghapus catatan dari list
        noteList.remove(position);

        // memberitahu adapter bahwa item telah dihapus
        noteAdapter.notifyItemRemoved(position);
    }

    private void startNewEditNoteActivity(boolean isEditingMode, String noteText, String imageUri, int notePosition) {
        Intent intent = new Intent(MainActivity.this, NewEditNoteActivity.class);
        intent.putExtra("isEditingMode", isEditingMode);
        intent.putExtra("noteText", noteText);
        intent.putExtra("imageUri", imageUri);
        intent.putExtra("notePosition", notePosition); // tambahkan posisi catatan yang akan diedit
        newEditNoteLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> newEditNoteLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            String noteText = result.getData().getStringExtra("noteText");
                            String imageUri = result.getData().getStringExtra("imageUri");
                            int notePosition = result.getData().getIntExtra("notePosition", -1);

                            if (notePosition != -1) {
                                // Memperbarui catatan yang sudah ada dengan data yang diedit
                                Note editedNote = noteList.get(notePosition);
                                editedNote.setNoteText(noteText);
                                editedNote.setImageUri(imageUri);

                                // Memberitahu adapter bahwa data telah berubah
                                noteAdapter.notifyItemChanged(notePosition);
                            } else {
                                // Menambahkan catatan baru ke list
                                Note note = new Note();
                                note.setNoteText(noteText != null ? noteText : "");
                                note.setImageUri(imageUri != null ? imageUri : "");
                                noteList.add(note);
                                noteAdapter.notifyItemInserted(noteList.size() - 1);
                            }
                        }
                    });
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_NOTE && resultCode == RESULT_OK && data != null) {
            int editedNotePosition = data.getIntExtra("notePosition", -1); // Dapatkan posisi catatan yang diedit
            String editedNoteText = data.getStringExtra("noteText");
            String editedImageUri = data.getStringExtra("imageUri");

            if (editedNotePosition != -1) {
                // Perbarui catatan di noteList dengan data yang diedit
                Note editedNote = noteList.get(editedNotePosition);
                editedNote.setNoteText(editedNoteText);
                editedNote.setImageUri(editedImageUri);

                // Perbarui tampilan di RecyclerView
                noteAdapter.notifyItemChanged(editedNotePosition);
            }
        }
    }

}