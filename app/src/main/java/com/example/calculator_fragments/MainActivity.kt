package com.example.calculator_fragments

import android.content.res.Configuration
import android.graphics.Path
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener

import kotlin.properties.Delegates

const val IS_RESULT_PAGE = "Is_Result_Page"
const val RESULT = "result"
const val REQUEST_KEY = "requestKey"
const val OPERAND1 = "operand1"
const val OPERAND2 = "operand2"
const val OPERATION = "operation"


class MainActivity : AppCompatActivity(),FragmentActionListener{
    private var operation = Operation.DEFAULT
    var finalResult = ""
    var state=1
    var input1 = ""
    var input2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState != null ){
            input1 = savedInstanceState.getString("operand1").toString()
            input2 = savedInstanceState.getString("operand2").toString()
        }
        if(findViewById<LinearLayout>(R.id.portrait_layout) != null) {
            if (savedInstanceState == null) {
                addMainScreenFragment()
            }
            else{
                if(savedInstanceState.getSerializable("operation") as Operation != Operation.DEFAULT  ){
                    addCalculateFragment(savedInstanceState.getSerializable("operation") as Operation)
                }

            }
        } else if(findViewById<LinearLayout>(R.id.landscape_layout) != null){

            if(savedInstanceState == null){
                addMainScreenFragment()
                addCalculateFragment(operation, R.id.fragment_container_calculate)
            }
            else{
//                if(savedInstanceState.getInt("state") !=2)
                    //addMainScreenFragment()
                addCalculateFragment(savedInstanceState.getSerializable("operation") as Operation, R.id.fragment_container_calculate)
            }


        }
        supportFragmentManager.setFragmentResultListener("CalculateScreenData",this) { requestKey, bundle ->
           input1 = bundle.getString("operand1").toString()
            input2 = bundle.getString("operand2").toString()

        }
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operation = savedInstanceState.getSerializable("operation") as Operation
        state = savedInstanceState.getInt("state")


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("operation",operation)
        outState.putInt("state",state)
        outState.putString("operand1",input1)
        outState.putString("operand2",input2)

    }

    override fun selectedOperation(operationSelected: Operation) {
        operation = operationSelected
       if(findViewById<LinearLayout>(R.id.landscape_layout)==null && operationSelected != Operation.DEFAULT)
            addCalculateFragment(operation)
        else if(findViewById<LinearLayout>(R.id.portrait_layout)==null)
                addCalculateFragment(operation,R.id.fragment_container_calculate)

    }

    override fun isResultScreen(state: Int) {
        this.state = state
        if(state==1){
            input1=""
            input2=""
        }
    }

    private fun addMainScreenFragment(){
        Toast.makeText(this,"MainScreen calleddd",Toast.LENGTH_SHORT).show()
//        val f = supportFragmentManager.findFragmentByTag("main_screen")
        val fragmentTransaction = supportFragmentManager.beginTransaction()
//        if(f !=null) {
//            fragmentTransaction.remove(f)
//            Toast.makeText(this,"removed f",Toast.LENGTH_SHORT).show()
//        }
        val mainScreenFragment = MainScreen()
        mainScreenFragment.actionListener=this
        fragmentTransaction.add(R.id.fragment_container, mainScreenFragment,"main_screen")
        fragmentTransaction.commit()
    }
        private fun addCalculateFragment(operation: Operation, container: Int){
            if(supportFragmentManager.backStackEntryCount >= 1) {
                supportFragmentManager.popBackStack()
            }
        val bundle = Bundle()
        bundle.putSerializable(OPERATION,operation)
            bundle.putString("operand1",input1)
            bundle.putString("operand2",input2)

        val calculateFragment = Calculate()
        calculateFragment.arguments=bundle
            val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(container, calculateFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun addCalculateFragment(operation: Operation){
        if(supportFragmentManager.backStackEntryCount>=1)
            supportFragmentManager.popBackStack()
        val bundle = Bundle()
        bundle.putSerializable(OPERATION,operation)
        bundle.putString("operand1",input1)
        bundle.putString("operand2",input2)
        val calculateFragment = Calculate()
        calculateFragment.arguments=bundle
        calculateFragment.actionListener = this
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, calculateFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    }
