package fr.series.codemotard.ui.etm

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import fr.series.codemotard.ui.etm.data.AppDatabase
import fr.series.codemotard.ui.etm.data.QuestionEntity
import fr.series.codemotard.ui.etm.data.OptionEntity

object Prepopulate {
    fun prepopulateDatabase(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppDatabase.getInstance(context).questionDao()

            if (dao.getAllQuestions().isEmpty()) {
                val questions = listOf(
                    QuestionEntity(
                        id = 9,
                        question1 = "À 90 km/h, un poids lourd parcourt environ combien de mètres en 1 seconde ?",
                        question2 = "Et à 130 km/h, une moto parcourt environ combien de mètres en 1 seconde ?",
                        image = "https://codemotard.fr/app/img/9.jpg",
                        explanation = "À 130 km/h, on divise 130 par 3,6 : cela fait environ 36 mètres par seconde.",
                        thematique = "regles_circulation",
                        serie = 1,
                        multiple = true
                    ),
                    QuestionEntity(
                        id = 39,
                        question1 = "À cette intersection, que devez-vous impérativement faire ?",
                        question2 = null,
                        image = "https://codemotard.fr/app/img/40.jpg",
                        explanation = "Un panneau STOP impose un arrêt complet à la ligne, quelle que soit la circulation.",
                        thematique = "regles_circulation",
                        serie = 1,
                        multiple = false
                    )
                )

                val options = listOf(
                    // Question 9 - groupe 1
                    OptionEntity(questionId = 9, text = "15 mètres", correct = false, groupe = 1),
                    OptionEntity(questionId = 9, text = "25 mètres", correct = true, groupe = 1),
                    // Question 9 - groupe 2
                    OptionEntity(questionId = 9, text = "25 mètres", correct = false, groupe = 2),
                    OptionEntity(questionId = 9, text = "35 mètres", correct = true, groupe = 2),

                    // Question 39
                    OptionEntity(questionId = 39, text = "Ralentir sans marquer l’arrêt", correct = false, groupe = 1),
                    OptionEntity(questionId = 39, text = "Marquer l’arrêt à la ligne rouge, même sans autre usager", correct = true, groupe = 1),
                    OptionEntity(questionId = 39, text = "M’arrêter uniquement si un véhicule approche", correct = false, groupe = 1),
                    OptionEntity(questionId = 39, text = "Passer si je ne gêne personne", correct = false, groupe = 1)
                )

                questions.forEach { question ->
                    val relatedOptions = options.filter { it.questionId == question.id }
                    dao.insertQuestionWithOptions(question, relatedOptions)
                }
            }
        }
    }
}
