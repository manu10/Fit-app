package com.manugarcia010.fitapp.timercustomizer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.manugarcia010.fitapp.R
import com.manugarcia010.fitapp.databinding.TimerCustomizerFragmentBinding
import com.manugarcia010.timeselectorview.TimeSelectorView
import com.manugarcia010.timeselectorview.UnitSelectorView
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class TimerCustomizerFragment : Fragment() {

    companion object {
        fun newInstance() =
            TimerCustomizerFragment()
    }

    private lateinit var viewDataBinding: TimerCustomizerFragmentBinding

    @Inject
    lateinit var viewModel: TimerCustomizerViewModel

    override fun onCreate(savedInstanceState: Bundle?) { //todo: extract to AppFragment open class
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.timer_customizer_fragment, container, false)
        viewDataBinding = TimerCustomizerFragmentBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(TimerCustomizerViewModel::class.java)
        // TODO: Use the ViewModel
        activity?.findViewById<TimeSelectorView>(R.id.work_time_selector)?.let {
            it.setOnTimeChangedListener { time ->
                Toast.makeText(activity,"Time changed to: $time", Toast.LENGTH_LONG).show()
            }
        }
        // TODO: Use the ViewModel
        activity?.findViewById<UnitSelectorView>(R.id.sets_input)?.let {
            it.setOnUnitChangedListener { time ->
                Toast.makeText(activity,"Time changed to: $time", Toast.LENGTH_LONG).show()
            }
        }
    }

}
