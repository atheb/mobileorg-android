package com.matburt.mobileorg.Gui.Theme;

import com.matburt.mobileorg.R;
import com.matburt.mobileorg.util.OrgUtils;

import android.content.Context;
import android.content.res.Resources;

public class DefaultTheme {
		
	public int todoActive;
	public int todoDone;
	public int priority;
	public int tags;
	
	public int inactive;
	public int url;
	
	public int childIndicator;
	
	public int agendaBlocks;
	
	public int[] levelColors;
	public int outline_l1;
	public int outline_l2;
	public int outline_l3;
	public int outline_l4;
	public int outline_l5;
	public int outline_l6;
	public int outline_l7;

	
	public DefaultTheme(Context context) {
		setup(context.getResources());
	}
	
	private void setup(Resources r) {
		todoActive = r.getColor(R.color.red);
		todoDone = r.getColor(R.color.light_green);
		priority = r.getColor(R.color.yellow);
		tags = r.getColor(R.color.gray);
		inactive = r.getColor(R.color.gray);
		url = r.getColor(R.color.dark_blue);
		childIndicator = r.getColor(R.color.white);
		agendaBlocks = r.getColor(R.color.white);
		
		outline_l1 = r.getColor(R.color.blue);
		outline_l2 = r.getColor(R.color.orange);
		outline_l3 = r.getColor(R.color.cyan);
		outline_l4 = r.getColor(R.color.dark_green);
		outline_l5 = r.getColor(R.color.purple);
		outline_l6 = r.getColor(R.color.blue);
		outline_l7 = r.getColor(R.color.dark_green);

		levelColors = new int[] { outline_l1, outline_l2, outline_l3, outline_l4,
				outline_l5, outline_l6, outline_l7 };
	}
	
	
	public static DefaultTheme getTheme(Context context) {
		final String themeName = OrgUtils.getThemeName(context);
		if(themeName.equals("Light"))
				return new WhiteTheme(context);
		else if(themeName.equals("Monochrome"))
			return new MonoTheme(context);
		else
			return new DefaultTheme(context);
	}
}
