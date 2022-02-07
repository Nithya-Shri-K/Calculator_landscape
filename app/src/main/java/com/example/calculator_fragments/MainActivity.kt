package com.example.calculator_fragments

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.example.calculator_fragments.databinding.ActivityMainBinding

const val IS_RESULT_PAGE = "Is_Result_Page"
const val RESULT = "result"
const val REQUEST_KEY = "requestKey"
const val OPERAND1 = "operand1"
const val OPERAND2 = "operand2"
const val OPERATION = "operation"
const val RESTORE_INPUTS_REQUEST_KEY = "restore_input_request_key"


class MainActivity : AppCompatActivity(),FragmentActionListener{
    lateinit var binding : ActivityMainBinding
    private var operation = Operation.DEFAULT
    private var input1 = ""
    private var input2 = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState != null ){
            input1 = savedInstanceState.getString(OPERAND1).toString()
            input2 = savedInstanceState.getString(OPERAND2).toString()
            operation = savedInstanceState.getSerializable(OPERATION) as Operation

        }
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (savedInstanceState == null) {
                addMainScreenFragment()
            }
            else{
                if(operation != Operation.DEFAULT ){
                    addCalculateFragment(operation, R.id.fragment_container,0)
                }else if(operation == Operation.DEFAULT){
                    supportFragmentManager.popBackStack()
                }
            }
        } else if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){

            if(savedInstanceState == null){
                addMainScreenFragment()
                addCalculateFragment(operation, R.id.fragment_container_calculate,0)
            }
            else{
                addCalculateFragment(operation, R.id.fragment_container_calculate,0)
            }
        }

        supportFragmentManager.setFragmentResultListener(RESTORE_INPUTS_REQUEST_KEY,this) { requestKey, bundle ->
           input1 = bundle.getString(OPERAND1).toString()
            input2 = bundle.getString(OPERAND2).toString()

        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(OPERATION,operation)
        outState.putString(OPERAND1,input1)
        outState.putString(OPERAND2,input2)
    }

    override fun selectedOperation(operationSelected: Operation) {
        operation = operationSelected
       if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT && operationSelected != Operation.DEFAULT)
            addCalculateFragment(operation, R.id.fragment_container,1)
        else if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                addCalculateFragment(operation,R.id.fragment_container_calculate,1)

    }
    override fun onBackPressed() {

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(supportFragmentManager.backStackEntryCount >=1){
                supportFragmentManager.popBackStack()
            }
            finish()
        }
        else{
            super.onBackPressed()
            input1 =""
            input2 =""
        }
    }
    private fun addMainScreenFragment(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val mainScreenFragment = MainScreen()
        mainScreenFragment.actionListener=this
        fragmentTransaction.add(R.id.fragment_container, mainScreenFragment)
        fragmentTransaction.commit()
    }
    private fun addCalculateFragment(operationToPerform: Operation,container: Int,flag : Int){
        if(supportFragmentManager.backStackEntryCount >= 1) {
            supportFragmentManager.popBackStack()
        }
        if(flag==1){
            input1 = ""
            input2 = ""
        }
        val bundle = Bundle()
        bundle.putSerializable(OPERATION,operationToPerform)
        bundle.putString(OPERAND1,input1)
        bundle.putString(OPERAND2,input2)
        val calculateFragment = Calculate()
        calculateFragment.arguments=bundle
        calculateFragment.actionListener = this
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(container, calculateFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    }
