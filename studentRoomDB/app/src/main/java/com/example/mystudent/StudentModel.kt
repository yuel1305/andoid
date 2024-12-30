package com.example.mystudent

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "students")
data class StudentModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String,
    val studentId: String
)  : Parcelable

