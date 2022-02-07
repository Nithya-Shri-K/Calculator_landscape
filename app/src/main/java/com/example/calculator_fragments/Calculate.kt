package com.example.calculator_fragments

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.calculator_fragments.databinding.FragmentCalculateBinding


class Calculate : Fragment() {

    lateinit var actionListener: FragmentActionListener
    lateinit var binding : FragmentCalculateBinding
    var operation = Operation.DEFAULT
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalculateBinding.inflate(inflater,container,false)
         operation = arguments?.getSerializable(OPERATION) as Operation
        val input1 = arguments?.getString(OPERAND1)
        val input2 = arguments?.getString(OPERAND2)
        binding.input1.setText(input1, TextView.BufferType.EDITABLE)
        binding.input2.setText(input2, TextView.BufferType.EDITABLE)
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.backButton.visibility = View.VISIBLE
        }
        else{
            binding.backButton.visibility = View.GONE
        }

        if(operation == Operation.DEFAULT){
            disable()
        }
        else{
            binding.toolbarTitle.text = operation.name
            binding.button.text = operation.name
        }

       binding.button.setOnClickListener {
            when(operation){
            Operation.ADD -> calculate(Operation.ADD)
            Operation.SUBTRACT ->calculate(Operation.SUBTRACT)
            Operation.MULTIPLY ->calculate(Operation.MULTIPLY)
            Operation.DIVIDE ->calculate(Operation.DIVIDE)
        } }
        binding.backButton.setOnClickListener {
            disable()
            parentFragmentManager.popBackStack()
            actionListener.selectedOperation(Operation.DEFAULT)
        }
        return binding.root
    }

    private fun calculate(operation : Operation) {

        val operand1 = binding.input1.text.toString()
        val operand2 = binding.input2.text.toString()

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

    override fun onPause() {
        super.onPause()
        val bundle = Bundle()
        bundle.putString(OPERAND1,binding.input1.text.toString())
        bundle.putString(OPERAND2,binding.input2.text.toString())
        setFragmentResult(RESTORE_INPUTS_REQUEST_KEY,bundle)
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
        disable()
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            parentFragmentManager.popBackStack()

    }
    private fun disable(){
        with(binding){
            input1.isEnabled = false
            input2.isEnabled = false
            button.isEnabled = false
            toolbarTitle.text = ""
            button.text = ""
            input1.setText("",TextView.BufferType.EDITABLE)
            input2.setText("",TextView.BufferType.EDITABLE)
        }
    }
}