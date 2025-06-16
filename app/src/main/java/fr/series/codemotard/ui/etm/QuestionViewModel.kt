// 📁 Fichier : /ui/etm/QuestionViewModel.kt
package fr.series.codemotard.ui.etm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.series.codemotard.ui.etm.data.AppDatabase
import fr.series.codemotard.ui.etm.data.QuestionWithOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).questionDao()
    private val _questions = MutableStateFlow<List<QuestionWithOptions>>(emptyList())
    val questions: StateFlow<List<QuestionWithOptions>> = _questions

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    fun loadSerie(serie: Int) {
        viewModelScope.launch {
            _questions.value = dao.getQuestionsWithOptionsBySerie(serie)
            _currentIndex.value = 0
        }
    }

    fun currentQuestion(): QuestionWithOptions? {
        return _questions.value.getOrNull(_currentIndex.value)
    }

    fun hasNext(): Boolean {
        return _currentIndex.value + 1 < _questions.value.size
    }

    fun next() {
        if (hasNext()) {
            _currentIndex.value += 1
        }
    }
}
