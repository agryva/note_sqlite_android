package id.irvan.is3_10518066;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import id.irvan.is3_10518066.model.NoteModel;
import id.irvan.is3_10518066.util.DataHelper;
import id.irvan.is3_10518066.util.StaticString;

public class MainActivity extends AppCompatActivity {
    private DataHelper dbHelper;
    private List<NoteModel> notes = new ArrayList();

    protected Cursor cursor;

    String[] title, description, createdAt;
    TodoAdapter adapter;
    RecyclerView recyView;
    View addNote;
    View emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recyView = findViewById(R.id.recy);
        addNote = findViewById(R.id.add_note);
        emptyLayout = findViewById(R.id.empty_layout);

        initRecyclerView();

        getData();
        addNote.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(StaticString.DETAIL_MODE_ENUM, DETAIL_MODE.ADD);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    void getData() {
        dbHelper = new DataHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM note", null);
        title = new String[cursor.getCount()];
        description = new String[cursor.getCount()];
        createdAt = new String[cursor.getCount()];
        notes.clear();
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            notes.add(new NoteModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        }
        adapter.updateData(notes);
        adapter.notifyDataSetChanged();

        if (notes.size() > 0) {
            emptyLayout.setVisibility(View.GONE);
            recyView.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            recyView.setVisibility(View.GONE);
        }
    }

    void initRecyclerView() {
        notes = new ArrayList<>();
        adapter = new TodoAdapter(MainActivity.this, (note) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(StaticString.DETAIL_MODE_ENUM, DETAIL_MODE.DETAIL);
            intent.putExtra(StaticString.DETAIL_DAO, note);
            startActivity(intent);
        }, notes);
        recyView.setLayoutManager(new LinearLayoutManager(this));
        recyView.setAdapter(adapter);
    }
}