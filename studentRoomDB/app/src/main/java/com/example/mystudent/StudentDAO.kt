package com.example.mystudent
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStudent(student: StudentModel): Long

    @Query("SELECT * FROM students ORDER BY studentName ASC")
    fun getAllStudents(): LiveData<List<StudentModel>>

    @Update
    suspend fun updateStudent(student: StudentModel): Int

    @Query("DELETE FROM students WHERE studentId = :studentId")
    suspend fun deleteStudent(studentId: String): Int

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Int): StudentModel?
}
