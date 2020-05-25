package com.manugarcia010.timeselectorview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.manugarcia010.timeselectorview.extensions.updateText

/**
 * TODO: Add & Use SelectorView as parent
 */
class UnitSelectorView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private lateinit var onUnitChangedListener: (Int) -> Unit
    private var label: String? = ""//context.getString(R.string.test_label)

    private var units = 58

    private val unitCustomizerView: ConstraintLayout =
        View.inflate(context, R.layout.unit_customizer_layout, this) as ConstraintLayout
    private val increaseBtn = unitCustomizerView.findViewById<AppCompatImageButton>(R.id.btn_increase_unit)
    private val decreaseBtn = unitCustomizerView.findViewById<AppCompatImageButton>(R.id.btn_decrease_unit)
    private val unitInput = unitCustomizerView.findViewById<AppCompatEditText>(R.id.unit_input)
    private val labelTv = unitCustomizerView.findViewById<TextView>(R.id.unit_label)

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
        unitInput.addTextChangedListener {
            if (unitInput.hasFocus()){
                val unitInputValue = it?.toString()?.toIntOrNull()?:0
                if (unitInputValue == 0) {
                    units = 1
                    updateView()
                } else {
                    units = unitInputValue
                }
                onUnitChangedListener.invoke(units)
            }
        }
    }

    private fun setUpButtons() {
        increaseBtn.setOnClickListener {
            if (units < 99) {
                units = units.inc()
                onUnitChangedListener.invoke(units)
                updateView()
            }
        }
        decreaseBtn.setOnClickListener {
            if (units > 1) {
                units = units.dec()
                onUnitChangedListener.invoke(units)
                updateView()
            }
        }

    }

    private fun updateView() {
        unitInput.updateText(units.toString())
    }

    private fun setUpLabel(label: String?) {
        labelTv.text = label
    }

    fun setOnUnitChangedListener(onunitChangedListener: (Int) -> Unit) {
        this.onUnitChangedListener = onunitChangedListener
    }

}