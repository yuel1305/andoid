package com.example.mystudent

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

  // Sử dụng ViewModel được khởi tạo với ViewModelFactory
  private val studentViewModel: StudentViewModel by viewModels {
    StudentViewModelFactory(StudentRepository(AppDatabase.getInstance(this).studentDao()))
  }

  private lateinit var adapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Thiết lập RecyclerView
    val recyclerView: RecyclerView = findViewById(R.id.recycler_view_students)
    recyclerView.layoutManager = LinearLayoutManager(this)

    adapter = StudentAdapter(mutableListOf()) { view, position ->
      adapter.contextMenuPosition = position
      registerForContextMenu(view)
      view.showContextMenu()
    }
    recyclerView.adapter = adapter

    // Quan sát danh sách sinh viên từ ViewModel
    studentViewModel.students.observe(this) { students ->
      adapter.studentList.clear()
      adapter.studentList.addAll(students)
      adapter.notifyDataSetChanged()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menu_add_new -> {
        val intent = Intent(this, AddStudentActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_ADD)
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onCreateContextMenu(
    menu: ContextMenu,
    v: View,
    menuInfo: ContextMenu.ContextMenuInfo?
  ) {
    super.onCreateContextMenu(menu, v, menuInfo)
    menuInflater.inflate(R.menu.context_menu, menu)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == RESULT_OK && data != null) {
      when (requestCode) {
        REQUEST_CODE_ADD -> {
          val newStudent = data.getParcelableExtra<StudentModel>("student")
          if (newStudent != null) {
            studentViewModel.insertStudent(newStudent)
          }
        }
        REQUEST_CODE_EDIT -> {
          val editedStudent = data.getParcelableExtra<StudentModel>("student")
          if (editedStudent != null) {
            studentViewModel.updateStudent(editedStudent)
          }
        }
      }
    }
  }

  override fun onContextItemSelected(item: MenuItem): Boolean {
    val position = adapter.contextMenuPosition
    val student = adapter.studentList[position] // Lấy student từ Adapter
    when (item.itemId) {
      R.id.menu_edit -> {
        val intent = Intent(this, EditStudentActivity::class.java)
        intent.putExtra("student", student)
        intent.putExtra("position", position)
        startActivityForResult(intent, REQUEST_CODE_EDIT)
      }
      R.id.menu_remove -> {
        studentViewModel.deleteStudent(student.studentId)
      }
    }
    return super.onContextItemSelected(item)
  }

  companion object {
    const val REQUEST_CODE_ADD = 1001
    const val REQUEST_CODE_EDIT = 1002
  }
}
