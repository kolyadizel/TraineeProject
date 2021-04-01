package com.example.hopeless.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao{

    @Query("SELECT COUNT(*) FROM task_table")
    fun getCount(): LiveData<Int>

    @Query("SELECT * FROM task_table")
    fun getTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}