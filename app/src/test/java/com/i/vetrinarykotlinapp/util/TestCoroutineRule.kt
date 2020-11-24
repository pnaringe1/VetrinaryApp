package com.i.vetrinarykotlinapp.util

import android.os.Parcel
import android.os.Parcelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * A Class which is enables the main dispatcher to use TestCoroutineDispatcher
 * After the test, it resets and cleanup.
 */
@ExperimentalCoroutinesApi
class TestCoroutineRule() : TestRule, Parcelable {

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    constructor(parcel: Parcel) : this()


    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        testCoroutineScope.runBlockingTest { block() }

    override fun apply(base: Statement, description: Description?) = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            Dispatchers.setMain(testCoroutineDispatcher)

            base.evaluate()

            Dispatchers.resetMain()
            testCoroutineScope.cleanupTestCoroutines()
        }
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TestCoroutineRule> {
        override fun createFromParcel(parcel: Parcel): TestCoroutineRule {
            return TestCoroutineRule(parcel)
        }

        override fun newArray(size: Int): Array<TestCoroutineRule?> {
            return arrayOfNulls(size)
        }
    }
}


