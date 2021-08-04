package id.irvan.is3_10518066;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.irvan.is3_10518066.model.NoteModel;
import id.irvan.is3_10518066.util.DataHelper;
import id.irvan.is3_10518066.util.StaticString;

enum DETAIL_MODE {
    DETAIL,
    ADD,
    EDIT
}

public class DetailActivity extends AppCompatActivity {
    private DETAIL_MODE detail_mode;
    private View saveCircle;
    private View pencilCircle;
    private View backCircle;
    private View trashCircle;
    private EditText ed_title;
    private EditText ed_description;

    private DataHelper dbHelper;
    private NoteModel noteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new DataHelper(this);

        saveCircle = findViewById(R.id.back_save);
        pencilCircle = findViewById(R.id.back_edit);
        backCircle = findViewById(R.id.back_button);
        trashCircle = findViewById(R.id.trash_button);
        ed_title = findViewById(R.id.et_judul);
        ed_description = findViewById(R.id.et_desk);

        detail_mode = (DETAIL_MODE) getIntent().getSerializableExtra(StaticString.DETAIL_MODE_ENUM);
        noteModel = (NoteModel) getIntent().getSerializableExtra(StaticString.DETAIL_DAO);

        if (detail_mode == DETAIL_MODE.DETAIL) {
            pencilCircle.setVisibility(View.VISIBLE);
            saveCircle.setVisibility(View.GONE);
            ed_title.setEnabled(false);
            ed_description.setEnabled(false);
            trashCircle.setVisibility(View.VISIBLE);
        } else if (detail_mode == DETAIL_MODE.ADD) {
            pencilCircle.setVisibility(View.GONE);
            saveCircle.setVisibility(View.VISIBLE);
        } else {
            pencilCircle.setVisibility(View.VISIBLE);
            saveCircle.setVisibility(View.GONE);
        }


        setData();;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        saveCircle.setOnClickListener(v -> saveOnClickListener());
        backCircle.setOnClickListener(v -> finish());
        trashCircle.setOnClickListener(v -> {
            if (noteModel != null) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM note" + " where id='" + noteModel.id + "'");
                Toast.makeText(getApplicationContext(), "Hapus Note Berhasil", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        pencilCircle.setOnClickListener(v -> {
            if (detail_mode == DETAIL_MODE.DETAIL) {
                detail_mode = DETAIL_MODE.EDIT;
            }

            ed_title.setEnabled(true);
            ed_description.setEnabled(true);
            pencilCircle.setVisibility(View.GONE);
            saveCircle.setVisibility(View.VISIBLE);
        });
    }

    void setData() {
        if (noteModel != null) {
            ed_title.setText(noteModel.title);
            ed_description.setText(noteModel.desc);
        }
    }

    void saveOnClickListener() {
        if (ed_title.getText().toString().isEmpty() && ed_description.getText().toString().isEmpty()) {
            Toast.makeText(this, "Judul dan Note tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd MM yyyy, HH:mm");
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if (detail_mode == DETAIL_MODE.ADD) {
                db.execSQL("INSERT INTO note(title, description, created_at) VALUES('" +
                        ed_title.getText().toString() + "','" +
                        ed_description.getText().toString() + "','" +
                        fmtOut.format(currentTime) + "')");
            } else {
                db.execSQL("update note set title='" +
                        ed_title.getText().toString() + "', description='" + ed_description.getText().toString() + "', created_at='" +
                        fmtOut.format(currentTime) + "' where id='" + noteModel.id + "'");
            }

            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}