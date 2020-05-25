package com.manugarcia010.timeselectorview

import android.app.Activity
import android.os.Build
import android.util.Xml
import android.widget.ImageButton
import android.widget.TextView
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
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
class UnitSelectorViewTest {

    //This is necessary to solve a problem using mockito and robolectric
    private interface OnUnitChangedListenerForTest : (Int) -> Unit

    @Mock
    private lateinit var listener: OnUnitChangedListenerForTest

    private lateinit var activityController: ActivityController<Activity>
    private lateinit var activity: Activity

    private lateinit var unitSelectorView: UnitSelectorView

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
        unitSelectorView = UnitSelectorView(activity, attr)
        unitSelectorView.setOnUnitChangedListener(listener)

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
        unitSelectorView = UnitSelectorView(activity, attr)
        assertEquals(activity.resources.getString(R.string.test_label),
            unitSelectorView.findViewById<TextView>(R.id.unit_label).text)

    }

    @Test
    fun `enter unit bigger than 99, truncate it to 2 digits`() {
        val unitInput = unitSelectorView.findViewById<TextView>(R.id.unit_input)
        unitInput.requestFocus() //to simulate a user interaction
        unitInput.text = "1099"
        assertEquals("10", unitInput.text.toString())
    }

    @Test
    fun `with unit set to 0, the view shows 1`() {
        val unitInput = unitSelectorView.findViewById<TextView>(R.id.unit_input)
        unitInput.requestFocus() //to simulate a user interaction
        unitInput.text = "0"
        assertEquals("1",unitInput.text.toString())
    }

    @Test
    fun `with unit set to 99, decrease time works`() {
        val unitInput = unitSelectorView.findViewById<TextView>(R.id.unit_input)
        val decreaseBtn = unitSelectorView.findViewById<ImageButton>(R.id.btn_decrease_unit)
        unitInput.requestFocus() //to simulate a user interaction
        unitInput.text = "99"
        decreaseBtn.performClick()
        assertEquals("98",unitInput.text.toString())
    }

    @Test
    fun `with unit set to 1, increase time works`() {
        val unitInput = unitSelectorView.findViewById<TextView>(R.id.unit_input)
        val increaseBtn = unitSelectorView.findViewById<ImageButton>(R.id.btn_increase_unit)
        unitInput.requestFocus() //to simulate a user interaction
        unitInput.text = "1"
        increaseBtn.performClick()
        assertEquals("2",unitInput.text.toString())
    }

    @Test
    fun `with unit set to 99, increase time fails`() {
        val unitInput = unitSelectorView.findViewById<TextView>(R.id.unit_input)
        val increaseBtn = unitSelectorView.findViewById<ImageButton>(R.id.btn_increase_unit)
        unitInput.requestFocus() //to simulate a user interaction
        unitInput.text = "99"
        increaseBtn.performClick()
        assertEquals("99",unitInput.text.toString())
    }

    @Test
    fun `with unit set to 1, decrease time fails`() {
        val unitInput = unitSelectorView.findViewById<TextView>(R.id.unit_input)
        val decreaseBtn = unitSelectorView.findViewById<ImageButton>(R.id.btn_decrease_unit)
        unitInput.requestFocus() //to simulate a user interaction
        unitInput.text = "1"
        decreaseBtn.performClick()
        assertEquals("1",unitInput.text.toString())
    }

    @Test
    fun `when decrease button is pressed, the listener is called`() {
        val decreaseBtn = unitSelectorView.findViewById<ImageButton>(R.id.btn_decrease_unit)
        decreaseBtn.performClick()
        verify(listener).invoke(any())
    }

    @Test
    fun `when increase button is pressed, the listener is called`() {
        val increaseBtn = unitSelectorView.findViewById<ImageButton>(R.id.btn_increase_unit)
        increaseBtn.performClick()
        verify(listener).invoke(any())
    }

    @Test
    fun `when unit input changes, the listener is called`() {
        val unitInput = unitSelectorView.findViewById<TextView>(R.id.unit_input)
        unitInput.requestFocus() //to simulate a user interaction
        unitInput.text = "10"
        verify(listener).invoke(any())
    }

}
