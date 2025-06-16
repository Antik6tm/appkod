package fr.series.codemotard.ui.etm

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import fr.series.codemotard.R
import fr.series.codemotard.ui.etm.data.OptionEntity
import kotlinx.coroutines.launch

class QuestionFragment : Fragment() {

    private val viewModel: QuestionViewModel by viewModels()

    private lateinit var questionText: TextView
    private lateinit var questionText2: TextView
    private lateinit var questionImage: ImageView
    private lateinit var optionsContainer1: LinearLayout
    private lateinit var optionsContainer2: LinearLayout
    private lateinit var validateButton: Button
    private lateinit var explanationText: TextView

    private var selectedButtons = mutableListOf<RadioButton>()
    private var correctAnswers = listOf<OptionEntity>()
    private var validated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        questionText = view.findViewById(R.id.question_text)
        questionText2 = view.findViewById(R.id.question_text_2)
        questionImage = view.findViewById(R.id.question_image)
        optionsContainer1 = view.findViewById(R.id.options_container_1)
        optionsContainer2 = view.findViewById(R.id.options_container_2)
        validateButton = view.findViewById(R.id.validate_button)
        explanationText = view.findViewById(R.id.explanation_text)

        viewModel.loadSerie(1)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.questions.collect {
                refreshUI()
            }
        }

        validateButton.setOnClickListener {
            if (!validated) {
                val allButtons = optionsContainer1.findAllRadioButtons() + optionsContainer2.findAllRadioButtons()

                allButtons.forEach { btn ->
                    val isCorrect = correctAnswers.any { it.text == btn.text }
                    val isSelected = btn.isChecked

                    when {
                        isCorrect -> {
                            btn.setTextColor(Color.GREEN)
                            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0)
                        }
                        isSelected -> {
                            btn.setTextColor(Color.RED)
                            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross, 0, 0, 0)
                        }
                        else -> {
                            btn.setTextColor(Color.BLACK)
                            btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        }
                    }

                    btn.isEnabled = false
                }

                explanationText.visibility = View.VISIBLE
                validateButton.text = getString(R.string.question_suivante)
                validated = true
            } else {
                if (viewModel.hasNext()) {
                    viewModel.next()
                    validated = false
                    validateButton.text = getString(R.string.valider)
                    refreshUI()
                } else {
                    Toast.makeText(requireContext(), "Fin de la série !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun refreshUI() {
        val question = viewModel.currentQuestion() ?: return

        questionText.text = question.question.question1
        if (!question.question.question2.isNullOrEmpty()) {
            questionText2.text = question.question.question2
            questionText2.visibility = View.VISIBLE
        } else {
            questionText2.visibility = View.GONE
        }

        questionImage.load(question.question.image)
        explanationText.text = question.question.explanation
        explanationText.visibility = View.GONE

        optionsContainer1.removeAllViews()
        optionsContainer2.removeAllViews()
        selectedButtons.clear()

        val group1 = question.options.filter { it.groupe == 1 }.map { it.text }
        val group2 = question.options.filter { it.groupe == 2 }.map { it.text }

        setupOptions(group1, optionsContainer1)
        if (question.question.multiple && group2.isNotEmpty()) {
            optionsContainer2.visibility = View.VISIBLE
            setupOptions(group2, optionsContainer2)
        } else {
            optionsContainer2.visibility = View.GONE
        }

        correctAnswers = question.options.filter { it.correct }
    }

    private fun setupOptions(optionTexts: List<String>, container: LinearLayout) {
        val radioGroup = RadioGroup(requireContext())
        optionTexts.forEach { text ->
            val radioButton = RadioButton(requireContext())
            radioButton.text = text
            radioButton.setTextColor(Color.BLACK)
            radioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

            radioButton.setOnClickListener {
                selectedButtons.removeIf { it.parent == radioGroup }
                selectedButtons.add(radioButton)
            }

            radioGroup.addView(radioButton)
        }
        container.addView(radioGroup)
    }

    // 🔍 Extension pour récupérer tous les RadioButton d’un conteneur
    private fun ViewGroup.findAllRadioButtons(): List<RadioButton> {
        val list = mutableListOf<RadioButton>()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is RadioButton) list.add(child)
            if (child is ViewGroup) list.addAll(child.findAllRadioButtons())
        }
        return list
    }
}
