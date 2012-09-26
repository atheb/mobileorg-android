package com.matburt.mobileorg.Gui.Theme;

import com.matburt.mobileorg.R;

import android.content.Context;
import android.content.res.Resources;

public class MonoTheme extends DefaultTheme {
	
	public MonoTheme(Context context) {
		super(context);
		setup(context.getResources());
	}
	
	private void setup(Resources r) {
		todoDone = r.getColor(R.color.light_green);
		todoActive = r.getColor(R.color.red);
		priority = r.getColor(R.color.black);
		tags = r.getColor(R.color.black);
		inactive = r.getColor(R.color.black);
		url = r.getColor(R.color.black);
		childIndicator = r.getColor(R.color.black);
		agendaBlocks = r.getColor(R.color.black);
		
		outline_l1 = r.getColor(R.color.black);
		
		levelColors = new int[] { outline_l1 };
	}
}
