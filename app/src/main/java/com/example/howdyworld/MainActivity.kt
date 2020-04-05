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
            val util = SetsOfIntsUtil()
            util.debug = true
            val resultArray : Array<Int>? = util.findRandomSetOfIntsWithGivenSum(m, n, r, s)

            Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show()
        }
    }

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

