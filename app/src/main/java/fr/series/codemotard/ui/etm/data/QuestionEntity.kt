// 📁 Fichier : /ui/ETM/data/QuestionEntity.kt
package fr.series.codemotard.ui.etm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionEntity(
    @PrimaryKey val id: Int,
    val question1: String,
    val question2: String?,
    val image: String,
    val explanation: String,
    val thematique: String,
    val serie: Int,
    val multiple: Boolean
)
