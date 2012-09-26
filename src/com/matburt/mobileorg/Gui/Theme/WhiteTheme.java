package com.matburt.mobileorg.Gui.Theme;

import com.matburt.mobileorg.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

public class WhiteTheme extends DefaultTheme {

	public WhiteTheme(Context context) {
		super(context);
		setup(context.getResources());
	}
	
	private void setup(Resources r) {
		todoActive = Color.rgb(0x20, 0x00, 0x00);
		todoDone = Color.rgb(0x00, 0x20, 0x00);
		priority = Color.rgb(0x80, 0x80, 0x00);
		
		childIndicator = r.getColor(R.color.black);

		outline_l1 = Color.rgb(0x00, 0x00, 0x80);
		outline_l2 = Color.rgb(0xc0, 0x80, 0x00);
		outline_l3 = Color.rgb(0x00, 0x80, 0x80);
		outline_l4 = Color.rgb(0x00, 0xa0, 0x00);
		outline_l5 = Color.rgb(0x20, 0x00, 0x20);
		outline_l6 = Color.rgb(0x00, 0x00, 0x80);
		outline_l7 = Color.rgb(0x00, 0xa0, 0x00);

		levelColors = new int[] { outline_l1, outline_l2, outline_l3,
				outline_l4, outline_l5, outline_l6, outline_l7 };
	}
}
