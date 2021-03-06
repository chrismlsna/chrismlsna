package com.example.howdyworld

import android.util.Log

class SetsOfIntsUtil {

    //----------------------------------
    //  constants
    //----------------------------------
    /** Label for debug prints */
    private val TAG: String = "SetsOfIntsUtil"
    /** If false, blocks debug prints to Log.d */
    public var debug = false
    /** If false, blocks error prints to Log.d */
    public var printErrors = true
    
    //----------------------------------
    //  data
    //----------------------------------

    //----------------------------------
    //  methods
    //----------------------------------

    /**
     * Finds a single random set of numInts integers which sum to the specified sum.
     * Integers are chosen from the range floor..ceiling, inclusive. Duplicates are allowed.
     * Returns a nullable array of those ints -- which will be null iff it's impossible to 
     * find a set from the given pool.
     *
     * @param sum      The number that the items in the list will add up to.
     * @param numInts  The number of ints in the return list.
     * @param floor    The lowest possible value of any number in the list.
     * @param ceiling  The highest possible value. Must be >= floor.
     *
     * @return  An array of ints that add up to the given sum. If no possible
     *          list exists, then null will be returned.
     */
    public fun findRandomSetOfIntsWithGivenSum(
        sum : Int, numInts: Int, floor: Int, ceiling: Int): Array<Int>? {

        val resultArray = findRandomSetOfIntsWithGivenSumRecurse(sum, numInts, floor, ceiling, "")

        debugPrint("### RESULT: ### array size = " + resultArray.size.toString())
        for (i in resultArray.indices) {
            debugPrint( i.toString() + ": " + resultArray.get(i).toString() )
        }
        debugPrint("RESULT sum = " + resultArray.sum())

        // Return a nullable array -- either the array returned from the recursive routing,
        // or null if that routine returns an empty list, meaning it was given an impossible task.
        var resultArrayNullable : Array<Int>? = null
        if( resultArray.size > 0) {
            resultArrayNullable = resultArray
        }
        return resultArrayNullable
    }

    /***
     * Same as the _AsArray version above, but returns an ArrayList.
     * The list will be empty if the conditions are impossible to satisfy.
     */
    public fun findRandomSetOfIntsWithGivenSumAsList(
        sum : Int, numInts: Int, floor: Int, ceiling: Int): ArrayList<Int> {

        val resultArray : Array<Int>? = findRandomSetOfIntsWithGivenSum(
            sum, numInts, floor, ceiling)
        var resultList : ArrayList<Int> = ArrayList()

        if( resultArray != null)  {
            for (element in resultArray) {
                resultList.add(element)
            }
        }
        return resultList
    }

    /**
     * Same as the public non-recursive function -- except that this
     * returns an empty list rather than null if no set of ints can satisfy the conditions.
     * Also the indent string parameter is added to indent debug prints.
     */
    private fun findRandomSetOfIntsWithGivenSumRecurse(
        sum : Int, numInts: Int, floor: Int, ceiling: Int, indent : String): Array<Int> {

        lateinit var resultArray : Array<Int>   // this will be returned

        debugPrint("M=" + sum.toString() + " N=" + numInts.toString() +
                " R=" + floor.toString() + " S=" + ceiling.toString(), indent )

        // Error case -- numInts < 1
        if (numInts < 1) {
            debugPrint("ERROR: numInts < 1", indent)
            resultArray = arrayOf<Int>()   // we'll return empty array

        // Error case -- floor > ceiling
        } else if ( floor > ceiling ) {
            debugPrint("ERROR: floor > ceiling", indent)
            resultArray = arrayOf<Int>()   // we'll return empty array

        // Error case -- sum is not achievable.
        } else if (sum < numInts * floor || sum > numInts * ceiling) {
            // If we're here, there's no possible set of of numInts ints in floor..ceiling
            // that add up to sum.
            debugPrint("ERROR: No possible set of ints satisfy the conditions.", indent)
            resultArray = arrayOf<Int>()   // we'll return empty array

        // Base case. numInts is 1, so return one int that equals sum.
        // (With the error cases above, sum must be in floor..ceiling!)
        } else if (numInts == 1) {
            debugPrint("base case -- sum = $sum", indent)
            resultArray = Array(1) { i -> sum }   // return an array with one element, sum

        /*
        Final case. Split numInts into ~half .. the first being 1 higher iff numInts is odd.
        Call firstNumInts N1 and secondNumInts N2.
        We're gonna then make two recursive calls based on these first and second N's.
        The idea is that there must be a solution to the given function parameters
        that involves a set of N1 integers that sums to *something* and a second set
        of N2 integers that will, added to it, result in the requested sum.
        */
        } else {
            // Find values for the two new xNumInts which sum to numInts.
            val secondNumInts = numInts / 2
            val firstNumInts = numInts - secondNumInts  // always >= secondNumInts
            debugPrint("N1=$firstNumInts N2=$secondNumInts", indent)

            // Find the possible range of values that the second set could sum to.
            val secondFloor = floor * secondNumInts
            val secondCeiling = ceiling * secondNumInts
            val secondRange = secondFloor..secondCeiling

            // Find the possible range of ints in the first set. Like the second set
            // above, it has high and low limits based simply on the the number of
            // ints and the range of ints we have to choose from.
            // But we make a second restriction using the max() funtion
            // which makes sure the range only includes values that can possibly
            // work with the range of the second summed set.
            val firstFloor = maxOf(floor * firstNumInts, sum - secondCeiling)
            val firstCeiling = minOf(ceiling * firstNumInts, sum - secondFloor)
            val firstRange = firstFloor..firstCeiling

            debugPrint("firstRange $firstRange", indent)
            debugPrint("secondRange $secondRange", indent)

            // We're only looking for a single randomly-chosen solution.
            // So we pick a random value for the firstSum.
            // TODO -- this results in making outlier integers more common in
            // the solution. Imagine our random value lands on the max value --
            // then *all* the integers in the recursive solution for that will
            // be equal to the original function parameter ceiling, and those
            // in the other set will all be floor. So find a way to make middle
            // choices much more likely for numInts more than a handfull, and
            // *especially* when it's way more than a handful. END TODO
            val firstSum = firstRange.random()
            val secondSum = sum - firstSum
            debugPrint("Loop iter M1=$firstSum M2=$secondSum", indent)

            // Recursively obtain a random set of ints for the first set.
            val firstArray = findRandomSetOfIntsWithGivenSumRecurse(
                firstSum, firstNumInts, floor, ceiling, "$indent  "
            )

            // Recursively obtain a random set of ints for the second set.
            val secondArray = findRandomSetOfIntsWithGivenSumRecurse(
                secondSum, secondNumInts, floor, ceiling, "$indent  "
            )

            // Combine the two sets. If either is zero length, there was
            // an error -- so return an empty list.
            if (firstArray.isEmpty() || secondArray.isEmpty()) {
                errorPrint("ERROR: Propagating error up the chain.", indent)
                resultArray = arrayOf<Int>()
            } else {
                resultArray = firstArray + secondArray
            }
        }

        return resultArray
    }  // end recursive function

    /**
    * If the class member var "debug" is true, prints text to Log.d, preceded by the class
    * member constant TAG and a space char. If debug is false, this does nothing.
    * @param text  The string to be printed.
    */
    private fun debugPrint (text : String) {
        if (debug) {
            Log.d(TAG, text)
        }
    }

    /**
     * If the class member var "debug" or "printErrors" is true, prints text to Log.d,
     * preceded by the class member constant TAG and a space char.
     * If debug and printErrors are false, this does nothing.
     * @param text  The string to be printed.
     */
    /**
     * If the class member var "debug" is true, prints text to Log.d, preceded by the class
     * member constant TAG, a space char, and indentChars. If debug is false, this fun does nothing.
     * @param text         The string to be printed.
     * @param indentChars  This string is printed in front of the main text.
     *                     The parameter indentChars is intended to be used as space characters to
     *                     indent the debug prints more and more as we dive into recursive functions.
     */
    private fun debugPrint (text : String, indentChars : String) {
        if (debug) {
            Log.d(TAG, indentChars + text)
        }
    }

    private fun errorPrint (text : String) {
        if (debug || printErrors) {
            debugPrint(text)
        }
    }

    private fun errorPrint( text : String, indentChars : String) {
        if (debug || printErrors) {
            debugPrint( indentChars + text)
        }
    }

} // end of class