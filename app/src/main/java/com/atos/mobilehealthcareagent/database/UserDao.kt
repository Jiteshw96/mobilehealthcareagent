package com.atos.mobilehealthcareagent.database

import androidx.room.*

@Dao
interface UserDao {
    @get:Query("SELECT * FROM user_basic_info")
    val all: List<User?>?

    @Query("SELECT * FROM user_basic_info where first_name LIKE  :firstName AND last_name LIKE :lastName")
    fun findByName(
        firstName: String?,
        lastName: String?
    ): User?

    @Query("SELECT COUNT(*) from user_basic_info")
    fun countUsers(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User?): LongArray?


    @Delete
    fun delete(user: User?)
}