package fr.series.codemotard.ui.etm.data

import androidx.room.*
//import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuestions(questions: List<QuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOptions(options: List<OptionEntity>)

    @Transaction
    suspend fun insertQuestionWithOptions(question: QuestionEntity, options: List<OptionEntity>) {
        insertAllQuestions(listOf(question))
        insertAllOptions(options)
    }

    @Transaction
    @Query("SELECT * FROM QuestionEntity WHERE serie = :serie")
    suspend fun getQuestionsWithOptionsBySerie(serie: Int): List<QuestionWithOptions>

    @Transaction
    @Query("SELECT * FROM QuestionEntity")
    suspend fun getAllQuestions(): List<QuestionEntity>
}
