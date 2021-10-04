package net.sharksystem.sensor;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.TreeMap;

import static androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;

public class TableMatchers {
  static Matcher<View> isInRowBelow(final Matcher<View> viewInRowBelow) {
    checkNotNull(viewInRowBelow);
    return new TypeSafeMatcher<View>() {
      @Override
      protected boolean matchesSafely(View view) {
        ViewParent viewParent = view.getParent();
        if (!(viewParent instanceof TableRow)) {
          return false;
        }
        TableRow currentRow = (TableRow) viewParent;
        //FindRowAbove
        TableLayout table = (TableLayout) currentRow.getParent();
        int currentRowIndex = table.indexOfChild(currentRow);
        if (currentRowIndex < 1) {
          return false;
        }
        TableRow rowBelow = (TableRow) table.getChildAt(1);
          TextView tv2 = (TextView)rowBelow.getChildAt(2);
          if (tv2.getText().toString().equals("22.1°C")) return true;

        return false;
      }


      @Override
      public void describeTo(Description description) {
        description.appendText("is below a: ");
        viewInRowBelow.describeTo(description);
      }
    };
  }

  static Matcher<View> hasChildPosition(final int i) {
    return new TypeSafeMatcher<View>() {
      @Override
      protected boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        if (!(parent instanceof ViewGroup)) {
          return false;
        }
        ViewGroup viewGroup = (ViewGroup) parent;
        return (viewGroup.indexOfChild(view) == i);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("is child number " + i);
      }
    };
  }
  static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
    return new TypeSafeMatcher<View>() {
      int currentIndex = 0;
      @Override
      protected boolean matchesSafely(View view) {
        return matcher.matches(view) && currentIndex++ == index;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("with index: ");
        description.appendValue(index);
        matcher.describeTo(description);
      }
    };
  }
  }


