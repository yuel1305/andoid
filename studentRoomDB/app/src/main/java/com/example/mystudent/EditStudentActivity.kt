package com.example.mystudent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditStudentActivity : AppCompatActivity() {

    private lateinit var studentDao: StudentDao
    private lateinit var edtName: EditText
    private lateinit var edtMSSV: EditText
    private lateinit var btnSave: Button
    private var studentId: Int = 0 // ID của sinh viên được truyền từ Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add)

        // Khởi tạo Room Database
        val db = AppDatabase.getInstance(this)
        studentDao = db.studentDao()

        // Ánh xạ view
        edtName = findViewById(R.id.name)
        edtMSSV = findViewById(R.id.mssv)
        btnSave = findViewById(R.id.btnSave)

        // Lấy dữ liệu từ Intent (ID của sinh viên cần chỉnh sửa)
        studentId = intent.getIntExtra("student_id", 0)

        // Tải thông tin sinh viên từ database và hiển thị
        loadStudentData()

        // Xử lý sự kiện khi người dùng nhấn nút lưu
        btnSave.setOnClickListener {
            val updatedName = edtName.text.toString().trim()
            val updatedMSSV = edtMSSV.text.toString().trim()

            if (updatedName.isNotEmpty() && updatedMSSV.isNotEmpty()) {
                updateStudent(studentId, updatedName, updatedMSSV)
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadStudentData() {
        CoroutineScope(Dispatchers.IO).launch {
            val student = studentDao.getStudentById(studentId)
            student?.let {
                // Hiển thị dữ liệu trên giao diện (chạy trên luồng chính)
                runOnUiThread {
                    edtName.setText(it.studentName)
                    edtMSSV.setText(it.studentId)
                }
            } ?: runOnUiThread {
                Toast.makeText(this@EditStudentActivity, "Không tìm thấy sinh viên!", Toast.LENGTH_SHORT).show()
                finish() // Đóng activity nếu không tìm thấy sinh viên
            }
        }
    }

    private fun updateStudent(studentId: Int, name: String, mssv: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val updatedStudent = StudentModel(id = studentId, studentName = name, studentId = mssv)
            studentDao.updateStudent(updatedStudent)

            runOnUiThread {
                Toast.makeText(this@EditStudentActivity, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                finish() // Đóng activity và quay lại màn hình chính
            }
        }
    }
}
