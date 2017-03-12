package android.telephony;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import com.android.internal.telephony.IccCardConstants;
import com.samsung.android.smartface.SmartFaceManager;
import com.sec.android.app.CscFeature;

public class ServiceState implements Parcelable {
    public static final Creator<ServiceState> CREATOR = new Creator<ServiceState>() {
        public ServiceState createFromParcel(Parcel in) {
            return new ServiceState(in);
        }

        public ServiceState[] newArray(int size) {
            return new ServiceState[size];
        }
    };
    static final boolean DBG = false;
    static final String LOG_TAG = "PHONE";
    public static final int LTE_IMS_VOICE_AVAIL_NOT_SUPPORT = 2;
    public static final int LTE_IMS_VOICE_AVAIL_SUPPORT = 1;
    public static final int LTE_IMS_VOICE_AVAIL_UNKNOWN = 0;
    public static final int LTE_IS_EB_SUPPORTED_NOT_SUPPORT = 2;
    public static final int LTE_IS_EB_SUPPORTED_SUPPORT = 1;
    public static final int LTE_IS_EB_SUPPORTED_UKNOWN = 0;
    public static final int LTE_IS_EMERGENCY_ACCESS_BARRED = 1;
    public static final int LTE_IS_EMERGENCY_ACCESS_BARRING_UNKNOWN = 0;
    public static final int LTE_IS_EMERGENCY_ACCESS_NOT_BARRED = 2;
    public static final int OPTIONAL_RADIO_TECH_DC = 1;
    public static final int OPTIONAL_RADIO_TECH_NONE = 0;
    public static final int OPTIONAL_RADIO_TECH_TDLTE = 2;
    public static final int REGISTRATION_STATE_HOME_NETWORK = 1;
    public static final int REGISTRATION_STATE_NOT_REGISTERED_AND_NOT_SEARCHING = 0;
    public static final int REGISTRATION_STATE_NOT_REGISTERED_AND_SEARCHING = 2;
    public static final int REGISTRATION_STATE_REGISTRATION_DENIED = 3;
    public static final int REGISTRATION_STATE_ROAMING = 5;
    public static final int REGISTRATION_STATE_UNKNOWN = 4;
    public static final int REGISTRATION_TYPE_CELLULAR = 1;
    public static final int REGISTRATION_TYPE_NOCELLULAR = 2;
    public static final int REGISTRATION_TYPE_UNKNOWN = 0;
    public static final int RIL_FEMTOCELL_INDICATOR_LTE = 1;
    public static final int RIL_FEMTOCELL_INDICATOR_NONE = 0;
    public static final int RIL_RADIO_TECHNOLOGY_1xRTT = 6;
    public static final int RIL_RADIO_TECHNOLOGY_DC = 30;
    public static final int RIL_RADIO_TECHNOLOGY_EDGE = 2;
    public static final int RIL_RADIO_TECHNOLOGY_EHRPD = 13;
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_0 = 7;
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_A = 8;
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_B = 12;
    public static final int RIL_RADIO_TECHNOLOGY_GPRS = 1;
    public static final int RIL_RADIO_TECHNOLOGY_GSM = 16;
    public static final int RIL_RADIO_TECHNOLOGY_HSDPA = 9;
    public static final int RIL_RADIO_TECHNOLOGY_HSPA = 11;
    public static final int RIL_RADIO_TECHNOLOGY_HSPAP = 15;
    public static final int RIL_RADIO_TECHNOLOGY_HSUPA = 10;
    public static final int RIL_RADIO_TECHNOLOGY_IS95A = 4;
    public static final int RIL_RADIO_TECHNOLOGY_IS95B = 5;
    public static final int RIL_RADIO_TECHNOLOGY_IWLAN = 18;
    public static final int RIL_RADIO_TECHNOLOGY_LTE = 14;
    public static final int RIL_RADIO_TECHNOLOGY_TDLTE = 31;
    public static final int RIL_RADIO_TECHNOLOGY_TD_SCDMA = 17;
    public static final int RIL_RADIO_TECHNOLOGY_UMTS = 3;
    public static final int RIL_RADIO_TECHNOLOGY_UNKNOWN = 0;
    public static final int RIL_REG_STATE_DENIED = 3;
    public static final int RIL_REG_STATE_DENIED_EMERGENCY_CALL_ENABLED = 13;
    public static final int RIL_REG_STATE_DENIED_ROAM = 8;
    public static final int RIL_REG_STATE_HOME = 1;
    public static final int RIL_REG_STATE_NOT_REG = 0;
    public static final int RIL_REG_STATE_NOT_REG_EMERGENCY_CALL_ENABLED = 10;
    public static final int RIL_REG_STATE_ROAMING = 5;
    public static final int RIL_REG_STATE_SEARCHING = 2;
    public static final int RIL_REG_STATE_SEARCHING_EMERGENCY_CALL_ENABLED = 12;
    public static final int RIL_REG_STATE_SYNC_DONE = 6;
    public static final int RIL_REG_STATE_SYNC_DONE_ROAM = 9;
    public static final int RIL_REG_STATE_UNKNOWN = 4;
    public static final int RIL_REG_STATE_UNKNOWN_EMERGENCY_CALL_ENABLED = 14;
    public static final int ROAMING_TYPE_DOMESTIC = 2;
    public static final int ROAMING_TYPE_INTERNATIONAL = 3;
    public static final int ROAMING_TYPE_NOT_ROAMING = 0;
    public static final int ROAMING_TYPE_UNKNOWN = 1;
    static final boolean SHIP_BUILD = SmartFaceManager.TRUE.equals(SystemProperties.get("ro.product_ship", SmartFaceManager.FALSE));
    public static final int SNAPSHOT_STATUS_ACTIVATED = 1;
    public static final int SNAPSHOT_STATUS_DEACTIVATED = 0;
    public static final int STATE_EMERGENCY_ONLY = 2;
    public static final int STATE_IN_SERVICE = 0;
    public static final int STATE_OUT_OF_SERVICE = 1;
    public static final int STATE_POWER_OFF = 3;
    private int mCdmaDefaultRoamingIndicator;
    private int mCdmaEriIconIndex = 1;
    private int mCdmaEriIconMode;
    private int mCdmaRoamingIndicator;
    private boolean mCssIndicator;
    private String mDataOperatorAlphaLong;
    private String mDataOperatorAlphaShort;
    private String mDataOperatorNumeric;
    private int mDataRegState = 1;
    private int mDataRoamingType;
    private int mFemtocellIndicator = 0;
    private boolean mIsDataRoamingFromRegistration;
    private boolean mIsEmergencyOnly;
    private boolean mIsManualNetworkSelection;
    private int mLteImsVoiceAvail = 0;
    private int mLteIsEbSupported = 0;
    private int mLteIsEmergencyAccessBarred = 0;
    private int mNetworkId;
    private int mOptionalRadioTech = 0;
    private int mRilDataRadioTechnology;
    private int mRilVoiceRadioTechnology;
    private int mSnapshotStatus = 0;
    private int mSystemId;
    private String mVoiceOperatorAlphaLong;
    private String mVoiceOperatorAlphaShort;
    private String mVoiceOperatorNumeric;
    private int mVoiceRegState = 1;
    private int mVoiceRegType = 0;
    private int mVoiceRoamingType;

    public static final String getRoamingLogString(int roamingType) {
        switch (roamingType) {
            case 0:
                return "home";
            case 1:
                return "roaming";
            case 2:
                return "Domestic Roaming";
            case 3:
                return "International Roaming";
            default:
                return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
    }

    public static ServiceState newFromBundle(Bundle m) {
        ServiceState ret = new ServiceState();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    public ServiceState(ServiceState s) {
        copyFrom(s);
    }

    protected void copyFrom(ServiceState s) {
        this.mVoiceRegState = s.mVoiceRegState;
        this.mDataRegState = s.mDataRegState;
        this.mVoiceRoamingType = s.mVoiceRoamingType;
        this.mDataRoamingType = s.mDataRoamingType;
        this.mVoiceOperatorAlphaLong = s.mVoiceOperatorAlphaLong;
        this.mVoiceOperatorAlphaShort = s.mVoiceOperatorAlphaShort;
        this.mVoiceOperatorNumeric = s.mVoiceOperatorNumeric;
        this.mDataOperatorAlphaLong = s.mDataOperatorAlphaLong;
        this.mDataOperatorAlphaShort = s.mDataOperatorAlphaShort;
        this.mDataOperatorNumeric = s.mDataOperatorNumeric;
        this.mIsManualNetworkSelection = s.mIsManualNetworkSelection;
        this.mRilVoiceRadioTechnology = s.mRilVoiceRadioTechnology;
        this.mRilDataRadioTechnology = s.mRilDataRadioTechnology;
        this.mCssIndicator = s.mCssIndicator;
        this.mNetworkId = s.mNetworkId;
        this.mSystemId = s.mSystemId;
        this.mCdmaRoamingIndicator = s.mCdmaRoamingIndicator;
        this.mCdmaDefaultRoamingIndicator = s.mCdmaDefaultRoamingIndicator;
        this.mCdmaEriIconIndex = s.mCdmaEriIconIndex;
        this.mCdmaEriIconMode = s.mCdmaEriIconMode;
        this.mIsEmergencyOnly = s.mIsEmergencyOnly;
        this.mIsDataRoamingFromRegistration = s.mIsDataRoamingFromRegistration;
        this.mVoiceRegType = s.mVoiceRegType;
        this.mLteImsVoiceAvail = s.mLteImsVoiceAvail;
        this.mLteIsEbSupported = s.mLteIsEbSupported;
        this.mLteIsEmergencyAccessBarred = s.mLteIsEmergencyAccessBarred;
        this.mSnapshotStatus = s.mSnapshotStatus;
        this.mFemtocellIndicator = s.mFemtocellIndicator;
        this.mOptionalRadioTech = s.mOptionalRadioTech;
    }

    public ServiceState(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.mVoiceRegState = in.readInt();
        this.mDataRegState = in.readInt();
        this.mVoiceRoamingType = in.readInt();
        this.mDataRoamingType = in.readInt();
        this.mVoiceOperatorAlphaLong = in.readString();
        this.mVoiceOperatorAlphaShort = in.readString();
        this.mVoiceOperatorNumeric = in.readString();
        this.mDataOperatorAlphaLong = in.readString();
        this.mDataOperatorAlphaShort = in.readString();
        this.mDataOperatorNumeric = in.readString();
        this.mIsManualNetworkSelection = in.readInt() != 0;
        this.mRilVoiceRadioTechnology = in.readInt();
        this.mRilDataRadioTechnology = in.readInt();
        if (in.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mCssIndicator = z;
        this.mNetworkId = in.readInt();
        this.mSystemId = in.readInt();
        this.mCdmaRoamingIndicator = in.readInt();
        this.mCdmaDefaultRoamingIndicator = in.readInt();
        this.mCdmaEriIconIndex = in.readInt();
        this.mCdmaEriIconMode = in.readInt();
        if (in.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mIsEmergencyOnly = z;
        if (in.readInt() == 0) {
            z2 = false;
        }
        this.mIsDataRoamingFromRegistration = z2;
        this.mVoiceRegType = in.readInt();
        this.mLteImsVoiceAvail = in.readInt();
        this.mLteIsEbSupported = in.readInt();
        this.mLteIsEmergencyAccessBarred = in.readInt();
        this.mSnapshotStatus = in.readInt();
        this.mFemtocellIndicator = in.readInt();
        this.mOptionalRadioTech = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        int i;
        int i2 = 1;
        out.writeInt(this.mVoiceRegState);
        out.writeInt(this.mDataRegState);
        out.writeInt(this.mVoiceRoamingType);
        out.writeInt(this.mDataRoamingType);
        out.writeString(this.mVoiceOperatorAlphaLong);
        out.writeString(this.mVoiceOperatorAlphaShort);
        out.writeString(this.mVoiceOperatorNumeric);
        out.writeString(this.mDataOperatorAlphaLong);
        out.writeString(this.mDataOperatorAlphaShort);
        out.writeString(this.mDataOperatorNumeric);
        out.writeInt(this.mIsManualNetworkSelection ? 1 : 0);
        out.writeInt(this.mRilVoiceRadioTechnology);
        out.writeInt(this.mRilDataRadioTechnology);
        if (this.mCssIndicator) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeInt(i);
        out.writeInt(this.mNetworkId);
        out.writeInt(this.mSystemId);
        out.writeInt(this.mCdmaRoamingIndicator);
        out.writeInt(this.mCdmaDefaultRoamingIndicator);
        out.writeInt(this.mCdmaEriIconIndex);
        out.writeInt(this.mCdmaEriIconMode);
        if (this.mIsEmergencyOnly) {
            i = 1;
        } else {
            i = 0;
        }
        out.writeInt(i);
        if (!this.mIsDataRoamingFromRegistration) {
            i2 = 0;
        }
        out.writeInt(i2);
        out.writeInt(this.mVoiceRegType);
        out.writeInt(this.mLteImsVoiceAvail);
        out.writeInt(this.mLteIsEbSupported);
        out.writeInt(this.mLteIsEmergencyAccessBarred);
        out.writeInt(this.mSnapshotStatus);
        out.writeInt(this.mFemtocellIndicator);
        out.writeInt(this.mOptionalRadioTech);
    }

    public int describeContents() {
        return 0;
    }

    public int getState() {
        return getVoiceRegState();
    }

    public int getVoiceRegState() {
        return this.mVoiceRegState;
    }

    public int getDataRegState() {
        return this.mDataRegState;
    }

    public boolean getRoaming() {
        return getVoiceRoaming() || getDataRoaming();
    }

    public boolean getVoiceRoaming() {
        return this.mVoiceRoamingType != 0;
    }

    public int getVoiceRoamingType() {
        return this.mVoiceRoamingType;
    }

    public boolean getDataRoaming() {
        return this.mDataRoamingType != 0;
    }

    public void setDataRoamingFromRegistration(boolean dataRoaming) {
        this.mIsDataRoamingFromRegistration = dataRoaming;
    }

    public boolean getDataRoamingFromRegistration() {
        return this.mIsDataRoamingFromRegistration;
    }

    public int getDataRoamingType() {
        return this.mDataRoamingType;
    }

    public boolean isEmergencyOnly() {
        return this.mIsEmergencyOnly;
    }

    public int getCdmaRoamingIndicator() {
        return this.mCdmaRoamingIndicator;
    }

    public int getCdmaDefaultRoamingIndicator() {
        return this.mCdmaDefaultRoamingIndicator;
    }

    public int getCdmaEriIconIndex() {
        return this.mCdmaEriIconIndex;
    }

    public int getCdmaEriIconMode() {
        return this.mCdmaEriIconMode;
    }

    public String getOperatorAlphaLong() {
        return this.mVoiceOperatorAlphaLong;
    }

    public String getVoiceOperatorAlphaLong() {
        return this.mVoiceOperatorAlphaLong;
    }

    public String getDataOperatorAlphaLong() {
        return this.mDataOperatorAlphaLong;
    }

    public String getOperatorAlphaShort() {
        return this.mVoiceOperatorAlphaShort;
    }

    public String getVoiceOperatorAlphaShort() {
        return this.mVoiceOperatorAlphaShort;
    }

    public String getDataOperatorAlphaShort() {
        return this.mDataOperatorAlphaShort;
    }

    public String getOperatorNumeric() {
        return this.mVoiceOperatorNumeric;
    }

    public String getVoiceOperatorNumeric() {
        return this.mVoiceOperatorNumeric;
    }

    public String getDataOperatorNumeric() {
        return this.mDataOperatorNumeric;
    }

    public boolean getIsManualSelection() {
        return this.mIsManualNetworkSelection;
    }

    public int hashCode() {
        int i = 1;
        int hashCode = (this.mIsEmergencyOnly ? 1 : 0) + (this.mFemtocellIndicator + (((((((((this.mDataOperatorNumeric == null ? 0 : this.mDataOperatorNumeric.hashCode()) + (((((((this.mDataRoamingType + (((this.mVoiceRegState * 31) + (this.mDataRegState * 37)) + this.mVoiceRoamingType)) + (this.mIsManualNetworkSelection ? 1 : 0)) + (this.mVoiceOperatorAlphaLong == null ? 0 : this.mVoiceOperatorAlphaLong.hashCode())) + (this.mVoiceOperatorAlphaShort == null ? 0 : this.mVoiceOperatorAlphaShort.hashCode())) + (this.mVoiceOperatorNumeric == null ? 0 : this.mVoiceOperatorNumeric.hashCode())) + (this.mDataOperatorAlphaLong == null ? 0 : this.mDataOperatorAlphaLong.hashCode())) + (this.mDataOperatorAlphaShort == null ? 0 : this.mDataOperatorAlphaShort.hashCode()))) + this.mCdmaRoamingIndicator) + this.mCdmaDefaultRoamingIndicator) + this.mVoiceRegType) + this.mLteImsVoiceAvail) + this.mLteIsEbSupported) + this.mLteIsEmergencyAccessBarred) + this.mSnapshotStatus));
        if (!this.mIsDataRoamingFromRegistration) {
            i = 0;
        }
        return hashCode + i;
    }

    public boolean equals(Object o) {
        try {
            ServiceState s = (ServiceState) o;
            if (o != null && this.mVoiceRegState == s.mVoiceRegState && this.mDataRegState == s.mDataRegState && this.mIsManualNetworkSelection == s.mIsManualNetworkSelection && this.mVoiceRoamingType == s.mVoiceRoamingType && this.mDataRoamingType == s.mDataRoamingType && equalsHandlesNulls(this.mVoiceOperatorAlphaLong, s.mVoiceOperatorAlphaLong) && equalsHandlesNulls(this.mVoiceOperatorAlphaShort, s.mVoiceOperatorAlphaShort) && equalsHandlesNulls(this.mVoiceOperatorNumeric, s.mVoiceOperatorNumeric) && equalsHandlesNulls(this.mDataOperatorAlphaLong, s.mDataOperatorAlphaLong) && equalsHandlesNulls(this.mDataOperatorAlphaShort, s.mDataOperatorAlphaShort) && equalsHandlesNulls(this.mDataOperatorNumeric, s.mDataOperatorNumeric) && equalsHandlesNulls(Integer.valueOf(this.mRilVoiceRadioTechnology), Integer.valueOf(s.mRilVoiceRadioTechnology)) && equalsHandlesNulls(Integer.valueOf(this.mRilDataRadioTechnology), Integer.valueOf(s.mRilDataRadioTechnology)) && equalsHandlesNulls(Boolean.valueOf(this.mCssIndicator), Boolean.valueOf(s.mCssIndicator)) && equalsHandlesNulls(Integer.valueOf(this.mNetworkId), Integer.valueOf(s.mNetworkId)) && equalsHandlesNulls(Integer.valueOf(this.mSystemId), Integer.valueOf(s.mSystemId)) && equalsHandlesNulls(Integer.valueOf(this.mCdmaRoamingIndicator), Integer.valueOf(s.mCdmaRoamingIndicator)) && equalsHandlesNulls(Integer.valueOf(this.mCdmaDefaultRoamingIndicator), Integer.valueOf(s.mCdmaDefaultRoamingIndicator)) && equalsHandlesNulls(Integer.valueOf(this.mVoiceRegType), Integer.valueOf(s.mVoiceRegType)) && equalsHandlesNulls(Integer.valueOf(this.mLteImsVoiceAvail), Integer.valueOf(s.mLteImsVoiceAvail)) && equalsHandlesNulls(Integer.valueOf(this.mLteIsEbSupported), Integer.valueOf(s.mLteIsEbSupported)) && equalsHandlesNulls(Integer.valueOf(this.mLteIsEmergencyAccessBarred), Integer.valueOf(s.mLteIsEmergencyAccessBarred)) && equalsHandlesNulls(Integer.valueOf(this.mSnapshotStatus), Integer.valueOf(s.mSnapshotStatus)) && equalsHandlesNulls(Integer.valueOf(this.mFemtocellIndicator), Integer.valueOf(s.mFemtocellIndicator)) && this.mIsEmergencyOnly == s.mIsEmergencyOnly && this.mIsDataRoamingFromRegistration == s.mIsDataRoamingFromRegistration) {
                return true;
            }
            return false;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public static String rilRadioTechnologyToString(int rt) {
        switch (rt) {
            case 0:
                return "Unknown";
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA-IS95A";
            case 5:
                return "CDMA-IS95B";
            case 6:
                return "1xRTT";
            case 7:
                return "EvDo-rev.0";
            case 8:
                return "EvDo-rev.A";
            case 9:
                return "HSDPA";
            case 10:
                return "HSUPA";
            case 11:
                return "HSPA";
            case 12:
                return "EvDo-rev.B";
            case 13:
                return "eHRPD";
            case 14:
                return "LTE";
            case 15:
                return "HSPAP";
            case 16:
                return "GSM";
            case 17:
                return "TD-SCDMA";
            case 18:
                return "IWLAN";
            case 30:
                return "DC";
            case 31:
                return "TDLTE";
            default:
                String rtString = "Unexpected";
                Rlog.w(LOG_TAG, "Unexpected radioTechnology=" + rt);
                return rtString;
        }
    }

    public String toString() {
        String radioTechnology = rilRadioTechnologyToString(this.mRilVoiceRadioTechnology);
        String dataRadioTechnology = rilRadioTechnologyToString(this.mRilDataRadioTechnology);
        if (SHIP_BUILD) {
            String str;
            StringBuilder append = new StringBuilder().append(this.mVoiceRegState).append(" ").append(this.mDataRegState).append(" ").append("voice ").append(getRoamingLogString(this.mVoiceRoamingType)).append(" ").append("data ").append(getRoamingLogString(this.mDataRoamingType)).append(" ");
            if (this.mIsManualNetworkSelection) {
                str = "(manual)";
            } else {
                str = "";
            }
            return append.append(str).append(" ").append(radioTechnology).append(" ").append(dataRadioTechnology).append(" ").append(this.mCssIndicator ? "CSS supported" : "CSS not supported").append(" ").append(this.mNetworkId).append(" ").append(this.mSystemId).append(" RoamInd=").append(this.mCdmaRoamingIndicator).append(" DefRoamInd=").append(this.mCdmaDefaultRoamingIndicator).append(" VoiceRegType=").append(this.mVoiceRegType).append(" ImsVoiceAvail=").append(this.mLteImsVoiceAvail).append(" IsEbSupport=").append(this.mLteIsEbSupported).append(" IsEaBarred=").append(this.mLteIsEmergencyAccessBarred).append(" Snap=").append(this.mSnapshotStatus).append(" FemtocellInd=").append(this.mFemtocellIndicator).append(" EmergOnly=").append(this.mIsEmergencyOnly).append(" IsDataRoamingFromRegistration=").append(this.mIsDataRoamingFromRegistration).toString();
        }
        return this.mVoiceRegState + " " + this.mDataRegState + " " + "voice " + getRoamingLogString(this.mVoiceRoamingType) + " " + "data " + getRoamingLogString(this.mDataRoamingType) + " " + this.mVoiceOperatorAlphaLong + " " + this.mVoiceOperatorAlphaShort + " " + this.mVoiceOperatorNumeric + " " + this.mDataOperatorAlphaLong + " " + this.mDataOperatorAlphaShort + " " + this.mDataOperatorNumeric + " " + (this.mIsManualNetworkSelection ? "(manual)" : "") + " " + radioTechnology + " " + dataRadioTechnology + " " + (this.mCssIndicator ? "CSS supported" : "CSS not supported") + " " + this.mNetworkId + " " + this.mSystemId + " RoamInd=" + this.mCdmaRoamingIndicator + " DefRoamInd=" + this.mCdmaDefaultRoamingIndicator + " VoiceRegType=" + this.mVoiceRegType + " ImsVoiceAvail=" + this.mLteImsVoiceAvail + " IsEbSupport=" + this.mLteIsEbSupported + " IsEaBarred=" + this.mLteIsEmergencyAccessBarred + " Snap=" + this.mSnapshotStatus + " FemtocellInd=" + this.mFemtocellIndicator + " EmergOnly=" + this.mIsEmergencyOnly + " IsDataRoamingFromRegistration=" + this.mIsDataRoamingFromRegistration;
    }

    private void setNullState(int state) {
        this.mVoiceRegState = state;
        this.mDataRegState = state;
        this.mVoiceRoamingType = 0;
        this.mDataRoamingType = 0;
        this.mVoiceOperatorAlphaLong = null;
        this.mVoiceOperatorAlphaShort = null;
        this.mVoiceOperatorNumeric = null;
        this.mDataOperatorAlphaLong = null;
        this.mDataOperatorAlphaShort = null;
        this.mDataOperatorNumeric = null;
        this.mIsManualNetworkSelection = false;
        this.mRilVoiceRadioTechnology = 0;
        this.mRilDataRadioTechnology = 0;
        this.mCssIndicator = false;
        this.mNetworkId = -1;
        this.mSystemId = -1;
        this.mCdmaRoamingIndicator = -1;
        this.mCdmaDefaultRoamingIndicator = -1;
        this.mCdmaEriIconIndex = -1;
        this.mCdmaEriIconMode = -1;
        this.mIsEmergencyOnly = false;
        this.mIsDataRoamingFromRegistration = false;
        this.mVoiceRegType = 0;
        this.mLteImsVoiceAvail = 0;
        this.mLteIsEbSupported = 0;
        this.mLteIsEmergencyAccessBarred = 0;
        this.mSnapshotStatus = 0;
        this.mFemtocellIndicator = 0;
        this.mOptionalRadioTech = 0;
    }

    public void setStateOutOfService() {
        setNullState(1);
    }

    public void setStateOff() {
        setNullState(3);
    }

    public void setState(int state) {
        setVoiceRegState(state);
    }

    public void setVoiceRegState(int state) {
        this.mVoiceRegState = state;
    }

    public void setDataRegState(int state) {
        this.mDataRegState = state;
    }

    public void setRoaming(boolean roaming) {
        this.mVoiceRoamingType = roaming ? 1 : 0;
        this.mDataRoamingType = this.mVoiceRoamingType;
    }

    public void setVoiceRoaming(boolean roaming) {
        this.mVoiceRoamingType = roaming ? 1 : 0;
    }

    public void setVoiceRoamingType(int type) {
        this.mVoiceRoamingType = type;
    }

    public void setDataRoaming(boolean dataRoaming) {
        this.mDataRoamingType = dataRoaming ? 1 : 0;
    }

    public void setDataRoamingType(int type) {
        this.mDataRoamingType = type;
    }

    public void setEmergencyOnly(boolean emergencyOnly) {
        this.mIsEmergencyOnly = emergencyOnly;
    }

    public void setCdmaRoamingIndicator(int roaming) {
        this.mCdmaRoamingIndicator = roaming;
    }

    public void setCdmaDefaultRoamingIndicator(int roaming) {
        this.mCdmaDefaultRoamingIndicator = roaming;
    }

    public void setCdmaEriIconIndex(int index) {
        this.mCdmaEriIconIndex = index;
    }

    public void setCdmaEriIconMode(int mode) {
        this.mCdmaEriIconMode = mode;
    }

    public void setOperatorName(String longName, String shortName, String numeric) {
        this.mVoiceOperatorAlphaLong = longName;
        this.mVoiceOperatorAlphaShort = shortName;
        this.mVoiceOperatorNumeric = numeric;
        this.mDataOperatorAlphaLong = longName;
        this.mDataOperatorAlphaShort = shortName;
        this.mDataOperatorNumeric = numeric;
    }

    public void setVoiceOperatorName(String longName, String shortName, String numeric) {
        this.mVoiceOperatorAlphaLong = longName;
        this.mVoiceOperatorAlphaShort = shortName;
        this.mVoiceOperatorNumeric = numeric;
    }

    public void setDataOperatorName(String longName, String shortName, String numeric) {
        this.mDataOperatorAlphaLong = longName;
        this.mDataOperatorAlphaShort = shortName;
        this.mDataOperatorNumeric = numeric;
    }

    public void setOperatorAlphaLong(String longName) {
        this.mVoiceOperatorAlphaLong = longName;
        this.mDataOperatorAlphaLong = longName;
    }

    public void setVoiceOperatorAlphaLong(String longName) {
        this.mVoiceOperatorAlphaLong = longName;
    }

    public void setDataOperatorAlphaLong(String longName) {
        this.mDataOperatorAlphaLong = longName;
    }

    public void setIsManualSelection(boolean isManual) {
        this.mIsManualNetworkSelection = isManual;
    }

    private static boolean equalsHandlesNulls(Object a, Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    private void setFromNotifierBundle(Bundle m) {
        this.mVoiceRegState = m.getInt("voiceRegState");
        this.mDataRegState = m.getInt("dataRegState");
        this.mVoiceRoamingType = m.getInt("voiceRoamingType");
        this.mDataRoamingType = m.getInt("dataRoamingType");
        this.mVoiceOperatorAlphaLong = m.getString("operator-alpha-long");
        this.mVoiceOperatorAlphaShort = m.getString("operator-alpha-short");
        this.mVoiceOperatorNumeric = m.getString("operator-numeric");
        this.mDataOperatorAlphaLong = m.getString("data-operator-alpha-long");
        this.mDataOperatorAlphaShort = m.getString("data-operator-alpha-short");
        this.mDataOperatorNumeric = m.getString("data-operator-numeric");
        this.mIsManualNetworkSelection = m.getBoolean("manual");
        this.mRilVoiceRadioTechnology = m.getInt("radioTechnology");
        this.mRilDataRadioTechnology = m.getInt("dataRadioTechnology");
        this.mCssIndicator = m.getBoolean("cssIndicator");
        this.mNetworkId = m.getInt("networkId");
        this.mSystemId = m.getInt("systemId");
        this.mCdmaRoamingIndicator = m.getInt("cdmaRoamingIndicator");
        this.mCdmaDefaultRoamingIndicator = m.getInt("cdmaDefaultRoamingIndicator");
        this.mIsEmergencyOnly = m.getBoolean("emergencyOnly");
        this.mIsDataRoamingFromRegistration = m.getBoolean("isDataRoamingFromRegistration");
        this.mVoiceRegType = m.getInt("voiceRegType");
        this.mLteImsVoiceAvail = m.getInt("lteimsvoiceavail");
        this.mLteIsEbSupported = m.getInt("lteisebsupported");
        this.mLteIsEmergencyAccessBarred = m.getInt("lteisemergencyaccessbarred");
        this.mSnapshotStatus = m.getInt("snapshotstatus");
        this.mFemtocellIndicator = m.getInt("femtocellIndicator");
    }

    public void fillInNotifierBundle(Bundle m) {
        m.putInt("voiceRegState", this.mVoiceRegState);
        m.putInt("dataRegState", this.mDataRegState);
        m.putInt("voiceRoamingType", this.mVoiceRoamingType);
        m.putInt("dataRoamingType", this.mDataRoamingType);
        m.putString("operator-alpha-long", this.mVoiceOperatorAlphaLong);
        m.putString("operator-alpha-short", this.mVoiceOperatorAlphaShort);
        m.putString("operator-numeric", this.mVoiceOperatorNumeric);
        m.putString("data-operator-alpha-long", this.mDataOperatorAlphaLong);
        m.putString("data-operator-alpha-short", this.mDataOperatorAlphaShort);
        m.putString("data-operator-numeric", this.mDataOperatorNumeric);
        m.putBoolean("manual", Boolean.valueOf(this.mIsManualNetworkSelection).booleanValue());
        m.putInt("radioTechnology", this.mRilVoiceRadioTechnology);
        m.putInt("dataRadioTechnology", this.mRilDataRadioTechnology);
        m.putBoolean("cssIndicator", this.mCssIndicator);
        m.putInt("networkId", this.mNetworkId);
        m.putInt("systemId", this.mSystemId);
        m.putInt("cdmaRoamingIndicator", this.mCdmaRoamingIndicator);
        m.putInt("cdmaDefaultRoamingIndicator", this.mCdmaDefaultRoamingIndicator);
        m.putBoolean("emergencyOnly", Boolean.valueOf(this.mIsEmergencyOnly).booleanValue());
        m.putBoolean("isDataRoamingFromRegistration", Boolean.valueOf(this.mIsDataRoamingFromRegistration).booleanValue());
        m.putInt("voiceRegType", this.mVoiceRegType);
        m.putInt("lteimsvoiceavail", this.mLteImsVoiceAvail);
        m.putInt("lteisebsupported", this.mLteIsEbSupported);
        m.putInt("lteisemergencyaccessbarred", this.mLteIsEmergencyAccessBarred);
        m.putInt("snapshotstatus", this.mSnapshotStatus);
        m.putInt("femtocellIndicator", this.mFemtocellIndicator);
    }

    public void setRilVoiceRadioTechnology(int rt) {
        this.mRilVoiceRadioTechnology = rt;
    }

    public void setRilDataRadioTechnology(int rt) {
        this.mRilDataRadioTechnology = rt;
    }

    public void setCssIndicator(int css) {
        this.mCssIndicator = css != 0;
    }

    public void setSystemAndNetworkId(int systemId, int networkId) {
        this.mSystemId = systemId;
        this.mNetworkId = networkId;
    }

    public int getRilVoiceRadioTechnology() {
        return this.mRilVoiceRadioTechnology;
    }

    public int getRilDataRadioTechnology() {
        return this.mRilDataRadioTechnology;
    }

    public int getRadioTechnology() {
        Rlog.e(LOG_TAG, "ServiceState.getRadioTechnology() DEPRECATED will be removed *******");
        return getRilDataRadioTechnology();
    }

    private int rilRadioTechnologyToNetworkType(int rt) {
        switch (rt) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
            case 5:
                return 4;
            case 6:
                return 7;
            case 7:
                return 5;
            case 8:
                return 6;
            case 9:
                return 8;
            case 10:
                return 9;
            case 11:
                return 10;
            case 12:
                return 12;
            case 13:
                return 14;
            case 14:
                if (getOptionalRadioTech() == 2) {
                    return 31;
                }
                return 13;
            case 15:
                if (getOptionalRadioTech() == 1) {
                    return 30;
                }
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            default:
                return 0;
        }
    }

    public int getNetworkType() {
        Rlog.e(LOG_TAG, "ServiceState.getNetworkType() DEPRECATED will be removed *******");
        return rilRadioTechnologyToNetworkType(this.mRilVoiceRadioTechnology);
    }

    public int getDataNetworkType() {
        return rilRadioTechnologyToNetworkType(this.mRilDataRadioTechnology);
    }

    public int getVoiceNetworkType() {
        return rilRadioTechnologyToNetworkType(this.mRilVoiceRadioTechnology);
    }

    public int getCssIndicator() {
        return this.mCssIndicator ? 1 : 0;
    }

    public int getNetworkId() {
        return this.mNetworkId;
    }

    public int getSystemId() {
        return this.mSystemId;
    }

    public static boolean isGsm(int radioTechnology) {
        boolean z = true;
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_ExceptionSid") && radioTechnology == 14 && "KDI".equals("EUR")) {
            String prop = SystemProperties.get("ril.iscdmalte", SmartFaceManager.TRUE);
            Rlog.d(LOG_TAG, "[ServiceState] isGsm prop =" + prop);
            if (prop == null || !prop.equals(SmartFaceManager.FALSE)) {
                z = false;
            }
            return z;
        } else if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_CdmalteTelephonyCommon")) {
            if (radioTechnology == 1 || radioTechnology == 2 || radioTechnology == 3 || radioTechnology == 9 || radioTechnology == 10 || radioTechnology == 11 || radioTechnology == 15 || radioTechnology == 16 || radioTechnology == 17 || radioTechnology == 18) {
                return true;
            }
            return false;
        } else if ((CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) && SmartFaceManager.TRUE.equals(SystemProperties.get("ril.iscdmalte", SmartFaceManager.FALSE)) && radioTechnology == 14) {
            return false;
        } else {
            if (radioTechnology == 1 || radioTechnology == 2 || radioTechnology == 3 || radioTechnology == 9 || radioTechnology == 10 || radioTechnology == 11 || radioTechnology == 14 || radioTechnology == 15 || radioTechnology == 16 || radioTechnology == 17 || radioTechnology == 18) {
                return true;
            }
            return false;
        }
    }

    public static boolean isCdma(int radioTechnology) {
        boolean z = false;
        if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_ExceptionSid") && radioTechnology == 14 && "KDI".equals("EUR")) {
            String prop = SystemProperties.get("ril.iscdmalte", SmartFaceManager.TRUE);
            Rlog.d(LOG_TAG, "[ServiceState] isCdma prop =" + prop);
            if (prop == null || !prop.equals(SmartFaceManager.TRUE)) {
                return false;
            }
            return true;
        } else if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_CdmalteTelephonyCommon")) {
            boolean ret;
            if (radioTechnology == 4 || radioTechnology == 5 || radioTechnology == 6 || radioTechnology == 7 || radioTechnology == 8 || radioTechnology == 12 || radioTechnology == 13 || radioTechnology == 14) {
                ret = true;
            } else {
                ret = false;
            }
            if (CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportEpdg")) {
                if (ret || radioTechnology == 18) {
                    ret = true;
                } else {
                    ret = false;
                }
            }
            return ret;
        } else if ((CscFeature.getInstance().getEnableStatus("CscFeature_RIL_SupportAllRat") || CscFeature.getInstance().getEnableStatus("CscFeature_RIL_Support75Mode")) && SmartFaceManager.TRUE.equals(SystemProperties.get("ril.iscdmalte", SmartFaceManager.TRUE)) && radioTechnology == 14) {
            return true;
        } else {
            if (radioTechnology == 4 || radioTechnology == 5 || radioTechnology == 6 || radioTechnology == 7 || radioTechnology == 8 || radioTechnology == 12 || radioTechnology == 13) {
                z = true;
            }
            return z;
        }
    }

    public static boolean hasCdma(int radioTechnologyBitmask) {
        return (radioTechnologyBitmask & 15) != 0;
    }

    public static boolean bitmaskHasTech(int bearerBitmask, int radioTech) {
        if (bearerBitmask == 0) {
            return true;
        }
        if (radioTech < 1) {
            return false;
        }
        if (((1 << (radioTech - 1)) & bearerBitmask) == 0) {
            return false;
        }
        return true;
    }

    public static int getBitmaskForTech(int radioTech) {
        if (radioTech >= 1) {
            return 1 << (radioTech - 1);
        }
        return 0;
    }

    public static int getBitmaskFromString(String bearerList) {
        int bearerBitmask = 0;
        String[] arr$ = bearerList.split("\\|");
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            try {
                int bearerInt = Integer.parseInt(arr$[i$].trim());
                if (bearerInt == 0) {
                    return 0;
                }
                bearerBitmask |= getBitmaskForTech(bearerInt);
                i$++;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return bearerBitmask;
    }

    public static ServiceState mergeServiceStates(ServiceState baseSs, ServiceState voiceSs) {
        if (voiceSs.mVoiceRegState != 0) {
            return baseSs;
        }
        ServiceState newSs = new ServiceState(baseSs);
        newSs.mVoiceRegState = voiceSs.mVoiceRegState;
        newSs.mIsEmergencyOnly = false;
        return newSs;
    }

    public int getVoiceRegType() {
        return this.mVoiceRegType;
    }

    public void setVoiceRegType(int voiceRegType) {
        this.mVoiceRegType = voiceRegType;
    }

    public boolean canCellularVoiceService() {
        return this.mVoiceRegType != 2;
    }

    public int getLteImsVoiceAvail() {
        return this.mLteImsVoiceAvail;
    }

    public void setLteImsVoiceAvail(int lteImsVoiceAvail) {
        this.mLteImsVoiceAvail = lteImsVoiceAvail;
    }

    public int getLteIsEbSupported() {
        return this.mLteIsEbSupported;
    }

    public void setLteIsEbSupported(int lteIsEbSupported) {
        this.mLteIsEbSupported = lteIsEbSupported;
    }

    public int getLteIsEmergencyAccessBarred() {
        return this.mLteIsEmergencyAccessBarred;
    }

    public void setLteIsEmergencyAccessBarred(int lteIsEmergencyAccessBarred) {
        this.mLteIsEmergencyAccessBarred = lteIsEmergencyAccessBarred;
    }

    public int getSnapshotStatus() {
        return this.mSnapshotStatus;
    }

    public void setSnapshotStatus(int snapshotStatus) {
        this.mSnapshotStatus = snapshotStatus;
    }

    public int getFemtocellIndicator() {
        return this.mFemtocellIndicator;
    }

    public void setFemtocellIndicator(int femtocellIndicator) {
        this.mFemtocellIndicator = femtocellIndicator;
    }

    public void setOptionalRadioTech(int optTech) {
        this.mOptionalRadioTech = optTech;
    }

    public int getOptionalRadioTech() {
        return this.mOptionalRadioTech;
    }

    public static boolean isSameGroupRat(int oldRadioTech, int newRadioTech) {
        boolean z = true;
        Rlog.d(LOG_TAG, "old RAT: " + oldRadioTech + " new RAT: " + newRadioTech);
        switch (oldRadioTech) {
            case 1:
                if (newRadioTech != 2) {
                    z = false;
                }
                return z;
            case 2:
                if (newRadioTech != 1) {
                    z = false;
                }
                return z;
            case 3:
                if (newRadioTech == 11 || newRadioTech == 9 || newRadioTech == 10 || newRadioTech == 15) {
                    return true;
                }
                return false;
            case 9:
                if (newRadioTech == 11 || newRadioTech == 3 || newRadioTech == 10 || newRadioTech == 15) {
                    return true;
                }
                return false;
            case 10:
                if (newRadioTech == 11 || newRadioTech == 9 || newRadioTech == 3 || newRadioTech == 15) {
                    return true;
                }
                return false;
            case 11:
                if (newRadioTech == 3 || newRadioTech == 9 || newRadioTech == 10 || newRadioTech == 15) {
                    return true;
                }
                return false;
            case 15:
                if (newRadioTech == 11 || newRadioTech == 9 || newRadioTech == 10 || newRadioTech == 3) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
