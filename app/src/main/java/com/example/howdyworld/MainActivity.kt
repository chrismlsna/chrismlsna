package com.example.howdyworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var inputViewM: EditText
    private lateinit var inputViewN: EditText
    private lateinit var inputViewR: EditText
    private lateinit var inputViewS: EditText
    private var m: Int = 10
    private var n: Int = 3
    private var r: Int = 1
    private var s: Int = 5
    private var sMinusR: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myButton = findViewById<Button>(R.id.button)

        inputViewM = findViewById(R.id.input_m)
        inputViewN = findViewById(R.id.input_n)
        inputViewR = findViewById(R.id.input_r)
        inputViewS = findViewById(R.id.input_s)
        inputViewM.setText(m.toString())
        inputViewN.setText(n.toString())
        inputViewR.setText(r.toString())
        inputViewS.setText(s.toString())

        myButton.setOnClickListener() {
            val mStr: String = inputViewM.text.toString()
            val nStr: String = inputViewN.text.toString()
            val rStr: String = inputViewR.text.toString()
            val sStr: String = inputViewS.text.toString()

            if (mStr.isEmpty()) { m = 0 } else { m = mStr.toInt() }
            if (nStr.isEmpty()) { n = 0 } else { n = nStr.toInt() }
            if (rStr.isEmpty()) { r = 0 } else { r = rStr.toInt() }
            if (sStr.isEmpty()) { s = 0 } else { s = sStr.toInt() }

            Log.d("onCreate", "calling recursive routine")
            val resultArray : Array<Int>? = findRandomSetOfIntsWithGivenSum(m, n, r, s)

            Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show()
        }
    }

    // returns null if impossible
    fun findRandomSetOfIntsWithGivenSum(sum : Int, numInts: Int, floor: Int, ceiling: Int): Array<Int>? {
        var resultArray = findRandomSetOfIntsWithGivenSumRecurse(sum, numInts, floor, ceiling)

        Log.d("MAIN", "### RESULT: ###")
        for (i in 0 until resultArray.size) {
            Log.d( "MAIN",i.toString() + ": " + resultArray?.get(i).toString() )
        }
        Log.d("MAIN", "RESULT sum = " + resultArray.sum())

        var resultArrayNullable : Array<Int>? = null
        if( resultArray.size > 0) {
            resultArrayNullable = resultArray
        }
        return resultArrayNullable
    }

    private fun findRandomSetOfIntsWithGivenSumRecurse(sum : Int, numInts: Int, floor: Int, ceiling: Int): Array<Int> {
        Log.d("MAIN ", "M=" + sum.toString() + " N=" + numInts.toString() +
                    " R=" + floor.toString() + " S=" + ceiling.toString() )
        lateinit var resultArray : Array<Int>

        if (numInts < 1) {
            Log.d("MAIN ", "ERROR numInts < 1")
            //resultArray = Array(1) { i -> -9999 }
            resultArray = arrayOf<Int>()
        } else if (sum < numInts * r || sum > numInts * s) {
            //resultArray = Array(1) { i -> -9999 }
            resultArray = arrayOf<Int>()
        } else if (numInts == 1) {
            Log.d("MAIN ", "base case")
            resultArray = Array(1) { i -> sum }
        } else {
            // Split numInts into two N's .. the first being 1 higher iff numInts is odd
            val secondNumInts = numInts / 2
            val firstNumInts = numInts - secondNumInts  // always >= secondNumInts

            Log.d(
                "MAIN ",
                "N1=" + firstNumInts.toString() + " N2=" + secondNumInts.toString()
            )
            val secondFloor = floor * secondNumInts
            val secondCeiling = ceiling * secondNumInts
            val firstFloor = maxOf(floor * firstNumInts, sum - secondCeiling)
            val firstCeiling = minOf(ceiling * firstNumInts, sum - secondFloor)
            val firstRange = firstFloor..firstCeiling
            val secondRange = secondFloor..secondCeiling
            Log.d("MAIN ", "firstRange " + firstRange.toString())
            Log.d("MAIN ", "secondRange " + secondRange.toString())

            val firstSum = firstRange.random()
            val secondSum = sum - firstSum
            Log.d(
                "MAIN ","Loop iter M1=" + firstSum.toString() + " M2=" + secondSum.toString()
            )

            val firstArray = findRandomSetOfIntsWithGivenSumRecurse(
                firstSum, firstNumInts, floor, ceiling )

            val secondArray = findRandomSetOfIntsWithGivenSumRecurse(
                secondSum, secondNumInts, floor, ceiling )
            resultArray = firstArray + secondArray
        }
        return resultArray
    }  // end recursive function

    // var foo: SortedSet<Int> = SortedSet()
    //var bar = SortedList<Int>
    //var aaa = ArrayList<Int>()
    //var bbb: ArrayList<Int> = ArrayList()
    // var myArray = Array<Int>(5){0}
    // var mySet = Set<Int>() ... no, it's just an interface
    //var myTreeSet = TreeSet<Int>()
    //var xxx = MutableList<Int>(3)
    var empty = mutableListOf<Int>()
    // TreeSet
    // NavigableSet
    // ConcurrentSkipListSet
}

