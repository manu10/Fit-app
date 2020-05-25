package com.manugarcia010.timeselectorview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.manugarcia010.timeselectorview.extensions.updateText

/**
 * TODO: Add & Use SelectorView as parent
 */
class TimeSelectorView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private lateinit var onTimeChangedListener: (Int) -> Unit
    private var label: String? = context.getString(R.string.test_label)

    private var timeInSeconds = 58

    private val timeCustomizerView: ConstraintLayout =
        View.inflate(context, R.layout.time_customizer_layout, this) as ConstraintLayout
    private var increaseBtn = timeCustomizerView.findViewById<ImageButton>(R.id.btn_increase_time)
    private var decreaseBtn = timeCustomizerView.findViewById<ImageButton>(R.id.btn_decrease_time)
    private var minutesInput = timeCustomizerView.findViewById<AppCompatEditText>(R.id.time_minutes_input)
    private var secondsInput = timeCustomizerView.findViewById<AppCompatEditText>(R.id.time_seconds_input)

    init {
        init(attrs)
    }
    private fun init(attrs: AttributeSet?) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.SelectorView
        )

        label = a.getString(
            R.styleable.SelectorView_label
        )
        setUpLabel(label)
        setUpButtons()
        setUpInputs()

        a.recycle()

    }

    private fun setUpInputs() {
        minutesInput.addTextChangedListener {
            if (minutesInput.hasFocus()){
                timeInSeconds = timeInSeconds % 60 + (it?.toString()?.toIntOrNull()?:0)*60
                onTimeChangedListener.invoke(timeInSeconds)
            }
        }
        secondsInput.addTextChangedListener {
            val inputInSeconds = it?.toString()?.toIntOrNull()?:0
            // ask for focus to avoid issues when the value is written programmatically
            if (secondsInput.hasFocus()) {
                timeInSeconds = timeInSeconds - (timeInSeconds % 60) + inputInSeconds
                if (timeInSeconds == 0) {
                    timeInSeconds = 1
                    updateView()
                } else {
                    if (inputInSeconds > 59)
                        updateView()
                }
                onTimeChangedListener.invoke(timeInSeconds)
            }
        }
    }

    private fun setUpButtons() {
        increaseBtn.setOnClickListener {
            if (timeInSeconds < 5999) {
                timeInSeconds = timeInSeconds.inc()
                onTimeChangedListener.invoke(timeInSeconds)
                updateView()
            }
        }
        decreaseBtn.setOnClickListener {
            if (timeInSeconds > 1) {
                timeInSeconds = timeInSeconds.dec()
                onTimeChangedListener.invoke(timeInSeconds)
                updateView()
            }
        }

    }

    private fun updateView() {
        minutesInput.updateText((timeInSeconds / 60).toString())
        secondsInput.updateText((timeInSeconds % 60).toString())
    }

    private fun setUpLabel(label: String?) {
        val labelTv = timeCustomizerView.findViewById<TextView>(R.id.time_label)
        labelTv.text = label

    }

    fun setOnTimeChangedListener(onTimeChangedListener: (Int) -> Unit) {
        this.onTimeChangedListener = onTimeChangedListener
    }

}