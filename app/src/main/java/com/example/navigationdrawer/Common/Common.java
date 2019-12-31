package com.example.navigationdrawer.Common;

public class Common {
    public static final int TIME_SLOT_TOTAL = 16;
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static  final Object DISABLE_TAG = "DISABLE";

    public static String convertTimeSlotToString(int slot) {

        switch (slot)
        {
            case 0:
                return  "9:00-11:00";
            case 1:
                return  "9:00-11:00";
            case 2:
                return  "9:00-11:00";
            case 3:
                return  "9:00-11:00";
            case 4:
                return "11:00- 13:00";
            case 5:
                return "11:00- 13:00";
            case 6:
                return "11:00- 13:00";
            case 7:
                return "11:00- 13:00";
            case 8:
                return "14:00- 16:00";
            case 9:
                return "14:00- 16:00";
            case 10:
                return "14:00- 16:00";
            case 11:
                return "14:00- 16:00";
            case 12:
                return "16:00- 18:00";
            case 13:
                return "16:00- 18:00";
            case 14:
                return "16:00- 18:00";
            case 15:
                return "16:00- 18:00";
                default:
                    return  "Closed";
        }
    }
}
