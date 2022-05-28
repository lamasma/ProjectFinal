package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SQLiteData extends AppCompatActivity implements myInterface {
    static SQLiteRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    static List<StudentDetails> students_list_sqlite;
    EditText s_id_1, s_name_1, s_surname_1, f_name_1, n_id_1, s_dob_1, s_gender_1;
    String student_id, student_name, student_surname, student_fatherName, student_nid, student_dob, student_gender;
    AlertDialog dialog_1;
    dbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_data);
        db = new dbHelper(this);
        recyclerView = findViewById(R.id.firebase_users_recycler_view);
        students_list_sqlite = db.getAllDataFromSQlite();
        adapter = new SQLiteRecyclerAdapter(students_list_sqlite, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void openStudentsDetails(StudentDetails student) {
        Intent i = new Intent(getApplicationContext(), Sqlitedatadetail.class);
        i.putExtra("Student", student);
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_sqlite_recycler, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id_item = item.getItemId();
        switch (id_item) {
            case R.id.add_data_sqlite: {
                add_data_to_sqlite();
                break;
            }
        }
        return true;
    }

    private void copyDate() {
    }

    private void add_data_to_sqlite() {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.add_student_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(SQLiteData.this);
        builder.setView(dialoglayout);
        dialog_1 = builder.create();
        //builder.show();
        dialog_1.show();
        s_name_1 = dialog_1.findViewById(R.id.etStudentName);
        s_id_1 = dialog_1.findViewById(R.id.etStudentId);
        s_dob_1 = dialog_1.findViewById(R.id.etStudentDOB);
        s_gender_1 = dialog_1.findViewById(R.id.etStudentGender);
        s_surname_1 = dialog_1.findViewById(R.id.etStudentSurName);
        f_name_1 = dialog_1.findViewById(R.id.etStudentFathersName);
        n_id_1 = dialog_1.findViewById(R.id.etStudentNationalID);
        Button btn = dialog_1.findViewById(R.id.btnSaveInFirebase);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_name = s_name_1.getText().toString();
                student_id = s_id_1.getText().toString();
                student_dob = s_dob_1.getText().toString();
                student_gender = s_gender_1.getText().toString();
                student_surname = s_surname_1.getText().toString();
                student_fatherName = f_name_1.getText().toString();
                student_nid = n_id_1.getText().toString();

                Boolean isFieldEmpty = !student_name.isEmpty() && !student_id.isEmpty() && !student_dob.isEmpty() && !student_gender.isEmpty() && !student_surname.isEmpty() && !student_fatherName.isEmpty() && !student_nid.isEmpty();
                if (isFieldEmpty) {
                    StudentDetails student = new StudentDetails();
                    student.setName(student_name);
                    student.setId(student_id);
                    student.setDateOfBirth(student_dob);
                    student.setGender(student_gender);
                    student.setSurName(student_surname);
                    student.setFatherName(student_fatherName);
                    student.setNationalId(student_nid);
                    db.addData(student.getId(), student.getName(), student.getSurName(), student.getFatherName(), student.getNationalId(), student.getDateOfBirth(), student.getGender());
                    dialog_1.dismiss();
                    Toast.makeText(getApplicationContext(), "Data inserted Successfully", Toast.LENGTH_SHORT).show();
                    adapter.notifyData(db.getAllDataFromSQlite());

                } else {
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}