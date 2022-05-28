package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SQLiteRecyclerAdapter extends RecyclerView.Adapter<SQLiteRecyclerAdapter.ViewHolder> {
    private List<StudentDetails> student_list;
    private myInterface action_interface;

    public SQLiteRecyclerAdapter(List<StudentDetails> listdata1, myInterface action_interface) {
        this.student_list = listdata1;
        this.action_interface = action_interface;
    }

    @NonNull
    @Override
    public SQLiteRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_for_student, parent, false);
        SQLiteRecyclerAdapter.ViewHolder viewHolder = new SQLiteRecyclerAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentDetails student = student_list.get(position);
        holder.name.setText("Name: "+student_list.get(position).getName());
        holder.f_name.setText("Father Name: "+student_list.get(position).getFatherName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action_interface.openStudentsDetails(student);
            }
        });

    }

    public void notifyData(List<StudentDetails> studentInformation) {

        this.student_list = studentInformation;
        this.notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return student_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView f_name;
        public TextView name;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.f_name = itemView.findViewById(R.id.f_name);
            this.name = itemView.findViewById(R.id.name);
            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
