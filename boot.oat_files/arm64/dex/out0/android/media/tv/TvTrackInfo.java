package android.media.tv;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public final class TvTrackInfo implements Parcelable {
    public static final Creator<TvTrackInfo> CREATOR = new Creator<TvTrackInfo>() {
        public TvTrackInfo createFromParcel(Parcel in) {
            return new TvTrackInfo(in);
        }

        public TvTrackInfo[] newArray(int size) {
            return new TvTrackInfo[size];
        }
    };
    public static final int TYPE_AUDIO = 0;
    public static final int TYPE_SUBTITLE = 2;
    public static final int TYPE_VIDEO = 1;
    private final int mAudioChannelCount;
    private final int mAudioSampleRate;
    private final CharSequence mDescription;
    private final Bundle mExtra;
    private final String mId;
    private final String mLanguage;
    private final int mType;
    private final float mVideoFrameRate;
    private final int mVideoHeight;
    private final float mVideoPixelAspectRatio;
    private final int mVideoWidth;

    public static final class Builder {
        private int mAudioChannelCount;
        private int mAudioSampleRate;
        private CharSequence mDescription;
        private Bundle mExtra;
        private final String mId;
        private String mLanguage;
        private final int mType;
        private float mVideoFrameRate;
        private int mVideoHeight;
        private float mVideoPixelAspectRatio = 1.0f;
        private int mVideoWidth;

        public Builder(int type, String id) {
            if (type == 0 || type == 1 || type == 2) {
                Preconditions.checkNotNull(id);
                this.mType = type;
                this.mId = id;
                return;
            }
            throw new IllegalArgumentException("Unknown type: " + type);
        }

        public final Builder setLanguage(String language) {
            this.mLanguage = language;
            return this;
        }

        public final Builder setDescription(CharSequence description) {
            this.mDescription = description;
            return this;
        }

        public final Builder setAudioChannelCount(int audioChannelCount) {
            if (this.mType != 0) {
                throw new IllegalStateException("Not an audio track");
            }
            this.mAudioChannelCount = audioChannelCount;
            return this;
        }

        public final Builder setAudioSampleRate(int audioSampleRate) {
            if (this.mType != 0) {
                throw new IllegalStateException("Not an audio track");
            }
            this.mAudioSampleRate = audioSampleRate;
            return this;
        }

        public final Builder setVideoWidth(int videoWidth) {
            if (this.mType != 1) {
                throw new IllegalStateException("Not a video track");
            }
            this.mVideoWidth = videoWidth;
            return this;
        }

        public final Builder setVideoHeight(int videoHeight) {
            if (this.mType != 1) {
                throw new IllegalStateException("Not a video track");
            }
            this.mVideoHeight = videoHeight;
            return this;
        }

        public final Builder setVideoFrameRate(float videoFrameRate) {
            if (this.mType != 1) {
                throw new IllegalStateException("Not a video track");
            }
            this.mVideoFrameRate = videoFrameRate;
            return this;
        }

        public final Builder setVideoPixelAspectRatio(float videoPixelAspectRatio) {
            if (this.mType != 1) {
                throw new IllegalStateException("Not a video track");
            }
            this.mVideoPixelAspectRatio = videoPixelAspectRatio;
            return this;
        }

        public final Builder setExtra(Bundle extra) {
            this.mExtra = new Bundle(extra);
            return this;
        }

        public TvTrackInfo build() {
            return new TvTrackInfo(this.mType, this.mId, this.mLanguage, this.mDescription, this.mAudioChannelCount, this.mAudioSampleRate, this.mVideoWidth, this.mVideoHeight, this.mVideoFrameRate, this.mVideoPixelAspectRatio, this.mExtra);
        }
    }

    private TvTrackInfo(int type, String id, String language, CharSequence description, int audioChannelCount, int audioSampleRate, int videoWidth, int videoHeight, float videoFrameRate, float videoPixelAspectRatio, Bundle extra) {
        this.mType = type;
        this.mId = id;
        this.mLanguage = language;
        this.mDescription = description;
        this.mAudioChannelCount = audioChannelCount;
        this.mAudioSampleRate = audioSampleRate;
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
        this.mVideoFrameRate = videoFrameRate;
        this.mVideoPixelAspectRatio = videoPixelAspectRatio;
        this.mExtra = extra;
    }

    private TvTrackInfo(Parcel in) {
        this.mType = in.readInt();
        this.mId = in.readString();
        this.mLanguage = in.readString();
        this.mDescription = in.readString();
        this.mAudioChannelCount = in.readInt();
        this.mAudioSampleRate = in.readInt();
        this.mVideoWidth = in.readInt();
        this.mVideoHeight = in.readInt();
        this.mVideoFrameRate = in.readFloat();
        this.mVideoPixelAspectRatio = in.readFloat();
        this.mExtra = in.readBundle();
    }

    public final int getType() {
        return this.mType;
    }

    public final String getId() {
        return this.mId;
    }

    public final String getLanguage() {
        return this.mLanguage;
    }

    public final CharSequence getDescription() {
        return this.mDescription;
    }

    public final int getAudioChannelCount() {
        if (this.mType == 0) {
            return this.mAudioChannelCount;
        }
        throw new IllegalStateException("Not an audio track");
    }

    public final int getAudioSampleRate() {
        if (this.mType == 0) {
            return this.mAudioSampleRate;
        }
        throw new IllegalStateException("Not an audio track");
    }

    public final int getVideoWidth() {
        if (this.mType == 1) {
            return this.mVideoWidth;
        }
        throw new IllegalStateException("Not a video track");
    }

    public final int getVideoHeight() {
        if (this.mType == 1) {
            return this.mVideoHeight;
        }
        throw new IllegalStateException("Not a video track");
    }

    public final float getVideoFrameRate() {
        if (this.mType == 1) {
            return this.mVideoFrameRate;
        }
        throw new IllegalStateException("Not a video track");
    }

    public final float getVideoPixelAspectRatio() {
        if (this.mType == 1) {
            return this.mVideoPixelAspectRatio;
        }
        throw new IllegalStateException("Not a video track");
    }

    public final Bundle getExtra() {
        return this.mExtra;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeString(this.mId);
        dest.writeString(this.mLanguage);
        dest.writeString(this.mDescription != null ? this.mDescription.toString() : null);
        dest.writeInt(this.mAudioChannelCount);
        dest.writeInt(this.mAudioSampleRate);
        dest.writeInt(this.mVideoWidth);
        dest.writeInt(this.mVideoHeight);
        dest.writeFloat(this.mVideoFrameRate);
        dest.writeFloat(this.mVideoPixelAspectRatio);
        dest.writeBundle(this.mExtra);
    }
}
