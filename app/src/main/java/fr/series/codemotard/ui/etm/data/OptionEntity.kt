// 📁 Fichier : /ui/ETM/data/OptionEntity.kt
package fr.series.codemotard.ui.etm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OptionEntity(
    @PrimaryKey(autoGenerate = true) val optionId: Int = 0,
    val questionId: Int,
    val text: String,
    val correct: Boolean,
    val groupe: Int
)
