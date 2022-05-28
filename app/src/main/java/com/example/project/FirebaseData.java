package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseData extends AppCompatActivity implements myInterface {
    EditText s_id, s_name, s_surname, f_name, n_id, s_dob, s_gender;
    String student_id, student_name, student_surname, student_fatherName, student_nid, student_dob, student_gender;
    FirebaseDatabase firebaseDatabase;
    EditText search_id;

    int check = 0;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter adapter;
    private RecyclerView recyclerView;
    List<StudentDetails> students_list_firebase;
    AlertDialog dialog_1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_data);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Firebase Data");
        progressDialog.setMessage("Loading");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        FirebaseDatabase.getInstance();
        search_id = findViewById(R.id.searchMessage);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students Data");
        recyclerView = findViewById(R.id.firebase_users_recycler_view);
        students_list_firebase = new ArrayList<StudentDetails>();
        adapter = new FirebaseRecyclerAdapter(students_list_firebase, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressDialog.show();
        search_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchData(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        databaseReference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                students_list_firebase.clear();
                Log.d("TAG", String.valueOf(snapshot.getChildren()));
                for (DataSnapshot obj : snapshot.getChildren()) {
                    StudentDetails student = obj.getValue(StudentDetails.class);
                    students_list_firebase.add(student);
                    adapter.notifyData(students_list_firebase);

                }
                progressDialog.dismiss();
                if (students_list_firebase.size() < 1) {
                    Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id_item = item.getItemId();
        switch (id_item) {
            case R.id.add_data: {
                add_data_to_firebase();
                break;
            }
            case R.id.fetch: {
                fetchFirebaseData();
                break;
            }
        }
        return true;
    }

    private void fetchFirebaseData() {
        FirebaseDatabase.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // below line is used to get reference for our database.
        databaseReference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                List<StudentDetails> students = new ArrayList<>();
                for (DataSnapshot obj : datasnapshot.getChildren()) {

                    StudentDetails student = obj.getValue(StudentDetails.class);
                    Log.d("TAGIns", student.getGender());
                    students.add(student);

                }
                for (StudentDetails obj : students
                ) {
                    saveDataToSqlite(obj.getId(),
                            obj.getName(),
                            obj.getSurName(),
                            obj.getFatherName(),
                            obj.getNationalId(),
                            obj.getDateOfBirth(),
                            obj.getGender());
                }
                if (check == students.size()) {
                    Toast.makeText(FirebaseData.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FirebaseData.this, check + " Records saved Successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FirebaseData.this,
                        error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void add_data_to_firebase() {

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.add_student_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(FirebaseData.this);
        builder.setView(dialoglayout);
        dialog_1 = builder.create();
        //builder.show();
        dialog_1.show();
        s_name = dialog_1.findViewById(R.id.etStudentName);
        s_id = dialog_1.findViewById(R.id.etStudentId);
        s_dob = dialog_1.findViewById(R.id.etStudentDOB);
        s_gender = dialog_1.findViewById(R.id.etStudentGender);
        s_surname = dialog_1.findViewById(R.id.etStudentSurName);
        f_name = dialog_1.findViewById(R.id.etStudentFathersName);
        n_id = dialog_1.findViewById(R.id.etStudentNationalID);
        Button btn = dialog_1.findViewById(R.id.btnSaveInFirebase);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                student_name = s_name.getText().toString();
                student_id = s_id.getText().toString();
                student_dob = s_dob.getText().toString();
                student_gender = s_gender.getText().toString();
                student_surname = s_surname.getText().toString();
                student_fatherName = f_name.getText().toString();
                student_nid = n_id.getText().toString();

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
                    databaseReference.child("Students").child(student.getId()).setValue(student);
                    Toast.makeText(getApplicationContext(), "Data inserted Successfully", Toast.LENGTH_SHORT).show();
                    //recreate();
                    dialog_1.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void saveDataToSqlite(String id, String name, String surName,
                                  String fatherName, String nationalId, String dob, String gender) {
        dbHelper db = new dbHelper(this);
        long insertData = db.addData(id, name, surName, fatherName, nationalId, dob, gender);
        // clearFields();

        if (insertData != -1) {
            check++;
            //Toast.makeText(FirebaseData.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();

        } else {
            //Toast.makeText(FirebaseData.this, "Data Insertion Failed", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void openStudentsDetails(StudentDetails student) {
        Intent i = new Intent(getApplicationContext(), detailsActivity.class);
        i.putExtra("Student", student);
        startActivity(i);
    }
    private void searchData(CharSequence charSequence) {
        ArrayList<StudentDetails> mPosts = new ArrayList<>();
        //users = new ArrayList<>();


        for (StudentDetails student : students_list_firebase) {
            //Log.e("TAG", "searchData: " +  searchText );
            if (student.getId().toLowerCase().contains(charSequence)) {
                Log.e("TAG", "searchData: " + charSequence);
                mPosts.add(student);

            }
        }
        adapter = new FirebaseRecyclerAdapter(mPosts,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}