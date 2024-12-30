package com.example.mystudent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddStudentActivity : AppCompatActivity() {

    private lateinit var studentDao: StudentDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add)

        val edtName: EditText = findViewById(R.id.name)
        val edtMSSV: EditText = findViewById(R.id.mssv)
        val btnSave: Button = findViewById(R.id.btnSave)

        // Khởi tạo Room Database
        val db = AppDatabase.getInstance(this)
        studentDao = db.studentDao()

        btnSave.setOnClickListener {
            val name = edtName.text.toString().trim()
            val mssv = edtMSSV.text.toString().trim()

            if (name.isNotEmpty() && mssv.isNotEmpty()) {
                val student = StudentModel(studentName = name, studentId = mssv)

                // Lưu vào database bằng coroutine
                lifecycleScope.launch {
                    studentDao.insertStudent(student)
                    Toast.makeText(this@AddStudentActivity, "Student added successfully!", Toast.LENGTH_SHORT).show()
                    finish() // Quay lại màn hình trước
                }
            } else {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
