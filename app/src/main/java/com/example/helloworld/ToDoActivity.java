package com.example.helloworld;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.Model.ToDoModel;
import com.example.helloworld.Utils.ToDoDatabaseHelper;
import com.example.helloworld.toDoAdapter.ToDoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDoActivity extends AppCompatActivity implements ToDoOnDialogCloseListener{

    RecyclerView recyclerViewToDo;
    FloatingActionButton addToDoButton;
    ToDoDatabaseHelper todoDb;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_to_do);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerViewToDo = findViewById(R.id.recyclerViewToDo);
        addToDoButton = findViewById(R.id.addToDoButton);
        todoDb = new ToDoDatabaseHelper(ToDoActivity.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(todoDb,ToDoActivity.this);

        recyclerViewToDo.setHasFixedSize(true);
        recyclerViewToDo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewToDo.setAdapter(adapter);

        mList= todoDb.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);




        addToDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTask.newInstance().show(getSupportFragmentManager(),addNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new toDoRViewTHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerViewToDo);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList= todoDb.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();

    }
}