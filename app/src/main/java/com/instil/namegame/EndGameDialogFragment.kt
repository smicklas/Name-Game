package com.instil.namegame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class EndGameDialogFragment: DialogFragment() {

    private lateinit var layoutView : View
    private val viewModel: DialogViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.setCanceledOnTouchOutside(false)
        layoutView = inflater.inflate(R.layout.end_screen_dialog, container, false);

        var restartButton = layoutView.findViewById<Button>(R.id.restartButton)

        restartButton.setOnClickListener {
            restartGame();
        }
        return layoutView
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels)
        val height = (resources.displayMetrics.heightPixels)
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun restartGame(){
        val fragment: Fragment? = getFragmentManager()?.findFragmentByTag("game")
        if (fragment != null) getFragmentManager()?.beginTransaction()?.remove(fragment)
            ?.commit()
        //dismiss dialog
        dismiss()
    }
}