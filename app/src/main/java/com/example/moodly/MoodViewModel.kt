package com.example.moodly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoodViewModel(private val dao: MoodDao) : ViewModel() {
    val entries = dao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getMostFrequent(list: List<MoodEntry>): String {
        if (list.isEmpty()) return "N/A"
        val top = list.take(7).groupBy { it.moodLevel }.maxByOrNull { it.value.size }?.key ?: 3
        return mapToEmoji(top)
    }

    fun mapToEmoji(level: Int) = when(level) {
        1 -> "😢" 2 -> "😕" 3 -> "😐" 4 -> "🙂" 5 -> "😄" else -> "😐"
    }

    fun save(level: Int, note: String, tags: List<String>) {
        viewModelScope.launch { dao.insert(MoodEntry(moodLevel = level, note = note, tags = tags.joinToString(", "))) }
    }
}