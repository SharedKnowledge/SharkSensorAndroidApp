package net.sharksystem.sensor;

import android.content.Context;
import android.graphics.Color;
import android.widget.TableLayout;

import net.sharksystem.SharkException;
import net.sharksystem.sensor.model.AndroidSensorRepository;
import net.sharksystem.sensor.model.SensorDataDbHelper;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static net.sharksystem.sensor.GraphMatchers.withColor;
import static net.sharksystem.sensor.TableMatchers.hasChildPosition;
import static net.sharksystem.sensor.TableMatchers.isInRowBelow;
import static net.sharksystem.sensor.TableMatchers.withIndex;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class ExampleInstrumentedTest {
  double now = System.currentTimeMillis();

  @Before
  public void populateDatabase() throws SharkException {
    System.out.println("Execute populate db");
    AndroidSensorRepository repo = new AndroidSensorRepository(new SensorDataDbHelper(InstrumentationRegistry.getInstrumentation().getTargetContext()));
    repo.dropTable();
    repo.createTable();
    ArrayList<SensorData> list = new ArrayList<>();
    list.add(new SensorData("test-id-1",25.8,Unit.C,32.5,Unit.P,51.1,Unit.P,(System.currentTimeMillis()/1000.0)-97200));
    list.add(new SensorData("test-id-1",25.9,Unit.C,32.4,Unit.P,51.0,Unit.P,(System.currentTimeMillis()/1000.0)-93600));
    list.add(new SensorData("test-id-1",25.8,Unit.C,32.3,Unit.P,50.9,Unit.P,(System.currentTimeMillis()/1000.0)-90000));
    list.add(new SensorData("test-id-1",25.4,Unit.C,32.1,Unit.P,50.7,Unit.P,(System.currentTimeMillis()/1000.0)-88200));
    list.add(new SensorData("test-id-1",25.4,Unit.C,31.9,Unit.P,50.7,Unit.P,(System.currentTimeMillis()/1000.0)-86400));
    list.add(new SensorData("test-id-1",25.1,Unit.C,31.6,Unit.P,50.3,Unit.P,(System.currentTimeMillis()/1000.0)-82800));
    list.add(new SensorData("test-id-1",25.2,Unit.C,31.5,Unit.P,50.0,Unit.P,(System.currentTimeMillis()/1000.0)-79200));
    list.add(new SensorData("test-id-1",24.9,Unit.C,31.3,Unit.P,50.1,Unit.P,(System.currentTimeMillis()/1000.0)-75600));
    list.add(new SensorData("test-id-1",24.8,Unit.C,31.3,Unit.P,49.7,Unit.P,(System.currentTimeMillis()/1000.0)-75600));
    list.add(new SensorData("test-id-1",24.5,Unit.C,31.2,Unit.P,49.5,Unit.P,(System.currentTimeMillis()/1000.0)-72000));
    list.add(new SensorData("test-id-1",24.1,Unit.C,31.0,Unit.P,48.7,Unit.P,(System.currentTimeMillis()/1000.0)-68400));
    list.add(new SensorData("test-id-1",24.0,Unit.C,30.8,Unit.P,48.7,Unit.P,(System.currentTimeMillis()/1000.0)-64800));
    list.add(new SensorData("test-id-1",23.8,Unit.C,30.8,Unit.P,48.1,Unit.P,(System.currentTimeMillis()/1000.0)-61200));
    list.add(new SensorData("test-id-1",23.8,Unit.C,30.7,Unit.P,47.9,Unit.P,(System.currentTimeMillis()/1000.0)-57600));
    list.add(new SensorData("test-id-1",23.6,Unit.C,30.4,Unit.P,47.8,Unit.P,(System.currentTimeMillis()/1000.0)-54000));
    list.add(new SensorData("test-id-1",23.5,Unit.C,30.4,Unit.P,47.9,Unit.P,(System.currentTimeMillis()/1000.0)-50400));
    list.add(new SensorData("test-id-2",23.3,Unit.C,71.1,Unit.P,42.2,Unit.P,(System.currentTimeMillis()/1000.0)-46800));
    list.add(new SensorData("test-id-3",-42.6,Unit.C,55.4,Unit.P,43.1,Unit.P,(System.currentTimeMillis()/1000.0)));
    list.add(new SensorData("test-id-2",12.4,Unit.C,63.9,Unit.P,43.8,Unit.P,(System.currentTimeMillis()/1000.0)));
    list.add(new SensorData("test-id-1",23.9,Unit.C,29.7,Unit.P,48.1,Unit.P,(System.currentTimeMillis()/1000.0)-43200));
    list.add(new SensorData("test-id-1",23.6,Unit.C,28.9,Unit.P,48.3,Unit.P,(System.currentTimeMillis()/1000.0)-39600));
    list.add(new SensorData("test-id-1",23.3,Unit.C,28.7,Unit.P,48.1,Unit.P,(System.currentTimeMillis()/1000.0)-36000));
    list.add(new SensorData("test-id-1",23.1,Unit.C,64.7,Unit.P,53.3,Unit.P,(System.currentTimeMillis()/1000.0)-32400));
    list.add(new SensorData("test-id-1",22.9,Unit.C,64.5,Unit.P,54.0,Unit.P,(System.currentTimeMillis()/1000.0)-28800));
    list.add(new SensorData("test-id-1",22.7,Unit.C,64.5,Unit.P,53.9,Unit.P,(System.currentTimeMillis()/1000.0)-25200));
    list.add(new SensorData("test-id-1",22.8,Unit.C,64.4,Unit.P,56.1,Unit.P,(System.currentTimeMillis()/1000.0)-21600));
    list.add(new SensorData("test-id-1",23.1,Unit.C,64.3,Unit.P,53.6,Unit.P,(System.currentTimeMillis()/1000.0)-18000));
    list.add(new SensorData("test-id-1",23.2,Unit.C,64.2,Unit.P,51.8,Unit.P,(System.currentTimeMillis()/1000.0)-14400));
    list.add(new SensorData("test-id-1",23.2,Unit.C,64.2,Unit.P,52.2,Unit.P,(System.currentTimeMillis()/1000.0)-10800));
    list.add(new SensorData("test-id-1",23.1,Unit.C,63.9,Unit.P,53.2,Unit.P,(System.currentTimeMillis()/1000.0)-7200));
    list.add(new SensorData("test-id-1",23.3,Unit.C,63.7,Unit.P,52.4,Unit.P,(System.currentTimeMillis()/1000.0)-3600));
    list.add(new SensorData("test-id-1",22.1,Unit.C,63.7,Unit.P,52.3,Unit.P,(now/1000.0)));
    repo.insertNewEntries(list);
  }

  @Rule public ActivityScenarioRule<ASAPInitialActivity> activityScenarioRule
    = new ActivityScenarioRule<ASAPInitialActivity>(ASAPInitialActivity.class);
  @Test
  public void tableView_test(){
    Date date = new Date((long) now);
    String dateString = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);

    String selectionText = "test-id-1";
    onView(withId(R.id.BNs_spinner)).perform(click());
    onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
    onView(withId(R.id.table_button)).perform(click());
    onView(
      allOf(isDescendantOfA(isAssignableFrom(TableLayout.class)),
        withIndex(isInRowBelow(withText("ID")),0))
    ).check(matches(withText("test-id-1")));
    onView(
      allOf(isDescendantOfA(isAssignableFrom(TableLayout.class)),
        withIndex(isInRowBelow(withText("ID")),1))
    ).check(matches(withText(dateString)));
    onView(
      allOf(isDescendantOfA(isAssignableFrom(TableLayout.class)),
        withIndex(isInRowBelow(withText("ID")),2))
    ).check(matches(withText("22.1°C")));
    onView(
      allOf(isDescendantOfA(isAssignableFrom(TableLayout.class)),
        withIndex(isInRowBelow(withText("ID")),3))
    ).check(matches(withText("63.7%")));
    onView(
      allOf(isDescendantOfA(isAssignableFrom(TableLayout.class)),
        withIndex(isInRowBelow(withText("ID")),4))
    ).check(matches(withText("52.3%")));
  }

  @Test
  public void graphView_test(){
    String selected = "#006600";
    String notSelected = "#7CB342";
    DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    Date date = new Date();
    String dateString = format.format(date);
    //select graphView with data for id "test-id-1"
    String selectionText = "test-id-1";
    onView(withId(R.id.BNs_spinner)).perform(click());
    onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
    onView(withId(R.id.graph_button)).perform(click());
    //check if current date is displayed
    withId(R.id.dateText).matches(withText(dateString));
    //In GraphView check if humidity is currently selected
    onView(withId(R.id.btn_humidity))
      .check(matches(withColor(Color.parseColor(selected))));
    //check that temperature button is not selected
    onView(withId(R.id.btn_temperature))
      .check(matches(withColor(Color.parseColor(notSelected))));
    //perform click on temperature button
    onView(withId(R.id.btn_temperature)).perform(click());
    //check that humidity button is not selected anymore
    onView(withId(R.id.btn_humidity))
      .check(matches(withColor(Color.parseColor(notSelected))));
    //and that instead temperature button is selected now
    onView(withId(R.id.btn_temperature))
      .check(matches(withColor(Color.parseColor(selected))));
    //perform click on back-button
    onView(withId(R.id.btn_back)).perform(click());
    //check if date of yesterday is displayed
    withId(R.id.dateText).matches(withText(getDateStringOfYesterday()));
    //check that temperature button is still selected
    onView(withId(R.id.btn_temperature))
      .check(matches(withColor(Color.parseColor(selected))));
    //check that humidity is still not selected
    onView(withId(R.id.btn_humidity))
      .check(matches(withColor(Color.parseColor(notSelected))));
  }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("net.sharksystem.sensor", appContext.getPackageName());
    }
    private String getDateStringOfYesterday(){
    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE,-1);
    Date date = cal.getTime();
    DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    return format.format(date);
    }
}
