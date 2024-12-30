package com.example.mystudent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    val students: LiveData<List<StudentModel>> = repository.getAllStudents() // Quan sát danh sách từ repository

    fun insertStudent(student: StudentModel) {
        viewModelScope.launch {
            repository.insertStudent(student)
        }
    }

    fun updateStudent(student: StudentModel) {
        viewModelScope.launch {
            repository.updateStudent(student)
        }
    }

    fun deleteStudent(studentId: String) {
        viewModelScope.launch {
            repository.deleteStudent(studentId)
        }
    }
}
