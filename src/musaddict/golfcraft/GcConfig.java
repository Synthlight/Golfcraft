package musaddict.golfcraft;


public class GcConfig {

	static Golfcraft plugin;


	public static int sandChance() {
		int Min = plugin.getConfig().getInt("sandtrap-min-percent-decrease");
		int Max = plugin.getConfig().getInt("sandtrap-max-percent-decrease");
		if(Max < 1)
			Max = 1;
		if(Min < 0)
			Min = 0;
		if (Min >= Max)
			Min = Max - 1;
		int Variation = Max - Min;
		return Variation;
	}


	public static int sandBase() {
		int Max = plugin.getConfig().getInt("sandtrap-max-percent-decrease");
		if(Max < 0)
			Max = 0;
		if(Max > 99)
			Max = 99;
		int Base = 100 - Max;
		return Base;
	}


	public static int roughChance() {
		int Min = plugin.getConfig().getInt("rough-min-percent-decrease");
		int Max = plugin.getConfig().getInt("rough-max-percent-decrease");
		if(Max < 1)
			Max = 1;
		if(Min < 0)
			Min = 0;
		if (Min >= Max)
			Min = Max - 1;
		int Variation = Max - Min;
		return Variation;
	}


	public static int roughBase() {
		int Min = plugin.getConfig().getInt("rough-min-percent-decrease");
		if(Min < 0)
			Min = 0;
		if(Min > 99)
			Min = 99;
		int Base = 100 - Min;
		return Base;
	}


	public static int teeingGroundChance() {
		int Min = plugin.getConfig().getInt("teeing-ground-min-percent-increase");
		int Max = plugin.getConfig().getInt("teeing-ground-max-percent-increase");
		if(Max < 1)
			Max = 1;
		if(Min < 0)
			Min = 0;
		if (Min >= Max)
			Min = Max - 1;
		int Variation = Max - Min;
		return Variation;
	}


	public static int teeingGroundBase() {
		int Min = plugin.getConfig().getInt("teeing-ground-min-percent-decrease");
		if(Min < 0)
			Min = 0;
		if(Min > 99)
			Min = 99;
		int Base = 100 - Min;
		return Base;
	}

}
