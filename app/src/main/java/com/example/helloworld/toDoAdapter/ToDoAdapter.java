package com.example.helloworld.toDoAdapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.Model.ToDoModel;
import com.example.helloworld.R;
import com.example.helloworld.ToDoActivity;
import com.example.helloworld.Utils.ToDoDatabaseHelper;
import com.example.helloworld.addNewTask;

import java.util.List;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private ToDoActivity activity;
    private ToDoDatabaseHelper myDb;

    public ToDoAdapter(ToDoDatabaseHelper myDb, ToDoActivity activity){
        this.activity = activity;
        this.myDb = myDb;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_do_task_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel item = mList.get(position);
        holder.todoCheckBox.setText(item.getTask());
        holder.todoCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.todoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    myDb.updateStatus(item.getId(),1);
                    holder.todoCheckBox.setPaintFlags(holder.todoCheckBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else {
                    myDb.updateStatus(item.getId(),0);
                    holder.todoCheckBox.setPaintFlags(holder.todoCheckBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

                }
            }
        });
    }

    public boolean toBoolean (int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setTasks(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        ToDoModel item = mList.get(position);
        myDb.deleteTask(item.getId());

        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("Id",item.getId());
        bundle.putString("task",item.getTask());

        addNewTask task = new addNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(),task.getTag());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        CheckBox todoCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            todoCheckBox = itemView.findViewById(R.id.todoCheckbox);
        }
    }
}
