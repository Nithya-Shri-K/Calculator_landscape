package com.example.calculator_fragments

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult


class Calculate : Fragment() {
    private lateinit var rootView : View
    lateinit var actionListener: FragmentActionListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        rootView=inflater.inflate(R.layout.fragment_calculate, container, false);
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rootView.findViewById<ImageView>(R.id.back_button).visibility = View.VISIBLE
        }
        else{
            rootView.findViewById<ImageView>(R.id.back_button).visibility = View.GONE
        }
        val operation = arguments?.getSerializable("operation") as Operation
        if(savedInstanceState == null) {
            val input1 = arguments?.getString("operand1")
            val input2 = arguments?.getString("operand2")
            rootView.findViewById<EditText>(R.id.input1)
                .setText(input1, TextView.BufferType.EDITABLE)
            rootView.findViewById<EditText>(R.id.input2)
                .setText(input2, TextView.BufferType.EDITABLE)
        }else{
            val input1 = savedInstanceState.getString("operand1")
            val input2 = savedInstanceState.getString("operand2")
            rootView.findViewById<EditText>(R.id.input1)
                .setText(input1, TextView.BufferType.EDITABLE)
            rootView.findViewById<EditText>(R.id.input2)
                .setText(input2, TextView.BufferType.EDITABLE)
        }
        if(operation == Operation.DEFAULT){
            rootView.findViewById<EditText>(R.id.input1).isEnabled = false
            rootView.findViewById<EditText>(R.id.input2).isEnabled = false
            rootView.findViewById<Button>(R.id.button).isEnabled = false
            rootView.findViewById<TextView>(R.id.toolbar_title).text = ""
            rootView.findViewById<Button>(R.id.button).text = ""
            rootView.findViewById<EditText>(R.id.input1).setText("",TextView.BufferType.EDITABLE)
            rootView.findViewById<EditText>(R.id.input2).setText("",TextView.BufferType.EDITABLE)
        }
        else{
            rootView.findViewById<TextView>(R.id.toolbar_title).text = operation.name
            rootView.findViewById<Button>(R.id.button).text = operation.name
        }

        rootView.findViewById<Button>(R.id.button).setOnClickListener {
            when(operation){
            Operation.ADD -> calculate(Operation.ADD)
            Operation.SUBTRACT ->calculate(Operation.SUBTRACT)
            Operation.MULTIPLY ->calculate(Operation.MULTIPLY)
            Operation.DIVIDE ->calculate(Operation.DIVIDE)
        } }
        rootView.findViewById<ImageView>(R.id.back_button)
                .setOnClickListener { parentFragmentManager.popBackStack()
                actionListener.selectedOperation(Operation.DEFAULT)}


        return rootView
    }

    private fun calculate(operation : Operation) {

        val operand1 = rootView.findViewById<EditText>(R.id.input1).text.toString()
        val operand2 = rootView.findViewById<EditText>(R.id.input2).text.toString()

        if (operand1.isNotEmpty() && operand2.isNotEmpty()) {
            if (operation == Operation.DIVIDE && operand2.toFloat() == 0.0f) {
                Toast.makeText(
                    activity,
                    getString(R.string.toast_divide_error_message),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val result = when (operation) {
                    Operation.ADD -> operand1.toFloat() + operand2.toFloat()
                    Operation.SUBTRACT -> operand1.toFloat() - operand2.toFloat()
                    Operation.MULTIPLY -> operand1.toFloat() * operand2.toFloat()
                    Operation.DIVIDE -> operand1.toFloat() / operand2.toFloat()
                    Operation.DEFAULT -> 0.0f
                }

                returnResult(operand1, operand2, result, operation)
            }
        }
        else{
            Toast.makeText(context,getString(R.string.valid_input_text), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("operand1",rootView.findViewById<EditText>(R.id.input1).text.toString())
        outState.putString("operand2",rootView.findViewById<EditText>(R.id.input2).text.toString())

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onPause() {
        super.onPause()
        val bundle = Bundle()
        bundle.putString("operand1",rootView.findViewById<EditText>(R.id.input1).text.toString())
        bundle.putString("operand2",rootView.findViewById<EditText>(R.id.input2).text.toString())
        setFragmentResult("CalculateScreenData",bundle)
    }

    private fun returnResult(
        operand1: String,
        operand2: String,
        result: Float,
        operation: Operation
    ) {
        val resultValue = if(result - result.toInt() > 0 )
            result.toString()
        else
            result.toInt().toString()

        setFragmentResult(REQUEST_KEY, bundleOf(RESULT to resultValue , OPERAND1 to operand1,
            OPERAND2 to operand2, OPERATION to operation.name)
        )
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            parentFragmentManager.popBackStack()


    }



}