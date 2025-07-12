package com.kyuu.advancenoteapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class NewEditNoteActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextNote;
    private ImageView imageView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_note);

        editTextNote = findViewById(R.id.editTextNote);
        imageView = findViewById(R.id.imageView);
        Button btnSaveNote = findViewById(R.id.btnSaveNote);

        // mendapatkan status editing dari intent
        boolean isEditingMode = getIntent().getBooleanExtra("isEditingMode", false);

        if (isEditingMode) {
            setTitle("Edit Catatan");
            // menampilkan data yang akan diedit
            String noteText = getIntent().getStringExtra("noteText");
            String imageUriString = getIntent().getStringExtra("imageUri");
            if (imageUriString != null && !imageUriString.isEmpty()) {
                imageUri = Uri.parse(imageUriString);
                imageView.setImageURI(imageUri);
            }
            editTextNote.setText(noteText);
        }

        imageView.setOnClickListener(v -> openGallery());

        btnSaveNote.setOnClickListener(v -> saveNote());
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        imageUri = data.getData();
                        imageView.setImageURI(imageUri);
                    }
                }
            }
    );

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        galleryLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void saveNote() {
        // Dapatkan data catatan dari EditText dan imageUri
        String noteText = editTextNote.getText().toString().trim();
        String imageUriString = (imageUri != null) ? imageUri.toString() : "";

        // Kirim hasil edit kembali ke MainActivity
        Intent intent = new Intent();
        intent.putExtra("noteText", noteText);
        intent.putExtra("imageUri", imageUriString);

        int notePosition = getIntent().getIntExtra("notePosition", -1); // Dapatkan posisi catatan yang diedit
        if (notePosition != -1) {
            intent.putExtra("notePosition", notePosition); // Sertakan posisi catatan yang akan diedit
        }

        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}