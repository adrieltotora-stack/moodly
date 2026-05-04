package com.example.moodly

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "mood_table")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val moodLevel: Int, // 1: 😢, 2: 😕, 3: 😐, 4: 🙂, 5: 😄
    val note: String,
    val tags: String,
    val date: Long = System.currentTimeMillis()
)

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_table ORDER BY date DESC")
    fun getAll(): Flow<List<MoodEntry>>
    @Insert
    suspend fun insert(entry: MoodEntry)
}

@Database(entities = [MoodEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
}