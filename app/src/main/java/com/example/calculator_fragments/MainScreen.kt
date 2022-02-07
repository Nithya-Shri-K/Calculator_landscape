package com.example.calculator_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.setFragmentResultListener
import com.example.calculator_fragments.databinding.FragmentMainScreenBinding


class MainScreen : Fragment() {
     lateinit var actionListener : FragmentActionListener
     lateinit var binding : FragmentMainScreenBinding
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         binding = FragmentMainScreenBinding.inflate(inflater,container,false)
        var isResultPage = 0
        var result = ""
        if(savedInstanceState !=null){
            actionListener = activity as MainActivity
            isResultPage = savedInstanceState?.getInt(IS_RESULT_PAGE)
            result = savedInstanceState.getString(RESULT) ?: ""
            if(isResultPage == 1){
                modifyScreenForResult()
                binding.textResult.text = result
            }
        }
         with(binding){
             toolbarTitle.text = "Calculator"
             add.setOnClickListener { actionListener.selectedOperation(Operation.ADD) }
             subtract.setOnClickListener { actionListener.selectedOperation(Operation.SUBTRACT) }
             multiply.setOnClickListener { actionListener.selectedOperation(Operation.MULTIPLY) }
             divide.setOnClickListener { actionListener.selectedOperation(Operation.DIVIDE) }
             buttonReset.setOnClickListener {
                 layoutResult.visibility = View.GONE
                 layoutButtons.visibility = View.VISIBLE
                 textResult.text = ""

             }
         }
        setFragmentResultListener(REQUEST_KEY) { requestKey, bundle ->
            binding.textResult.text = getString(
                R.string.result_text, bundle.getString(RESULT), bundle.getString(
                    OPERAND1
                ), bundle.getString(OPERAND2), bundle.getString(OPERATION)
            )
            modifyScreenForResult()
            actionListener.selectedOperation(Operation.DEFAULT)

        }

         return binding.root
   }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(this::binding.isInitialized && binding.layoutResult.visibility == View.VISIBLE){
            outState.putInt(IS_RESULT_PAGE, 1)
            outState.putString(RESULT, binding.textResult.text.toString())
        }
    }
    private fun modifyScreenForResult() {
        binding.layoutButtons.visibility = View.GONE
        binding.layoutResult.visibility = View.VISIBLE

    }
}