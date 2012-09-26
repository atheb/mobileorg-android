package com.matburt.mobileorg.Gui.Theme;

import com.matburt.mobileorg.R;

import android.content.Context;
import android.content.res.Resources;

public class WhiteTheme extends DefaultTheme {

	public WhiteTheme(Context context) {
		super(context);
		setup(context.getResources());
	}
	
	private void setup(Resources r) {
		todoActive = r.getColor(R.color.red);
		todoDone = r.getColor(R.color.light_green);
		priority = r.getColor(R.color.light_gray);
		tags = r.getColor(R.color.gray);
		
		childIndicator = r.getColor(R.color.black);

		outline_l1 = r.getColor(R.color.light_blue);
		outline_l2 = r.getColor(R.color.orange);
		outline_l3 = r.getColor(R.color.turquoise);
		outline_l4 = r.getColor(R.color.dark_green);
		outline_l5 = r.getColor(R.color.dark_purple);
		outline_l6 = r.getColor(R.color.blue);
		outline_l7 = r.getColor(R.color.dark_green);

		levelColors = new int[] { outline_l1, outline_l2, outline_l3,
				outline_l4, outline_l5, outline_l6, outline_l7 };
	}
}
