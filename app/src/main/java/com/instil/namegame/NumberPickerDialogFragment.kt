package com.instil.namegame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels


class NumberPickerDialogFragment: DialogFragment() {

    private lateinit var layoutView : View
    private val viewModel: DialogViewModel by activityViewModels()

    val pickerValues = arrayOf("5", "10", "20", "50", "100")

    companion object {
        var INPUT_NUMBER = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutView = inflater.inflate(R.layout.number_picker_dialog, container, false);
        var numberPicker = layoutView.findViewById<NumberPicker>(R.id.questionPickerInput)
        //TODO check if the number of questions has been touched and show that value instead
        numberPicker.setDisplayedValues(pickerValues);
        numberPicker.setMaxValue(pickerValues.size-1);
        numberPicker.setMinValue(0);

        numberPicker.setOnValueChangedListener { _, _, newVal ->
            viewModel.setItem(pickerValues[newVal])
        }

        //getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        return layoutView
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels)
        val height = (resources.displayMetrics.heightPixels)
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
