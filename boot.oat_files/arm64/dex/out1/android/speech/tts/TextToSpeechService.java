package android.speech.tts;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.speech.tts.ITextToSpeechService.Stub;
import android.speech.tts.TextToSpeech.Engine;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public abstract class TextToSpeechService extends Service {
    private static final boolean DBG = false;
    public static boolean PAUSE_STATE = false;
    private static final String SYNTH_THREAD_NAME = "SynthThread";
    private static final String TAG = "TextToSpeechService";
    public static Condition pauseCondition = pauseLock.newCondition();
    public static ReentrantLock pauseLock = new ReentrantLock();
    private AudioPlaybackHandler mAudioPlaybackHandler;
    private final Stub mBinder = new Stub() {
        public int speak(IBinder caller, CharSequence text, int queueMode, Bundle params, String utteranceId) {
            if (!checkNonNull(caller, text, params)) {
                return -1;
            }
            SpeechItem item = new SynthesisSpeechItemV1(caller, Binder.getCallingUid(), Binder.getCallingPid(), params, utteranceId, text);
            try {
                TextToSpeechService.pauseLock.lock();
                if (TextToSpeechService.PAUSE_STATE) {
                    SynthesisPlaybackQueueItem.userSelectedOtherItemInPauseState = true;
                    TextToSpeechService.pauseCondition.signal();
                }
                TextToSpeechService.pauseLock.unlock();
                return TextToSpeechService.this.mSynthHandler.enqueueSpeechItem(queueMode, item);
            } catch (Throwable th) {
                TextToSpeechService.pauseLock.unlock();
            }
        }

        public int resume(IBinder callerIdentity) {
            if (callerIdentity == null) {
                return -1;
            }
            try {
                TextToSpeechService.pauseLock.lock();
                TextToSpeechService.pauseCondition.signal();
                return 0;
            } finally {
                TextToSpeechService.pauseLock.unlock();
            }
        }

        public int pause(IBinder callerIdentity) {
            if (callerIdentity == null) {
                return -1;
            }
            try {
                TextToSpeechService.pauseLock.lock();
                TextToSpeechService.PAUSE_STATE = true;
                return 0;
            } finally {
                TextToSpeechService.pauseLock.unlock();
            }
        }

        public boolean isPaused() {
            return TextToSpeechService.PAUSE_STATE;
        }

        public int synthesizeToFileDescriptor(IBinder caller, CharSequence text, ParcelFileDescriptor fileDescriptor, Bundle params, String utteranceId) {
            if (!checkNonNull(caller, text, fileDescriptor, params)) {
                return -1;
            }
            IBinder iBinder = caller;
            Bundle bundle = params;
            String str = utteranceId;
            CharSequence charSequence = text;
            return TextToSpeechService.this.mSynthHandler.enqueueSpeechItem(1, new SynthesisToFileOutputStreamSpeechItemV1(iBinder, Binder.getCallingUid(), Binder.getCallingPid(), bundle, str, charSequence, new AutoCloseOutputStream(ParcelFileDescriptor.adoptFd(fileDescriptor.detachFd()))));
        }

        public int playAudio(IBinder caller, Uri audioUri, int queueMode, Bundle params, String utteranceId) {
            if (!checkNonNull(caller, audioUri, params)) {
                return -1;
            }
            return TextToSpeechService.this.mSynthHandler.enqueueSpeechItem(queueMode, new AudioSpeechItemV1(caller, Binder.getCallingUid(), Binder.getCallingPid(), params, utteranceId, audioUri));
        }

        public int playSilence(IBinder caller, long duration, int queueMode, String utteranceId) {
            if (!checkNonNull(caller)) {
                return -1;
            }
            return TextToSpeechService.this.mSynthHandler.enqueueSpeechItem(queueMode, new SilenceSpeechItem(caller, Binder.getCallingUid(), Binder.getCallingPid(), utteranceId, duration));
        }

        public boolean isSpeaking() {
            return TextToSpeechService.this.mSynthHandler.isSpeaking() || TextToSpeechService.this.mAudioPlaybackHandler.isSpeaking();
        }

        public int stop(IBinder caller) {
            if (!checkNonNull(caller)) {
                return -1;
            }
            try {
                TextToSpeechService.pauseLock.lock();
                TextToSpeechService.pauseCondition.signal();
                TextToSpeechService.PAUSE_STATE = false;
                return TextToSpeechService.this.mSynthHandler.stopForApp(caller);
            } finally {
                TextToSpeechService.pauseLock.unlock();
            }
        }

        public String[] getLanguage() {
            return TextToSpeechService.this.onGetLanguage();
        }

        public String[] getClientDefaultLanguage() {
            return TextToSpeechService.this.getSettingsLocale();
        }

        public int isLanguageAvailable(String lang, String country, String variant) {
            if (checkNonNull(lang)) {
                return TextToSpeechService.this.onIsLanguageAvailable(lang, country, variant);
            }
            return -1;
        }

        public String[] getFeaturesForLanguage(String lang, String country, String variant) {
            Set<String> features = TextToSpeechService.this.onGetFeaturesForLanguage(lang, country, variant);
            if (features == null) {
                return new String[0];
            }
            String[] featuresArray = new String[features.size()];
            features.toArray(featuresArray);
            return featuresArray;
        }

        public int loadLanguage(IBinder caller, String lang, String country, String variant) {
            if (!checkNonNull(lang)) {
                return -1;
            }
            int retVal = TextToSpeechService.this.onIsLanguageAvailable(lang, country, variant);
            if (retVal != 0 && retVal != 1 && retVal != 2) {
                return retVal;
            }
            if (TextToSpeechService.this.mSynthHandler.enqueueSpeechItem(1, new LoadLanguageItem(caller, Binder.getCallingUid(), Binder.getCallingPid(), lang, country, variant)) != 0) {
                return -1;
            }
            return retVal;
        }

        public List<Voice> getVoices() {
            return TextToSpeechService.this.onGetVoices();
        }

        public int loadVoice(IBinder caller, String voiceName) {
            if (!checkNonNull(voiceName)) {
                return -1;
            }
            int retVal = TextToSpeechService.this.onIsValidVoiceName(voiceName);
            if (retVal != 0) {
                return retVal;
            }
            if (TextToSpeechService.this.mSynthHandler.enqueueSpeechItem(1, new LoadVoiceItem(caller, Binder.getCallingUid(), Binder.getCallingPid(), voiceName)) != 0) {
                return -1;
            }
            return retVal;
        }

        public String getDefaultVoiceNameFor(String lang, String country, String variant) {
            if (!checkNonNull(lang)) {
                return null;
            }
            int retVal = TextToSpeechService.this.onIsLanguageAvailable(lang, country, variant);
            if (retVal == 0 || retVal == 1 || retVal == 2) {
                return TextToSpeechService.this.onGetDefaultVoiceNameFor(lang, country, variant);
            }
            return null;
        }

        public void setCallback(IBinder caller, ITextToSpeechCallback cb) {
            if (checkNonNull(caller)) {
                TextToSpeechService.this.mCallbacks.setCallback(caller, cb);
            }
        }

        private String intern(String in) {
            return in.intern();
        }

        private boolean checkNonNull(Object... args) {
            for (Object o : args) {
                if (o == null) {
                    return false;
                }
            }
            return true;
        }
    };
    private CallbackMap mCallbacks;
    private TtsEngines mEngineHelper;
    private String mPackageName;
    private SynthHandler mSynthHandler;
    private final Object mVoicesInfoLock = new Object();

    static class AudioOutputParams {
        public final AudioAttributes mAudioAttributes;
        public final float mPan;
        public final int mSessionId;
        public final float mVolume;

        AudioOutputParams() {
            this.mSessionId = 0;
            this.mVolume = 1.0f;
            this.mPan = 0.0f;
            this.mAudioAttributes = null;
        }

        AudioOutputParams(int sessionId, float volume, float pan, AudioAttributes audioAttributes) {
            this.mSessionId = sessionId;
            this.mVolume = volume;
            this.mPan = pan;
            this.mAudioAttributes = audioAttributes;
        }

        static AudioOutputParams createFromV1ParamsBundle(Bundle paramsBundle, boolean isSpeech) {
            if (paramsBundle == null) {
                return new AudioOutputParams();
            }
            AudioAttributes audioAttributes = (AudioAttributes) paramsBundle.getParcelable(Engine.KEY_PARAM_AUDIO_ATTRIBUTES);
            if (audioAttributes == null) {
                audioAttributes = new Builder().setLegacyStreamType(paramsBundle.getInt(Engine.KEY_PARAM_STREAM, 3)).setContentType(isSpeech ? 1 : 4).build();
            }
            return new AudioOutputParams(paramsBundle.getInt(Engine.KEY_PARAM_SESSION_ID, 0), paramsBundle.getFloat(Engine.KEY_PARAM_VOLUME, 1.0f), paramsBundle.getFloat(Engine.KEY_PARAM_PAN, 0.0f), audioAttributes);
        }
    }

    private abstract class SpeechItem {
        private final Object mCallerIdentity;
        private final int mCallerPid;
        private final int mCallerUid;
        private boolean mStarted = false;
        private boolean mStopped = false;

        public abstract boolean isValid();

        protected abstract void playImpl();

        protected abstract void stopImpl();

        public SpeechItem(Object caller, int callerUid, int callerPid) {
            this.mCallerIdentity = caller;
            this.mCallerUid = callerUid;
            this.mCallerPid = callerPid;
        }

        public Object getCallerIdentity() {
            return this.mCallerIdentity;
        }

        public int getCallerUid() {
            return this.mCallerUid;
        }

        public int getCallerPid() {
            return this.mCallerPid;
        }

        public void play() {
            synchronized (this) {
                if (this.mStarted) {
                    throw new IllegalStateException("play() called twice");
                }
                this.mStarted = true;
            }
            playImpl();
        }

        public void stop() {
            synchronized (this) {
                if (this.mStopped) {
                    throw new IllegalStateException("stop() called twice");
                }
                this.mStopped = true;
            }
            stopImpl();
        }

        protected synchronized boolean isStopped() {
            return this.mStopped;
        }

        protected synchronized boolean isStarted() {
            return this.mStarted;
        }
    }

    interface UtteranceProgressDispatcher {
        void dispatchOnError(int i);

        void dispatchOnStart();

        void dispatchOnStop();

        void dispatchOnSuccess();
    }

    private abstract class UtteranceSpeechItem extends SpeechItem implements UtteranceProgressDispatcher {
        public abstract String getUtteranceId();

        public UtteranceSpeechItem(Object caller, int callerUid, int callerPid) {
            super(caller, callerUid, callerPid);
        }

        public void dispatchOnSuccess() {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnSuccess(getCallerIdentity(), utteranceId);
            }
        }

        public void dispatchOnStop() {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnStop(getCallerIdentity(), utteranceId, isStarted());
            }
        }

        public void dispatchOnStart() {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnStart(getCallerIdentity(), utteranceId);
            }
        }

        public void dispatchOnError(int errorCode) {
            String utteranceId = getUtteranceId();
            if (utteranceId != null) {
                TextToSpeechService.this.mCallbacks.dispatchOnError(getCallerIdentity(), utteranceId, errorCode);
            }
        }

        String getStringParam(Bundle params, String key, String defaultValue) {
            return params == null ? defaultValue : params.getString(key, defaultValue);
        }

        int getIntParam(Bundle params, String key, int defaultValue) {
            return params == null ? defaultValue : params.getInt(key, defaultValue);
        }

        float getFloatParam(Bundle params, String key, float defaultValue) {
            return params == null ? defaultValue : params.getFloat(key, defaultValue);
        }
    }

    private abstract class SpeechItemV1 extends UtteranceSpeechItem {
        protected final Bundle mParams;
        protected final String mUtteranceId;

        SpeechItemV1(Object callerIdentity, int callerUid, int callerPid, Bundle params, String utteranceId) {
            super(callerIdentity, callerUid, callerPid);
            this.mParams = params;
            this.mUtteranceId = utteranceId;
        }

        boolean hasLanguage() {
            return !TextUtils.isEmpty(getStringParam(this.mParams, Engine.KEY_PARAM_LANGUAGE, null));
        }

        int getSpeechRate() {
            return getIntParam(this.mParams, Engine.KEY_PARAM_RATE, TextToSpeechService.this.getDefaultSpeechRate());
        }

        int getPitch() {
            return getIntParam(this.mParams, Engine.KEY_PARAM_PITCH, 100);
        }

        public String getUtteranceId() {
            return this.mUtteranceId;
        }

        AudioOutputParams getAudioParams() {
            return AudioOutputParams.createFromV1ParamsBundle(this.mParams, true);
        }
    }

    private class AudioSpeechItemV1 extends SpeechItemV1 {
        private final AudioPlaybackQueueItem mItem;

        public AudioSpeechItemV1(Object callerIdentity, int callerUid, int callerPid, Bundle params, String utteranceId, Uri uri) {
            super(callerIdentity, callerUid, callerPid, params, utteranceId);
            this.mItem = new AudioPlaybackQueueItem(this, getCallerIdentity(), TextToSpeechService.this, uri, getAudioParams());
        }

        public boolean isValid() {
            return true;
        }

        protected void playImpl() {
            TextToSpeechService.this.mAudioPlaybackHandler.enqueue(this.mItem);
        }

        protected void stopImpl() {
        }

        public String getUtteranceId() {
            return getStringParam(this.mParams, Engine.KEY_PARAM_UTTERANCE_ID, null);
        }

        AudioOutputParams getAudioParams() {
            return AudioOutputParams.createFromV1ParamsBundle(this.mParams, false);
        }
    }

    private class CallbackMap extends RemoteCallbackList<ITextToSpeechCallback> {
        private final HashMap<IBinder, ITextToSpeechCallback> mCallerToCallback;

        private CallbackMap() {
            this.mCallerToCallback = new HashMap();
        }

        public void setCallback(IBinder caller, ITextToSpeechCallback cb) {
            synchronized (this.mCallerToCallback) {
                ITextToSpeechCallback old;
                if (cb != null) {
                    register(cb, caller);
                    old = (ITextToSpeechCallback) this.mCallerToCallback.put(caller, cb);
                } else {
                    old = (ITextToSpeechCallback) this.mCallerToCallback.remove(caller);
                }
                if (!(old == null || old == cb)) {
                    unregister(old);
                }
            }
        }

        public void dispatchOnStop(Object callerIdentity, String utteranceId, boolean started) {
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb != null) {
                try {
                    cb.onStop(utteranceId, started);
                } catch (RemoteException e) {
                    Log.e(TextToSpeechService.TAG, "Callback onStop failed: " + e);
                }
            }
        }

        public void dispatchOnSuccess(Object callerIdentity, String utteranceId) {
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb != null) {
                try {
                    cb.onSuccess(utteranceId);
                } catch (RemoteException e) {
                    Log.e(TextToSpeechService.TAG, "Callback onDone failed: " + e);
                }
            }
        }

        public void dispatchOnStart(Object callerIdentity, String utteranceId) {
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb != null) {
                try {
                    cb.onStart(utteranceId);
                } catch (RemoteException e) {
                    Log.e(TextToSpeechService.TAG, "Callback onStart failed: " + e);
                }
            }
        }

        public void dispatchOnError(Object callerIdentity, String utteranceId, int errorCode) {
            ITextToSpeechCallback cb = getCallbackFor(callerIdentity);
            if (cb != null) {
                try {
                    cb.onError(utteranceId, errorCode);
                } catch (RemoteException e) {
                    Log.e(TextToSpeechService.TAG, "Callback onError failed: " + e);
                }
            }
        }

        public void onCallbackDied(ITextToSpeechCallback callback, Object cookie) {
            IBinder caller = (IBinder) cookie;
            synchronized (this.mCallerToCallback) {
                this.mCallerToCallback.remove(caller);
            }
        }

        public void kill() {
            synchronized (this.mCallerToCallback) {
                this.mCallerToCallback.clear();
                super.kill();
            }
        }

        private ITextToSpeechCallback getCallbackFor(Object caller) {
            ITextToSpeechCallback cb;
            IBinder asBinder = (IBinder) caller;
            synchronized (this.mCallerToCallback) {
                cb = (ITextToSpeechCallback) this.mCallerToCallback.get(asBinder);
            }
            return cb;
        }
    }

    private class LoadLanguageItem extends SpeechItem {
        private final String mCountry;
        private final String mLanguage;
        private final String mVariant;

        public LoadLanguageItem(Object callerIdentity, int callerUid, int callerPid, String language, String country, String variant) {
            super(callerIdentity, callerUid, callerPid);
            this.mLanguage = language;
            this.mCountry = country;
            this.mVariant = variant;
        }

        public boolean isValid() {
            return true;
        }

        protected void playImpl() {
            TextToSpeechService.this.onLoadLanguage(this.mLanguage, this.mCountry, this.mVariant);
        }

        protected void stopImpl() {
        }
    }

    private class LoadVoiceItem extends SpeechItem {
        private final String mVoiceName;

        public LoadVoiceItem(Object callerIdentity, int callerUid, int callerPid, String voiceName) {
            super(callerIdentity, callerUid, callerPid);
            this.mVoiceName = voiceName;
        }

        public boolean isValid() {
            return true;
        }

        protected void playImpl() {
            TextToSpeechService.this.onLoadVoice(this.mVoiceName);
        }

        protected void stopImpl() {
        }
    }

    private class SilenceSpeechItem extends UtteranceSpeechItem {
        private final long mDuration;
        private final String mUtteranceId;

        public SilenceSpeechItem(Object callerIdentity, int callerUid, int callerPid, String utteranceId, long duration) {
            super(callerIdentity, callerUid, callerPid);
            this.mUtteranceId = utteranceId;
            this.mDuration = duration;
        }

        public boolean isValid() {
            return true;
        }

        protected void playImpl() {
            TextToSpeechService.this.mAudioPlaybackHandler.enqueue(new SilencePlaybackQueueItem(this, getCallerIdentity(), this.mDuration));
        }

        protected void stopImpl() {
        }

        public String getUtteranceId() {
            return this.mUtteranceId;
        }
    }

    private class SynthHandler extends Handler {
        private SpeechItem mCurrentSpeechItem = null;
        private boolean mFlushAll;
        private ArrayList<Object> mFlushedObjects = new ArrayList();

        public SynthHandler(Looper looper) {
            super(looper);
        }

        private void startFlushingSpeechItems(Object callerIdentity) {
            synchronized (this.mFlushedObjects) {
                if (callerIdentity == null) {
                    this.mFlushAll = true;
                } else {
                    this.mFlushedObjects.add(callerIdentity);
                }
            }
        }

        private void endFlushingSpeechItems(Object callerIdentity) {
            synchronized (this.mFlushedObjects) {
                if (callerIdentity == null) {
                    this.mFlushAll = false;
                } else {
                    this.mFlushedObjects.remove(callerIdentity);
                }
            }
        }

        private boolean isFlushed(SpeechItem speechItem) {
            boolean z;
            synchronized (this.mFlushedObjects) {
                z = this.mFlushAll || this.mFlushedObjects.contains(speechItem.getCallerIdentity());
            }
            return z;
        }

        private synchronized SpeechItem getCurrentSpeechItem() {
            return this.mCurrentSpeechItem;
        }

        private synchronized SpeechItem setCurrentSpeechItem(SpeechItem speechItem) {
            SpeechItem old;
            old = this.mCurrentSpeechItem;
            this.mCurrentSpeechItem = speechItem;
            return old;
        }

        private synchronized SpeechItem maybeRemoveCurrentSpeechItem(Object callerIdentity) {
            SpeechItem speechItem = null;
            synchronized (this) {
                if (this.mCurrentSpeechItem != null && this.mCurrentSpeechItem.getCallerIdentity() == callerIdentity) {
                    speechItem = this.mCurrentSpeechItem;
                    this.mCurrentSpeechItem = null;
                }
            }
            return speechItem;
        }

        public boolean isSpeaking() {
            return getCurrentSpeechItem() != null;
        }

        public void quit() {
            getLooper().quit();
            SpeechItem current = setCurrentSpeechItem(null);
            if (current != null) {
                current.stop();
            }
        }

        public int enqueueSpeechItem(int queueMode, final SpeechItem speechItem) {
            UtteranceProgressDispatcher utterenceProgress = null;
            if (speechItem instanceof UtteranceProgressDispatcher) {
                utterenceProgress = (UtteranceProgressDispatcher) speechItem;
            }
            if (speechItem.isValid()) {
                if (queueMode == 0) {
                    stopForApp(speechItem.getCallerIdentity());
                } else if (queueMode == 2) {
                    stopAll();
                }
                Message msg = Message.obtain(this, new Runnable() {
                    public void run() {
                        if (SynthHandler.this.isFlushed(speechItem)) {
                            speechItem.stop();
                            return;
                        }
                        SynthHandler.this.setCurrentSpeechItem(speechItem);
                        speechItem.play();
                        SynthHandler.this.setCurrentSpeechItem(null);
                    }
                });
                msg.obj = speechItem.getCallerIdentity();
                if (sendMessage(msg)) {
                    return 0;
                }
                Log.w(TextToSpeechService.TAG, "SynthThread has quit");
                if (utterenceProgress == null) {
                    return -1;
                }
                utterenceProgress.dispatchOnError(-4);
                return -1;
            } else if (utterenceProgress == null) {
                return -1;
            } else {
                utterenceProgress.dispatchOnError(-8);
                return -1;
            }
        }

        public int stopForApp(final Object callerIdentity) {
            if (callerIdentity == null) {
                return -1;
            }
            startFlushingSpeechItems(callerIdentity);
            SpeechItem current = maybeRemoveCurrentSpeechItem(callerIdentity);
            if (current != null) {
                current.stop();
            }
            TextToSpeechService.this.mAudioPlaybackHandler.stopForApp(callerIdentity);
            sendMessage(Message.obtain(this, new Runnable() {
                public void run() {
                    SynthHandler.this.endFlushingSpeechItems(callerIdentity);
                }
            }));
            return 0;
        }

        public int stopAll() {
            startFlushingSpeechItems(null);
            SpeechItem current = setCurrentSpeechItem(null);
            if (current != null) {
                current.stop();
            }
            TextToSpeechService.this.mAudioPlaybackHandler.stop();
            sendMessage(Message.obtain(this, new Runnable() {
                public void run() {
                    SynthHandler.this.endFlushingSpeechItems(null);
                }
            }));
            return 0;
        }
    }

    private class SynthThread extends HandlerThread implements IdleHandler {
        private boolean mFirstIdle = true;

        public SynthThread() {
            super(TextToSpeechService.SYNTH_THREAD_NAME, 0);
        }

        protected void onLooperPrepared() {
            getLooper().getQueue().addIdleHandler(this);
        }

        public boolean queueIdle() {
            if (this.mFirstIdle) {
                this.mFirstIdle = false;
            } else {
                broadcastTtsQueueProcessingCompleted();
            }
            return true;
        }

        private void broadcastTtsQueueProcessingCompleted() {
            TextToSpeechService.this.sendBroadcast(new Intent(TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED));
        }
    }

    class SynthesisSpeechItemV1 extends SpeechItemV1 {
        private final int mCallerUid;
        private final String[] mDefaultLocale;
        private final EventLoggerV1 mEventLogger;
        private AbstractSynthesisCallback mSynthesisCallback;
        private final SynthesisRequest mSynthesisRequest = new SynthesisRequest(this.mText, this.mParams);
        private final CharSequence mText;

        public SynthesisSpeechItemV1(Object callerIdentity, int callerUid, int callerPid, Bundle params, String utteranceId, CharSequence text) {
            super(callerIdentity, callerUid, callerPid, params, utteranceId);
            this.mText = text;
            this.mCallerUid = callerUid;
            this.mDefaultLocale = TextToSpeechService.this.getSettingsLocale();
            setRequestParams(this.mSynthesisRequest);
            this.mEventLogger = new EventLoggerV1(this.mSynthesisRequest, callerUid, callerPid, TextToSpeechService.this.mPackageName);
        }

        public CharSequence getText() {
            return this.mText;
        }

        public boolean isValid() {
            if (this.mText == null) {
                Log.e(TextToSpeechService.TAG, "null synthesis text");
                return false;
            } else if (this.mText.length() < TextToSpeech.getMaxSpeechInputLength()) {
                return true;
            } else {
                Log.w(TextToSpeechService.TAG, "Text too long: " + this.mText.length() + " chars");
                return false;
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void playImpl() {
            /*
            r3 = this;
            r1 = r3.mEventLogger;
            r1.onRequestProcessingStart();
            monitor-enter(r3);
            r1 = r3.isStopped();	 Catch:{ all -> 0x002e }
            if (r1 == 0) goto L_0x000e;
        L_0x000c:
            monitor-exit(r3);	 Catch:{ all -> 0x002e }
        L_0x000d:
            return;
        L_0x000e:
            r1 = r3.createSynthesisCallback();	 Catch:{ all -> 0x002e }
            r3.mSynthesisCallback = r1;	 Catch:{ all -> 0x002e }
            r0 = r3.mSynthesisCallback;	 Catch:{ all -> 0x002e }
            monitor-exit(r3);	 Catch:{ all -> 0x002e }
            r1 = android.speech.tts.TextToSpeechService.this;
            r2 = r3.mSynthesisRequest;
            r1.onSynthesizeText(r2, r0);
            r1 = r0.hasStarted();
            if (r1 == 0) goto L_0x000d;
        L_0x0024:
            r1 = r0.hasFinished();
            if (r1 != 0) goto L_0x000d;
        L_0x002a:
            r0.done();
            goto L_0x000d;
        L_0x002e:
            r1 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x002e }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.speech.tts.TextToSpeechService.SynthesisSpeechItemV1.playImpl():void");
        }

        protected AbstractSynthesisCallback createSynthesisCallback() {
            return new PlaybackSynthesisCallback(getAudioParams(), TextToSpeechService.this.mAudioPlaybackHandler, this, getCallerIdentity(), this.mEventLogger, false);
        }

        private void setRequestParams(SynthesisRequest request) {
            String voiceName = getVoiceName();
            request.setLanguage(getLanguage(), getCountry(), getVariant());
            if (!TextUtils.isEmpty(voiceName)) {
                request.setVoiceName(getVoiceName());
            }
            request.setSpeechRate(getSpeechRate());
            request.setCallerUid(this.mCallerUid);
            request.setPitch(getPitch());
        }

        protected void stopImpl() {
            synchronized (this) {
                AbstractSynthesisCallback synthesisCallback = this.mSynthesisCallback;
            }
            if (synthesisCallback != null) {
                synthesisCallback.stop();
                TextToSpeechService.this.onStop();
                return;
            }
            dispatchOnStop();
        }

        private String getCountry() {
            if (hasLanguage()) {
                return getStringParam(this.mParams, Engine.KEY_PARAM_COUNTRY, "");
            }
            return this.mDefaultLocale[1];
        }

        private String getVariant() {
            if (hasLanguage()) {
                return getStringParam(this.mParams, Engine.KEY_PARAM_VARIANT, "");
            }
            return this.mDefaultLocale[2];
        }

        public String getLanguage() {
            return getStringParam(this.mParams, Engine.KEY_PARAM_LANGUAGE, this.mDefaultLocale[0]);
        }

        public String getVoiceName() {
            return getStringParam(this.mParams, Engine.KEY_PARAM_VOICE_NAME, "");
        }
    }

    private class SynthesisToFileOutputStreamSpeechItemV1 extends SynthesisSpeechItemV1 {
        private final FileOutputStream mFileOutputStream;

        public SynthesisToFileOutputStreamSpeechItemV1(Object callerIdentity, int callerUid, int callerPid, Bundle params, String utteranceId, CharSequence text, FileOutputStream fileOutputStream) {
            super(callerIdentity, callerUid, callerPid, params, utteranceId, text);
            this.mFileOutputStream = fileOutputStream;
        }

        protected AbstractSynthesisCallback createSynthesisCallback() {
            return new FileSynthesisCallback(this.mFileOutputStream.getChannel(), this, getCallerIdentity(), false);
        }

        protected void playImpl() {
            dispatchOnStart();
            super.playImpl();
            try {
                this.mFileOutputStream.close();
            } catch (IOException e) {
                Log.w(TextToSpeechService.TAG, "Failed to close output file", e);
            }
        }
    }

    protected abstract String[] onGetLanguage();

    protected abstract int onIsLanguageAvailable(String str, String str2, String str3);

    protected abstract int onLoadLanguage(String str, String str2, String str3);

    protected abstract void onStop();

    protected abstract void onSynthesizeText(SynthesisRequest synthesisRequest, SynthesisCallback synthesisCallback);

    public void onCreate() {
        super.onCreate();
        SynthThread synthThread = new SynthThread();
        synthThread.start();
        this.mSynthHandler = new SynthHandler(synthThread.getLooper());
        this.mAudioPlaybackHandler = new AudioPlaybackHandler();
        this.mAudioPlaybackHandler.start();
        this.mEngineHelper = new TtsEngines(this);
        this.mCallbacks = new CallbackMap();
        this.mPackageName = getApplicationInfo().packageName;
        String[] defaultLocale = getSettingsLocale();
        onLoadLanguage(defaultLocale[0], defaultLocale[1], defaultLocale[2]);
    }

    public void onDestroy() {
        this.mSynthHandler.quit();
        this.mAudioPlaybackHandler.quit();
        this.mCallbacks.kill();
        super.onDestroy();
    }

    protected Set<String> onGetFeaturesForLanguage(String lang, String country, String variant) {
        return new HashSet();
    }

    private int getExpectedLanguageAvailableStatus(Locale locale) {
        if (!locale.getVariant().isEmpty()) {
            return 2;
        }
        if (locale.getCountry().isEmpty()) {
            return 0;
        }
        return 1;
    }

    public List<Voice> onGetVoices() {
        ArrayList<Voice> voices = new ArrayList();
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                if (onIsLanguageAvailable(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant()) == getExpectedLanguageAvailableStatus(locale)) {
                    voices.add(new Voice(onGetDefaultVoiceNameFor(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant()), locale, 300, 300, false, onGetFeaturesForLanguage(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant())));
                }
            } catch (MissingResourceException e) {
            }
        }
        return voices;
    }

    public String onGetDefaultVoiceNameFor(String lang, String country, String variant) {
        Locale iso3Locale;
        switch (onIsLanguageAvailable(lang, country, variant)) {
            case 0:
                iso3Locale = new Locale(lang);
                break;
            case 1:
                iso3Locale = new Locale(lang, country);
                break;
            case 2:
                iso3Locale = new Locale(lang, country, variant);
                break;
            default:
                return null;
        }
        String voiceName = TtsEngines.normalizeTTSLocale(iso3Locale).toLanguageTag();
        if (onIsValidVoiceName(voiceName) != 0) {
            return null;
        }
        return voiceName;
    }

    public int onLoadVoice(String voiceName) {
        Locale locale = Locale.forLanguageTag(voiceName);
        if (locale == null) {
            return -1;
        }
        try {
            if (onIsLanguageAvailable(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant()) != getExpectedLanguageAvailableStatus(locale)) {
                return -1;
            }
            onLoadLanguage(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant());
            return 0;
        } catch (MissingResourceException e) {
            return -1;
        }
    }

    public int onIsValidVoiceName(String voiceName) {
        Locale locale = Locale.forLanguageTag(voiceName);
        if (locale == null) {
            return -1;
        }
        try {
            if (onIsLanguageAvailable(locale.getISO3Language(), locale.getISO3Country(), locale.getVariant()) == getExpectedLanguageAvailableStatus(locale)) {
                return 0;
            }
            return -1;
        } catch (MissingResourceException e) {
            return -1;
        }
    }

    private int getDefaultSpeechRate() {
        return getSecureSettingInt("tts_default_rate", 100);
    }

    private String[] getSettingsLocale() {
        return TtsEngines.toOldLocaleStringFormat(this.mEngineHelper.getLocalePrefForEngine(this.mPackageName));
    }

    private int getSecureSettingInt(String name, int defaultValue) {
        return Secure.getInt(getContentResolver(), name, defaultValue);
    }

    public IBinder onBind(Intent intent) {
        if (Engine.INTENT_ACTION_TTS_SERVICE.equals(intent.getAction())) {
            return this.mBinder;
        }
        return null;
    }
}
