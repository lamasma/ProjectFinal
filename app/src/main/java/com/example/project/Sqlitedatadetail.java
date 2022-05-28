package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class Sqlitedatadetail extends AppCompatActivity {
    TextView s_id, s_name, s_surname, s_fatherName, s_nid, s_dob, s_gender;
    String student_id, student_name, student_surname, student_fatherName, student_nid, student_dob, student_gender;
    AlertDialog dialog_1;
    StudentDetails student_incoming;
    dbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlitedatadetail);
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
        s_id.setText("ID: "+student_incoming.getId());
        s_name.setText("Name: "+student_incoming.getName());
        s_surname.setText("SurName: "+student_incoming.getSurName());
        s_fatherName.setText("Father Name:"+student_incoming.getFatherName());
        s_nid.setText("ID Number:" +student_incoming.getNationalId());
        s_dob.setText("Date of Birth: "+student_incoming.getDateOfBirth());
        s_gender.setText("Gender: "+student_incoming.getGender());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sqlite_option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id_item = item.getItemId();
        switch (id_item) {

            case R.id.delete: {
                delete_data_from_sqlite(student_incoming.getId());
                break;
            }
            case R.id.edit: {
                updateData();
                break;
            }
        }
        return true;
    }

    private void updateData() {
        EditText s_name, s_id, s_dob, s_gender, s_surname, f_name, n_id;
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.add_student_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Sqlitedatadetail.this);
        builder.setView(dialoglayout);
        dialog_1 = builder.create();
        //builder.show();
        dialog_1.show();
        s_name = dialog_1.findViewById(R.id.etStudentName);
        s_name.setText(student_incoming.getName());
        s_id = dialog_1.findViewById(R.id.etStudentId);
        s_id.setText(student_incoming.getId());
        s_id.setEnabled(false);
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
                    dbHelper dbHelperClass = new dbHelper(Sqlitedatadetail.this);
                    dbHelperClass.updateDataInSQlite(student.getId(), student.getName(), student.getSurName(), student.getFatherName(), student.getNationalId(), student.getDateOfBirth(), student.getGender());
                    Toast.makeText(getApplicationContext(), "Data updated Successfully", Toast.LENGTH_SHORT).show();
                    SQLiteData.students_list_sqlite.clear();
                    SQLiteData.students_list_sqlite = dbHelperClass.getAllDataFromSQlite();
                    SQLiteData.adapter.notifyData(SQLiteData.students_list_sqlite);
                    dialog_1.dismiss();
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void delete_data_from_sqlite(String id) {
        dbHelper db = new dbHelper(this);
        db.deleteDataFromSQlite(id);
        Toast.makeText(getApplicationContext(), "Data removed successfully", Toast.LENGTH_SHORT).show();
        SQLiteData.adapter.notifyData(db.getAllDataFromSQlite());
        finish();

    }
}