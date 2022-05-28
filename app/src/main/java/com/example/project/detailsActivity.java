package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detailsActivity extends AppCompatActivity {
    TextView s_id, s_name, s_surname, s_fatherName, s_nid, s_dob, s_gender;
    String student_id,student_name,student_surname,student_fatherName,student_nid,student_dob,student_gender;
    ProgressDialog pDialog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StudentDetails student_incoming;
    AlertDialog dialog_1;
    dbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent i = getIntent();
        student_incoming = (StudentDetails) i.getSerializableExtra("Student");
        s_id = findViewById(R.id.id);
        s_name = findViewById(R.id.name);
        s_surname = findViewById(R.id.surName);
        s_fatherName = findViewById(R.id.F_name);
        s_nid = findViewById(R.id.nationID);
        s_dob = findViewById(R.id.DOB);
        db = new dbHelper(this);
        s_gender = findViewById(R.id.gender);
        pDialog = new ProgressDialog(detailsActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Students Data");
        pDialog.setMessage("Loading...");
        pDialog.show();

        s_id.append(student_incoming.getId());
        s_name.append(student_incoming.getName());
        s_surname.append(student_incoming.getSurName());
        s_fatherName.append(student_incoming.getFatherName());
        s_nid.append(student_incoming.getNationalId());
        s_dob.append(student_incoming.getDateOfBirth());
        s_gender.append(student_incoming.getGender());
        pDialog.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id_item = item.getItemId();
        switch (id_item) {
            case R.id.delete: {
                deleteFromFireBase();
                break;
            }
            case R.id.import_data: {
                import_data_to_sqlite();
                break;
            }
            case R.id.edit: {
                updateData();
                break;
            }
        }
        return true;
    }

    private void import_data_to_sqlite() {
        long c = db.addData(student_incoming.getId(), student_incoming.getName(), student_incoming.getSurName(), student_incoming.getFatherName(), student_incoming.getNationalId(), student_incoming.getDateOfBirth(), student_incoming.getGender());
        if (c != -1) {
            Toast.makeText(getApplicationContext(), "Successfully saved to sqlite", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Data already present there", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateData() {
        EditText s_name,s_id,s_dob,s_gender,s_surname,f_name,n_id;
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.add_student_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(detailsActivity.this);
        builder.setView(dialoglayout);
        dialog_1 = builder.create();
        //builder.show();
        dialog_1.show();
        s_name = dialog_1.findViewById(R.id.etStudentName);
        s_name.setText(student_incoming.getName());
        s_id = dialog_1.findViewById(R.id.etStudentId);
        s_id.setText(student_incoming.getId());
        s_dob = dialog_1.findViewById(R.id.etStudentDOB);
        s_dob.setText(student_incoming.getDateOfBirth());
        s_gender = dialog_1.findViewById(R.id.etStudentGender);
        s_gender.setText(student_incoming.getGender());
        s_surname = dialog_1.findViewById(R.id.etStudentSurName);
        s_surname.setText(student_incoming.getSurName());
        f_name = dialog_1.findViewById(R.id.etStudentFathersName);
        f_name.setText(student_incoming.getFatherName());
        n_id = dialog_1.findViewById(R.id.etStudentNationalID);
        n_id.setText(student_incoming.getNationalId());
        Button btn = dialog_1.findViewById(R.id.btnSaveInFirebase);
        btn.setText("Update Data to Firebase");
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
                    Toast.makeText(getApplicationContext(), "Data updated Successfully", Toast.LENGTH_SHORT).show();
                    dialog_1.dismiss();
                    finish();

                } else {
                    
                    Toast.makeText(getApplicationContext(),"One of the field is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteFromFireBase() {
        databaseReference.child("Students").child(student_incoming.getId()).removeValue();
        Toast.makeText(this, "Record Deleted Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

}