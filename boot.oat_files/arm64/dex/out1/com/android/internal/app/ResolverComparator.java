package com.android.internal.app;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.text.Collator;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class ResolverComparator implements Comparator<ResolvedComponentInfo> {
    private static final boolean DEBUG = false;
    private static final float RECENCY_MULTIPLIER = 3.0f;
    private static final long RECENCY_TIME_PERIOD = 43200000;
    private static final String TAG = "ResolverComparator";
    private static final long USAGE_STATS_PERIOD = 1209600000;
    private final Collator mCollator;
    private final long mCurrentTime;
    private final boolean mHttp;
    private final PackageManager mPm;
    private final String mReferrerPackage;
    private final LinkedHashMap<ComponentName, ScoredTarget> mScoredTargets = new LinkedHashMap();
    private final long mSinceTime;
    private final Map<String, UsageStats> mStats;
    private final UsageStatsManager mUsm;

    static class ScoredTarget {
        public final ComponentInfo componentInfo;
        public long lastTimeUsed;
        public long launchCount;
        public float score;
        public long timeSpent;

        public ScoredTarget(ComponentInfo ci) {
            this.componentInfo = ci;
        }

        public String toString() {
            return "ScoredTarget{" + this.componentInfo + " score: " + this.score + " lastTimeUsed: " + this.lastTimeUsed + " timeSpent: " + this.timeSpent + " launchCount: " + this.launchCount + "}";
        }
    }

    public ResolverComparator(Context context, Intent intent, String referrerPackage) {
        this.mCollator = Collator.getInstance(context.getResources().getConfiguration().locale);
        String scheme = intent.getScheme();
        boolean z = "http".equals(scheme) || "https".equals(scheme);
        this.mHttp = z;
        this.mReferrerPackage = referrerPackage;
        this.mPm = context.getPackageManager();
        this.mUsm = (UsageStatsManager) context.getSystemService("usagestats");
        this.mCurrentTime = System.currentTimeMillis();
        this.mSinceTime = this.mCurrentTime - USAGE_STATS_PERIOD;
        this.mStats = this.mUsm.queryAndAggregateUsageStats(this.mSinceTime, this.mCurrentTime);
    }

    public void compute(List<ResolvedComponentInfo> targets) {
        this.mScoredTargets.clear();
        long recentSinceTime = this.mCurrentTime - RECENCY_TIME_PERIOD;
        long mostRecentlyUsedTime = recentSinceTime + 1;
        long mostTimeSpent = 1;
        int mostLaunched = 1;
        for (ResolvedComponentInfo target : targets) {
            ScoredTarget scoredTarget = new ScoredTarget(target.getResolveInfoAt(0).activityInfo);
            this.mScoredTargets.put(target.name, scoredTarget);
            UsageStats pkStats = (UsageStats) this.mStats.get(target.name.getPackageName());
            if (pkStats != null) {
                if (!(target.name.getPackageName().equals(this.mReferrerPackage) || isPersistentProcess(target))) {
                    long lastTimeUsed = pkStats.getLastTimeUsed();
                    scoredTarget.lastTimeUsed = lastTimeUsed;
                    if (lastTimeUsed > mostRecentlyUsedTime) {
                        mostRecentlyUsedTime = lastTimeUsed;
                    }
                }
                long timeSpent = pkStats.getTotalTimeInForeground();
                scoredTarget.timeSpent = timeSpent;
                if (timeSpent > mostTimeSpent) {
                    mostTimeSpent = timeSpent;
                }
                int launched = pkStats.mLaunchCount;
                scoredTarget.launchCount = (long) launched;
                if (launched > mostLaunched) {
                    mostLaunched = launched;
                }
            }
        }
        for (ScoredTarget target2 : this.mScoredTargets.values()) {
            float recency = ((float) Math.max(target2.lastTimeUsed - recentSinceTime, 0)) / ((float) (mostRecentlyUsedTime - recentSinceTime));
            float launchCountScore = ((float) target2.launchCount) / ((float) mostLaunched);
            target2.score = (((recency * recency) * RECENCY_MULTIPLIER) + (((float) target2.timeSpent) / ((float) mostTimeSpent))) + launchCountScore;
        }
    }

    static boolean isPersistentProcess(ResolvedComponentInfo rci) {
        if (rci == null || rci.getCount() <= 0 || (rci.getResolveInfoAt(0).activityInfo.applicationInfo.flags & 8) == 0) {
            return false;
        }
        return true;
    }

    public int compare(ResolvedComponentInfo lhsp, ResolvedComponentInfo rhsp) {
        String displayPriorityKey = "com.sec.android.display_priority";
        ResolveInfo lhs = lhsp.getResolveInfoAt(0);
        ResolveInfo rhs = rhsp.getResolveInfoAt(0);
        int ia = 0;
        ApplicationInfo ai_A = lhs.activityInfo.applicationInfo;
        if (!(ai_A == null || ai_A.metaData == null)) {
            ia = ai_A.metaData.getInt("com.sec.android.display_priority", 0);
        }
        int ib = 0;
        ApplicationInfo ai_B = rhs.activityInfo.applicationInfo;
        if (!(ai_B == null || ai_B.metaData == null)) {
            ib = ai_B.metaData.getInt("com.sec.android.display_priority", 0);
        }
        if (ia != ib) {
            return ib - ia;
        }
        if (lhs.targetUserId != -2) {
            return 1;
        }
        if (this.mHttp) {
            boolean lhsSpecific = ResolverActivity.isSpecificUriMatch(lhs.match);
            if (lhsSpecific != ResolverActivity.isSpecificUriMatch(rhs.match)) {
                return lhsSpecific ? -1 : 1;
            }
        }
        if (this.mStats != null) {
            float diff = ((ScoredTarget) this.mScoredTargets.get(new ComponentName(rhs.activityInfo.packageName, rhs.activityInfo.name))).score - ((ScoredTarget) this.mScoredTargets.get(new ComponentName(lhs.activityInfo.packageName, lhs.activityInfo.name))).score;
            if (diff != 0.0f) {
                return diff > 0.0f ? 1 : -1;
            }
        }
        CharSequence sa = lhs.loadLabel(this.mPm);
        if (sa == null) {
            sa = lhs.activityInfo.name;
        }
        CharSequence sb = rhs.loadLabel(this.mPm);
        if (sb == null) {
            sb = rhs.activityInfo.name;
        }
        return this.mCollator.compare(sa.toString().trim(), sb.toString().trim());
    }

    public float getScore(ComponentName name) {
        ScoredTarget target = (ScoredTarget) this.mScoredTargets.get(name);
        if (target != null) {
            return target.score;
        }
        return 0.0f;
    }
}
