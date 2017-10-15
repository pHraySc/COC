package org.apache.log4j;

import java.util.*;


class RollingPastCalendar extends RollingCalendar
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	RollingPastCalendar()
	{
	}

	RollingPastCalendar(TimeZone tz, Locale locale)
	{
		super(tz, locale);
	}

	public long getPastCheckMillis(Date now, int maxBackupIndex)
	{
		return getPastDate(now, maxBackupIndex).getTime();
	}

	public Date getPastDate(Date now, int maxBackupIndex)
	{
		setTime(now);
		switch (type)
		{
		case 0: // '\0'
			set(13, get(13));
			set(14, get(14));
			set(12, get(12) - maxBackupIndex);
			break;

		case 1: // '\001'
			set(12, get(12));
			set(13, get(13));
			set(14, get(14));
			set(11, get(11) - maxBackupIndex);
			break;

		case 2: // '\002'
			set(12, get(12));
			set(13, get(13));
			set(14, get(14));
			int hour = get(11);
			if (hour < 12)
				set(11, 12);
			else
				set(11, 0);
			set(5, get(5) - maxBackupIndex);
			break;

		case 3: // '\003'
			set(11, get(11));
			set(12, get(12));
			set(13, get(13));
			set(14, get(14));
			set(5, get(5) - maxBackupIndex);
			break;

		case 4: // '\004'
			set(7, getFirstDayOfWeek());
			set(11, get(11));
			set(12, get(12));
			set(13, get(13));
			set(14, get(14));
			set(3, get(3) - maxBackupIndex);
			break;

		case 5: // '\005'
			set(5, get(5));
			set(11, get(11));
			set(12, get(12));
			set(13, get(13));
			set(14, get(14));
			set(2, get(2) - maxBackupIndex);
			break;

		default:
			throw new IllegalStateException("Unknown periodicity type.");
		}
		return getTime();
	}
}
