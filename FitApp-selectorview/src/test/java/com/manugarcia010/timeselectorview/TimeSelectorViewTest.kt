package com.manugarcia010.timeselectorview

import android.app.Activity
import android.os.Build
import android.util.Xml
import android.widget.ImageButton
import android.widget.TextView
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.xmlpull.v1.XmlPullParser


@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class TimeSelectorViewTest {

    //This is necessary to solve a problem using mockito and robolectric
    private interface OnTimeChangedListenerForTest : (Int) -> Unit

    @Mock
    private lateinit var listener: OnTimeChangedListenerForTest

    private lateinit var activityController: ActivityController<Activity>
    private lateinit var activity: Activity

    private lateinit var timeSelectorView : TimeSelectorView

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        // Create an activity (Can be any sub-class: i.e. AppCompatActivity, FragmentActivity, etc)
        activityController = Robolectric.buildActivity(Activity::class.java)
        activity = activityController.get()

        // Get the view attr from a test xml
        val parser: XmlPullParser = activity.resources.getXml(R.xml.test)
        try {
            parser.next()
            parser.nextTag()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val attr = Xml.asAttributeSet(parser)

        // Create the view using the activity context and attr and set the listener
        timeSelectorView = TimeSelectorView(activity,attr)
        timeSelectorView.setOnTimeChangedListener(listener)

    }

    @Test
    fun `set label attr show attr text in the label`() {
        val parser: XmlPullParser = activity.resources.getXml(R.xml.test)
        try {
            parser.next()
            parser.nextTag()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val attr = Xml.asAttributeSet(parser)

        // Create the view using the activity context and attr
        timeSelectorView = TimeSelectorView(activity, attr)
        assertEquals(activity.resources.getString(R.string.test_label),
            timeSelectorView.findViewById<TextView>(R.id.time_label).text)

    }

    @Test
    fun `enter minutes bigger than 99 truncate it to 2 digits`() {
        val minutesInput = timeSelectorView.findViewById<TextView>(R.id.time_minutes_input)
        minutesInput.requestFocus() //to simulate a user interaction
        minutesInput.text = "1099"
        assertEquals("10", minutesInput.text.toString())
    }

    @Test
    fun `enter seconds bigger than 59 update the minutes and seconds views accordingly`() {
        val minutesInput = timeSelectorView.findViewById<TextView>(R.id.time_minutes_input)
        val secondsInput = timeSelectorView.findViewById<TextView>(R.id.time_seconds_input)
        secondsInput.requestFocus() //to simulate a user interaction
        secondsInput.text = "99"
        assertEquals("1",minutesInput.text.toString())
        assertEquals("39",secondsInput.text.toString())
    }

    @Test
    fun `with time set to 0 minutes 0 seconds the view show 1 second`() {
        val minutesInput = timeSelectorView.findViewById<TextView>(R.id.time_minutes_input)
        val secondsInput = timeSelectorView.findViewById<TextView>(R.id.time_seconds_input)
        minutesInput.requestFocus() //to simulate a user interaction
        minutesInput.text = "0"
        secondsInput.requestFocus() //to simulate a user interaction
        secondsInput.text = "0"
        assertEquals("0",minutesInput.text.toString())
        assertEquals("1",secondsInput.text.toString())
    }

    @Test
    fun `with time set to 1 minutes 0 seconds the view show 1 minutes and 0 second`() {
        val minutesInput = timeSelectorView.findViewById<TextView>(R.id.time_minutes_input)
        val secondsInput = timeSelectorView.findViewById<TextView>(R.id.time_seconds_input)
        minutesInput.requestFocus() //to simulate a user interaction
        minutesInput.text = "1"
        secondsInput.requestFocus() //to simulate a user interaction
        secondsInput.text = "0"
        assertEquals("1",minutesInput.text.toString())
        assertEquals("0",secondsInput.text.toString())
    }

    @Test
    fun `with time set to 99 minutes 59 seconds decrease time works`() {
        val minutesInput = timeSelectorView.findViewById<TextView>(R.id.time_minutes_input)
        val secondsInput = timeSelectorView.findViewById<TextView>(R.id.time_seconds_input)
        val decreaseBtn = timeSelectorView.findViewById<ImageButton>(R.id.btn_decrease_time)
        minutesInput.requestFocus() //to simulate a user interaction
        minutesInput.text = "99"
        secondsInput.requestFocus() //to simulate a user interaction
        secondsInput.text = "59"
        decreaseBtn.performClick()
        assertEquals("99",minutesInput.text.toString())
        assertEquals("58",secondsInput.text.toString())
    }

    @Test
    fun `with time set to 0 minutes 1 seconds increase time works`() {
        val minutesInput = timeSelectorView.findViewById<TextView>(R.id.time_minutes_input)
        val secondsInput = timeSelectorView.findViewById<TextView>(R.id.time_seconds_input)
        val increaseBtn = timeSelectorView.findViewById<ImageButton>(R.id.btn_increase_time)
        minutesInput.requestFocus() //to simulate a user interaction
        minutesInput.text = "0"
        secondsInput.requestFocus() //to simulate a user interaction
        secondsInput.text = "1"
        increaseBtn.performClick()
        assertEquals("0",minutesInput.text.toString())
        assertEquals("2",secondsInput.text.toString())
    }

    @Test
    fun `with time set to 99 minutes 59 seconds increase time fails`() {
        val minutesInput = timeSelectorView.findViewById<TextView>(R.id.time_minutes_input)
        val secondsInput = timeSelectorView.findViewById<TextView>(R.id.time_seconds_input)
        val increaseBtn = timeSelectorView.findViewById<ImageButton>(R.id.btn_increase_time)
        secondsInput.requestFocus() //to simulate a user interaction
        secondsInput.text = "59"
        minutesInput.requestFocus() //to simulate a user interaction
        minutesInput.text = "99"
        increaseBtn.performClick()
        assertEquals("99",minutesInput.text.toString())
        assertEquals("59",secondsInput.text.toString())
    }

    @Test
    fun `with time set to 0 minutes 1 seconds decrease time fails`() {
        val minutesInput = timeSelectorView.findViewById<TextView>(R.id.time_minutes_input)
        val secondsInput = timeSelectorView.findViewById<TextView>(R.id.time_seconds_input)
        val decreaseBtn = timeSelectorView.findViewById<ImageButton>(R.id.btn_decrease_time)
        minutesInput.requestFocus() //to simulate a user interaction
        minutesInput.text = "0"
        secondsInput.requestFocus() //to simulate a user interaction
        secondsInput.text = "1"
        decreaseBtn.performClick()
        assertEquals("0",minutesInput.text.toString())
        assertEquals("1",secondsInput.text.toString())
    }

    @Test
    fun `when decrease button is pressed the listener is called`() {
        val decreaseBtn = timeSelectorView.findViewById<ImageButton>(R.id.btn_decrease_time)
        decreaseBtn.performClick()
        verify(listener).invoke(any())
    }

    @Test
    fun `when increase button is pressed the listener is called`() {
        val increaseBtn = timeSelectorView.findViewById<ImageButton>(R.id.btn_increase_time)
        increaseBtn.performClick()
        verify(listener).invoke(any())
    }

    @Test
    fun `when minutes input changes the listener is called`() {
        val minutesInput = timeSelectorView.findViewById<TextView>(R.id.time_minutes_input)
        minutesInput.requestFocus() //to simulate a user interaction
        minutesInput.text = "10"
        verify(listener).invoke(any())
    }

    @Test
    fun `when seconds input changes the listener is called`() {
        val secondsInput = timeSelectorView.findViewById<TextView>(R.id.time_seconds_input)
        secondsInput.requestFocus() //to simulate a user interaction
        secondsInput.text = "10"
        verify(listener).invoke(any())
    }

}
