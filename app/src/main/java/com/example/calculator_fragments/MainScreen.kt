package com.example.calculator_fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener


class MainScreen : Fragment() {
     lateinit var actionListener : FragmentActionListener
     lateinit var rootView : View

     var state2 = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_main_screen, container, false);
        if(savedInstanceState !=null){
            actionListener=activity as MainActivity
            state2 = savedInstanceState.getInt("state")
            if(savedInstanceState?.getInt(IS_RESULT_PAGE) == 1){
                modifyScreenForResult()
                rootView.findViewById<TextView>(R.id.text_result).text = savedInstanceState.getString(RESULT)
            }
        }
        if(state2 == 2){
            modifyScreenForResult()
        }
        rootView.findViewById<TextView>(R.id.toolbar_title).text = "Calculator"
        rootView.findViewById<Button>(R.id.add).setOnClickListener { actionListener.selectedOperation(Operation.ADD) }
        rootView.findViewById<Button>(R.id.subtract).setOnClickListener { actionListener.selectedOperation(Operation.SUBTRACT) }
        rootView.findViewById<Button>(R.id.multiply).setOnClickListener { actionListener.selectedOperation(Operation.MULTIPLY) }
        rootView.findViewById<Button>(R.id.divide).setOnClickListener { actionListener.selectedOperation(Operation.DIVIDE) }
       rootView.findViewById<Button>(R.id.button_reset).setOnClickListener {
           rootView.findViewById<LinearLayout>(R.id.layout_result).visibility = View.GONE
           rootView.findViewById<LinearLayout>(R.id.layout_buttons).visibility = View.VISIBLE
           rootView.findViewById<TextView>(R.id.text_result).text = ""
           actionListener.isResultScreen(1)
           state2=1


       }
        setFragmentResultListener(REQUEST_KEY) { requestKey, bundle ->
            rootView.findViewById<TextView>(R.id.text_result).text = getString(
                R.string.result_text, bundle.getString(RESULT), bundle.getString(
                    OPERAND1
                ), bundle.getString(OPERAND2), bundle.getString(OPERATION)
            )
            modifyScreenForResult()
            actionListener.isResultScreen(2)
            actionListener.selectedOperation(Operation.DEFAULT)
            state2 =2
        }


       return rootView
   }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if(this::rootView.isInitialized && rootView.findViewById<LinearLayout>(R.id.layout_result).visibility == View.VISIBLE){
            outState.putInt(IS_RESULT_PAGE, 1)
            outState.putInt("state2",2)
            outState.putString(RESULT, rootView.findViewById<TextView>(R.id.text_result).text.toString())
        }


    }
    private fun modifyScreenForResult() {
        Toast.makeText(context,"modify called",Toast.LENGTH_SHORT).show()

        rootView.findViewById<LinearLayout>(R.id.layout_buttons).visibility = View.GONE
        rootView.findViewById<LinearLayout>(R.id.layout_result).visibility = View.VISIBLE



    }





}