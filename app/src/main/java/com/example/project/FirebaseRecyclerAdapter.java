package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FirebaseRecyclerAdapter extends RecyclerView.Adapter<FirebaseRecyclerAdapter.ViewHolder> {
    private List<StudentDetails> student_list;
    private myInterface action_interface;

    public FirebaseRecyclerAdapter(List<StudentDetails> listdata1, myInterface action_interface) {
        this.student_list = listdata1;
        this.action_interface = action_interface;
    }

    @NonNull
    @Override
    public FirebaseRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_for_student, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    public void notifyData(List<StudentDetails> studentInformation) {

        this.student_list = studentInformation;
        this.notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(@NonNull FirebaseRecyclerAdapter.ViewHolder holder, int position) {
        StudentDetails student = student_list.get(position);
        holder.name.append(student_list.get(position).getName());
        holder.f_name.append(student_list.get(position).getFatherName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action_interface.openStudentsDetails(student);
            }
        });

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

