package net.sharksystem.sensor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class GraphMatchers {
  static Matcher<View> withColor(final int color) {
    return new TypeSafeMatcher<View>() {
      @Override
      protected boolean matchesSafely(View view) {
        Button btn = (Button)view;
        ColorDrawable buttonColor = (ColorDrawable)btn.getBackground();
        int colorId = buttonColor.getColor();
        return colorId == color;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("with color: ");
        description.appendValue(color);
      }
    };
  }
}
