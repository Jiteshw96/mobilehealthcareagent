package com.atos.mobilehealthcareagent.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_basic_info")
class User {
    @PrimaryKey(autoGenerate = false)
    var uid = 0

    @ColumnInfo(name = "first_name")
    var firstName: String? = null

    @ColumnInfo(name = "last_name")
    var lastName: String? = null

    @ColumnInfo(name = "age")
    var age = 0

    @ColumnInfo(name = "weight")
    var weight = 0

    @ColumnInfo(name = "height")
    var height = 0


}