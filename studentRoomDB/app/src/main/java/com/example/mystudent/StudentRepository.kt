package com.example.mystudent
import androidx.lifecycle.LiveData

class StudentRepository(private val studentDao: StudentDao) {
    fun getAllStudents(): LiveData<List<StudentModel>> = studentDao.getAllStudents() // Trả về LiveData

    suspend fun insertStudent(student: StudentModel) = studentDao.insertStudent(student)

    suspend fun updateStudent(student: StudentModel) = studentDao.updateStudent(student)

    suspend fun deleteStudent(studentId: String) = studentDao.deleteStudent(studentId)
}

