package android.text.format;

import android.content.Context;
import android.content.res.Resources;
import android.net.NetworkUtils;
import android.text.BidiFormatter;
import android.text.TextUtils;
import com.android.internal.R;

public final class Formatter {
    public static final int FLAG_CALCULATE_ROUNDED = 2;
    public static final int FLAG_SHORTER = 1;
    private static final int MILLIS_PER_MINUTE = 60000;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;

    public static class BytesResult {
        public final long roundedBytes;
        public final String units;
        public final String value;

        public BytesResult(String value, String units, long roundedBytes) {
            this.value = value;
            this.units = units;
            this.roundedBytes = roundedBytes;
        }
    }

    private static String bidiWrap(Context context, String source) {
        if (TextUtils.getLayoutDirectionFromLocale(context.getResources().getConfiguration().locale) == 1) {
            return BidiFormatter.getInstance(true).unicodeWrap(source);
        }
        return source;
    }

    public static String formatFileSize(Context context, long sizeBytes) {
        if (context == null) {
            return "";
        }
        BytesResult res = formatBytes(context.getResources(), sizeBytes, 0);
        return bidiWrap(context, context.getString(R.string.fileSizeSuffix, new Object[]{res.value, res.units}));
    }

    public static String formatShortFileSize(Context context, long sizeBytes) {
        if (context == null) {
            return "";
        }
        BytesResult res = formatBytes(context.getResources(), sizeBytes, 1);
        return bidiWrap(context, context.getString(R.string.fileSizeSuffix, new Object[]{res.value, res.units}));
    }

    public static BytesResult formatBytes(Resources res, long sizeBytes, int flags) {
        int roundFactor;
        String roundFormat;
        float result = (float) sizeBytes;
        int suffix = R.string.byteShort;
        long mult = 1;
        if (result > 900.0f) {
            suffix = R.string.kilobyteShort;
            mult = 1024;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.megabyteShort;
            mult = 1048576;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.gigabyteShort;
            mult = 1073741824;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.terabyteShort;
            mult = 1099511627776L;
            result /= 1024.0f;
        }
        if (result > 900.0f) {
            suffix = R.string.petabyteShort;
            mult = 1125899906842624L;
            result /= 1024.0f;
        }
        if (result < 1.0f) {
            roundFactor = 100;
            roundFormat = "%.2f";
        } else if (result < 10.0f) {
            if ((flags & 1) != 0) {
                roundFactor = 10;
                roundFormat = "%.1f";
            } else {
                roundFactor = 100;
                roundFormat = "%.2f";
            }
        } else if (result >= 100.0f) {
            roundFactor = 1;
            roundFormat = "%.0f";
        } else if ((flags & 1) != 0) {
            roundFactor = 1;
            roundFormat = "%.0f";
        } else {
            roundFactor = 100;
            roundFormat = "%.2f";
        }
        return new BytesResult(String.format(roundFormat, new Object[]{Float.valueOf(result)}), res.getString(suffix), (flags & 2) == 0 ? 0 : (((long) Math.round(((float) roundFactor) * result)) * mult) / ((long) roundFactor));
    }

    @Deprecated
    public static String formatIpAddress(int ipv4Address) {
        return NetworkUtils.intToInetAddress(ipv4Address).getHostAddress();
    }

    public static String formatShortElapsedTime(Context context, long millis) {
        long secondsLong = millis / 1000;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        if (secondsLong >= 86400) {
            days = (int) (secondsLong / 86400);
            secondsLong -= (long) (SECONDS_PER_DAY * days);
        }
        if (secondsLong >= 3600) {
            hours = (int) (secondsLong / 3600);
            secondsLong -= (long) (hours * SECONDS_PER_HOUR);
        }
        if (secondsLong >= 60) {
            minutes = (int) (secondsLong / 60);
            secondsLong -= (long) (minutes * 60);
        }
        int seconds = (int) secondsLong;
        if (days >= 2) {
            return context.getString(R.string.durationDays, new Object[]{Integer.valueOf(days + ((hours + 12) / 24))});
        } else if (days > 0) {
            if (hours == 1) {
                return context.getString(R.string.durationDayHour, new Object[]{Integer.valueOf(days), Integer.valueOf(hours)});
            }
            return context.getString(R.string.durationDayHours, new Object[]{Integer.valueOf(days), Integer.valueOf(hours)});
        } else if (hours >= 2) {
            return context.getString(R.string.durationHours, new Object[]{Integer.valueOf(hours + ((minutes + 30) / 60))});
        } else if (hours > 0) {
            if (minutes == 1) {
                return context.getString(R.string.durationHourMinute, new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes)});
            }
            return context.getString(R.string.durationHourMinutes, new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes)});
        } else if (minutes >= 2) {
            return context.getString(R.string.durationMinutes, new Object[]{Integer.valueOf(minutes + ((seconds + 30) / 60))});
        } else if (minutes > 0) {
            if (seconds == 1) {
                return context.getString(R.string.durationMinuteSecond, new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
            }
            return context.getString(R.string.durationMinuteSeconds, new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
        } else if (seconds == 1) {
            return context.getString(R.string.durationSecond, new Object[]{Integer.valueOf(seconds)});
        } else {
            return context.getString(R.string.durationSeconds, new Object[]{Integer.valueOf(seconds)});
        }
    }

    public static String formatShortElapsedTimeRoundingUpToMinutes(Context context, long millis) {
        long minutesRoundedUp = ((millis + DateUtils.MINUTE_IN_MILLIS) - 1) / DateUtils.MINUTE_IN_MILLIS;
        if (minutesRoundedUp == 0) {
            return context.getString(R.string.durationMinutes, new Object[]{Integer.valueOf(0)});
        } else if (minutesRoundedUp != 1) {
            return formatShortElapsedTime(context, minutesRoundedUp * DateUtils.MINUTE_IN_MILLIS);
        } else {
            return context.getString(R.string.durationMinute, new Object[]{Integer.valueOf(1)});
        }
    }
}
