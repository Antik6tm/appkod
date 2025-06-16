// 📁 Fichier : /ui/ETM/data/QuestionWithOptions.kt
package fr.series.codemotard.ui.etm.data

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithOptions(
    @Embedded val question: QuestionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionId"
    )
    val options: List<OptionEntity>
)
