/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IBinder$DeathRecipient
 *  android.os.Message
 *  android.os.ParcelFileDescriptor
 *  android.os.RemoteException
 *  android.text.TextUtils
 *  com.sec.enterprise.knox.seams.SEAMS
 *  java.io.FileDescriptor
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.SecurityManager
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.List
 */
package com.samsung.android.spayfw.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.text.TextUtils;
import com.samsung.android.analytics.AnalyticFrameworkService;
import com.samsung.android.spayfw.a.b;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.CardState;
import com.samsung.android.spayfw.appinterface.CommonSpayResponse;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.ExtractGiftCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ExtractGlobalMembershipCardDetailRequest;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.ICardAttributeCallback;
import com.samsung.android.spayfw.appinterface.ICardDataCallback;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IDeleteCardCallback;
import com.samsung.android.spayfw.appinterface.IEnrollCardCallback;
import com.samsung.android.spayfw.appinterface.IExtractLoyaltyCardDetailResponseCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGiftCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardExtractDetailCallback;
import com.samsung.android.spayfw.appinterface.IGlobalMembershipCardRegisterCallback;
import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.IPaymentFramework;
import com.samsung.android.spayfw.appinterface.IProvisionTokenCallback;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.IRefreshIdvCallback;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.ISelectIdvCallback;
import com.samsung.android.spayfw.appinterface.IServerResponseCallback;
import com.samsung.android.spayfw.appinterface.ISynchronizeCardsCallback;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.IUpdateLoyaltyCardCallback;
import com.samsung.android.spayfw.appinterface.IUserSignatureCallback;
import com.samsung.android.spayfw.appinterface.IVerifyIdvCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.appinterface.LoyaltyCardShowRequest;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.ServerRequest;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.UpdateLoyaltyCardInfo;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.BinAttribute;
import com.samsung.android.spayfw.core.PFGenericReceiver;
import com.samsung.android.spayfw.core.PackageStateReceiver;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.UpdateReceiver;
import com.samsung.android.spayfw.core.a.n;
import com.samsung.android.spayfw.core.a.r;
import com.samsung.android.spayfw.core.a.x;
import com.samsung.android.spayfw.core.e;
import com.samsung.android.spayfw.core.f;
import com.samsung.android.spayfw.core.hce.SPayHCEReceiver;
import com.samsung.android.spayfw.core.hce.SPayHCEService;
import com.samsung.android.spayfw.core.i;
import com.samsung.android.spayfw.core.j;
import com.samsung.android.spayfw.fraud.FraudReceiver;
import com.samsung.android.spayfw.payprovider.TokenReplenishReceiver;
import com.samsung.android.spayfw.utils.h;
import com.sec.enterprise.knox.seams.SEAMS;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentFrameworkService
extends Service {
    private i jB;
    private a jT = null;
    private final IPaymentFramework.Stub jU = new IPaymentFramework.Stub(){

        @Override
        public int acceptTnC(String string, boolean bl, ICommonCallback iCommonCallback) {
            c.i("PaymentFrameworkService", "Entered method = acceptTnC");
            if (!PaymentFrameworkService.this.o("acceptTnC")) {
                c.e("PaymentFrameworkService", "acceptTnC:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "acceptTnC:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(2, string, bl, iCommonCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void clearCard() {
            c.d("PaymentFrameworkService", "clearCard()");
            if (!PaymentFrameworkService.this.o("clearCard")) {
                c.e("PaymentFrameworkService", "clearCard:permission denied");
                return;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "clearCard:TAException");
                return;
            }
            try {
                f.j(PaymentFrameworkService.this.mContext).ap();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            Message message = j.a(10, null, null);
            PaymentFrameworkService.this.jB.a(message);
        }

        @Override
        public int clearEnrolledCard(String string) {
            c.d("PaymentFrameworkService", "clearEnrolledCard : " + string);
            if (!PaymentFrameworkService.this.o("clearEnrolledCard")) {
                c.e("PaymentFrameworkService", "clearEnrolledCard:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "clearEnrolledCard:TAException");
                return PaymentFrameworkApp.ay();
            }
            return new x(PaymentFrameworkService.this.mContext).clearEnrolledCard(string);
        }

        @Override
        public int deleteCard(String string, Bundle bundle, IDeleteCardCallback iDeleteCardCallback) {
            c.d("PaymentFrameworkService", "Entered method = deleteCard : " + string);
            if (!PaymentFrameworkService.this.o("deleteCard")) {
                c.e("PaymentFrameworkService", "deleteCard:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "deleteCard:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(22, string, (Object)bundle, iDeleteCardCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int enrollCard(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo, IEnrollCardCallback iEnrollCardCallback) {
            Bundle bundle;
            i i2;
            c.i("PaymentFrameworkService", "Entered method = enrollCard");
            if (!PaymentFrameworkService.this.o("enrollCard")) {
                c.e("PaymentFrameworkService", "enrollCard:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "enrollCard:TAException");
                return PaymentFrameworkApp.ay();
            }
            String string = h.a(PaymentFrameworkService.this.mContext, Binder.getCallingPid());
            if (enrollCardInfo != null) {
                enrollCardInfo.setApplicationId(string);
            }
            Message message = j.a(1, enrollCardInfo, billingInfo, iEnrollCardCallback);
            if (enrollCardInfo != null && enrollCardInfo.getExtraEnrollData() != null && (i2 = PaymentFrameworkService.this.a(bundle = enrollCardInfo.getExtraEnrollData())) != null) {
                c.d("PaymentFrameworkService", "send to restore handler");
                i2.a(message);
                return 0;
            }
            c.d("PaymentFrameworkService", "send to main handler");
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public int extractGiftCardDetail(ExtractGiftCardDetailRequest extractGiftCardDetailRequest, SecuredObject securedObject, IGiftCardExtractDetailCallback iGiftCardExtractDetailCallback) {
            c.d("PaymentFrameworkService", "Entered method =  extractGiftCardDetail()");
            if (!PaymentFrameworkService.this.o("extractGiftCardDetail")) {
                c.e("PaymentFrameworkService", "extractGiftCardDetail:permission denied");
                return -40;
            }
            try {
                f.j(PaymentFrameworkService.this.mContext).A(null);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            Message message = j.a(30, extractGiftCardDetailRequest, securedObject, iGiftCardExtractDetailCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public int extractGlobalMembershipCardDetail(List<ExtractGlobalMembershipCardDetailRequest> list, IGlobalMembershipCardExtractDetailCallback iGlobalMembershipCardExtractDetailCallback) {
            c.d("PaymentFrameworkService", "Entered method =  extractGlobalMembershipCardDetail()");
            if (!PaymentFrameworkService.this.o("extractGlobalMembershipCardDetail")) {
                c.e("PaymentFrameworkService", "extractGlobalMembershipCardDetail:permission denied");
                return -40;
            }
            try {
                f.j(PaymentFrameworkService.this.mContext).A(null);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            Message message = j.a(43, list, iGlobalMembershipCardExtractDetailCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public int extractLoyaltyCardDetails(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest, IExtractLoyaltyCardDetailResponseCallback iExtractLoyaltyCardDetailResponseCallback) {
            c.d("PaymentFrameworkService", "Entered method =  extractLoyaltyCardDetails()");
            if (!PaymentFrameworkService.this.o("extractLoyaltyCardDetails")) {
                c.e("PaymentFrameworkService", "extractLoyaltyCardDetails:CallerPermission failed");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "extractLoyaltyCardDetails:TAException");
                return PaymentFrameworkApp.ay();
            }
            try {
                f.j(PaymentFrameworkService.this.mContext).A(null);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            Message message = j.a(35, extractLoyaltyCardDetailRequest, iExtractLoyaltyCardDetailResponseCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public List<CardState> getAllCardState(Bundle bundle) {
            c.d("PaymentFrameworkService", "getAllTokenState : ");
            if (!PaymentFrameworkService.this.o("getAllTokenState")) {
                c.e("PaymentFrameworkService", "getAllCardState:permission denied");
                return null;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "getAllCardState:TAException");
                return null;
            }
            return new x(PaymentFrameworkService.this.mContext).getAllCardState(bundle);
        }

        @Override
        public int getCardAttributes(String string, boolean bl, ICardAttributeCallback iCardAttributeCallback) {
            c.d("PaymentFrameworkService", "Entered method = getCardAttributes");
            if (!PaymentFrameworkService.this.o("getCardAttributes")) {
                c.e("PaymentFrameworkService", "getCardAttributes:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "getCardAttributes:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(14, string, bl, iCardAttributeCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int getCardData(String string, ICardDataCallback iCardDataCallback) {
            c.d("PaymentFrameworkService", "Entered method = getTokenData : " + string);
            if (!PaymentFrameworkService.this.o("getTokenData")) {
                c.e("PaymentFrameworkService", "getCardData:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "getCardData:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(20, string, iCardDataCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public String getConfig(String string) {
            c.d("PaymentFrameworkService", "Entered method =  getConfig()");
            if (!PaymentFrameworkService.this.o("getConfig")) {
                return String.valueOf((int)-40);
            }
            return e.h(PaymentFrameworkService.this.mContext).getConfig(string);
        }

        @Override
        public int getEnvironment(ICommonCallback iCommonCallback) {
            Message message = j.a(26, null, iCommonCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
            i i2;
            Bundle bundle;
            c.d("PaymentFrameworkService", "Entered method =  getGiftCardRegisterData()");
            if (!PaymentFrameworkService.this.o("getGiftCardRegisterData")) {
                return -40;
            }
            Message message = j.a(27, giftCardRegisterRequestData, iGiftCardRegisterCallback);
            if (giftCardRegisterRequestData != null && giftCardRegisterRequestData.getExtraData() != null && (i2 = PaymentFrameworkService.this.a(bundle = giftCardRegisterRequestData.getExtraData())) != null) {
                c.d("PaymentFrameworkService", "send to restore handler");
                i2.a(message);
                return 0;
            }
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData, IGiftCardRegisterCallback iGiftCardRegisterCallback) {
            i i2;
            Bundle bundle;
            c.d("PaymentFrameworkService", "Entered method =  getGiftCardTzEncData()");
            if (!PaymentFrameworkService.this.o("getGiftCardTzEncData")) {
                return -40;
            }
            Message message = j.a(28, giftCardRegisterRequestData, iGiftCardRegisterCallback);
            if (giftCardRegisterRequestData != null && giftCardRegisterRequestData.getExtraData() != null && (i2 = PaymentFrameworkService.this.a(bundle = giftCardRegisterRequestData.getExtraData())) != null) {
                c.d("PaymentFrameworkService", "send to restore handler");
                i2.a(message);
                return 0;
            }
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
            c.d("PaymentFrameworkService", "Entered method =  getGlobalMembershipCardRegisterData()");
            if (!PaymentFrameworkService.this.o("getGlobalMembershipCardRegisterData")) {
                return -40;
            }
            Message message = j.a(40, globalMembershipCardRegisterRequestData, iGlobalMembershipCardRegisterCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData, IGlobalMembershipCardRegisterCallback iGlobalMembershipCardRegisterCallback) {
            c.d("PaymentFrameworkService", "Entered method =  getGlobalMembershipCardTzEncData()");
            if (!PaymentFrameworkService.this.o("getGlobalMembershipCardTzEncData")) {
                return -40;
            }
            Message message = j.a(41, globalMembershipCardRegisterRequestData, iGlobalMembershipCardRegisterCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int getLogs(ParcelFileDescriptor parcelFileDescriptor, String string, ICommonCallback iCommonCallback) {
            if (parcelFileDescriptor == null || iCommonCallback == null || string == null) {
                return -5;
            }
            if (!PaymentFrameworkService.this.o("getLogs")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "getLogs:TAException");
                return PaymentFrameworkApp.ay();
            }
            try {
                new SecurityManager().checkWrite(parcelFileDescriptor.getFileDescriptor());
            }
            catch (SecurityException securityException) {
                c.e("PaymentFrameworkService", "can't write to file descriptor");
                return -5;
            }
            Message message = j.a(25, (Object)parcelFileDescriptor, string, iCommonCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public List<String> getPaymentReadyState(String string) {
            c.d("PaymentFrameworkService", "getPaymentReadyState : ");
            if (!PaymentFrameworkService.this.o("getPaymentReadyState")) {
                c.e("PaymentFrameworkService", "getPaymentReadyState:permission denied");
                return null;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "getPaymentReadyState:TAException");
                return null;
            }
            return new x(PaymentFrameworkService.this.mContext).getPaymentReadyState(string);
        }

        @Override
        public TokenStatus getTokenStatus(String string) {
            c.i("PaymentFrameworkService", "Entered method = getTokenStatus");
            if (!PaymentFrameworkService.this.o("getTokenStatus")) {
                c.e("PaymentFrameworkService", "getTokenStatus:permission denied");
                return null;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "getTokenStatus:TAException");
                return null;
            }
            return new x(PaymentFrameworkService.this.mContext).getTokenStatus(string);
        }

        @Override
        public int getTransactionDetails(String string, long l2, long l3, int n2, ITransactionDetailsCallback iTransactionDetailsCallback) {
            c.d("PaymentFrameworkService", "Entered method =  getTransactionDetails()");
            if (!PaymentFrameworkService.this.o("getTransactionDetails")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "getTransactionDetails:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(37, string, l2, l3, n2, iTransactionDetailsCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int getUserSignature(String string, IUserSignatureCallback iUserSignatureCallback) {
            c.d("PaymentFrameworkService", "Entered method =  getUserSignature()");
            if (!PaymentFrameworkService.this.o("getUserSignature")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "getUserSignature:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(31, string, iUserSignatureCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public List<String> isDsrpBlobMissing() {
            c.d("PaymentFrameworkService", "isDsrpBlobMissing : ");
            ArrayList arrayList = new ArrayList();
            if (!PaymentFrameworkService.this.o("isDsrpBlobMissing")) {
                c.e("PaymentFrameworkService", "isDsrpBlobMissing:permission denied");
                return arrayList;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "isDsrpBlobMissing:TAException");
                return arrayList;
            }
            return new x(PaymentFrameworkService.this.mContext).isDsrpBlobMissing();
        }

        @Override
        public int isDsrpBlobMissingForTokenId(String string) {
            c.d("PaymentFrameworkService", "isDsrpBlobMissing : ");
            if (!PaymentFrameworkService.this.o("isDsrpBlobMissing")) {
                c.e("PaymentFrameworkService", "isDsrpBlobMissing:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "isDsrpBlobMissing:TAException");
                return PaymentFrameworkApp.ay();
            }
            return new x(PaymentFrameworkService.this.mContext).isDsrpBlobMissingForTokenId(string);
        }

        @Override
        public int notifyDeviceReset(ICommonCallback iCommonCallback) {
            return this.reset("", iCommonCallback);
        }

        @Override
        public int processPushMessage(PushMessage pushMessage, IPushMessageCallback iPushMessageCallback) {
            c.d("PaymentFrameworkService", "processPushMessage()");
            if (!PaymentFrameworkService.this.o("processPushMessage")) {
                c.e("PaymentFrameworkService", "processPushMessage:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "processPushMessage:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(9, pushMessage, 1, iPushMessageCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int processServerRequest(final ServerRequest serverRequest, final IServerResponseCallback iServerResponseCallback) {
            c.d("PaymentFrameworkService", "Entered method =  processServerRequest()");
            if (!PaymentFrameworkService.this.o("processServerRequest")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "processServerRequest:TAException");
                return PaymentFrameworkApp.ay();
            }
            new Thread(){

                public void run() {
                    try {
                        r.u(PaymentFrameworkService.this.mContext).a(serverRequest, iServerResponseCallback);
                        return;
                    }
                    catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                        return;
                    }
                }
            }.start();
            return 0;
        }

        @Override
        public CommonSpayResponse processSpayApdu(byte[] arrby, Bundle bundle) {
            CommonSpayResponse commonSpayResponse = new CommonSpayResponse();
            c.d("PaymentFrameworkService", "Entered method =  processSpayApdu()");
            if (arrby != null) {
                c.d("PaymentFrameworkService", "processSpayApdu data : " + Arrays.toString((byte[])arrby));
            }
            if (bundle != null) {
                c.d("PaymentFrameworkService", "processSpayApdu extras size [" + bundle.size() + "]");
            }
            commonSpayResponse.setStatus(0);
            commonSpayResponse.setData(new byte[]{1, 2, 3, 4, 5});
            return commonSpayResponse;
        }

        @Override
        public int provisionToken(String string, ProvisionTokenInfo provisionTokenInfo, IProvisionTokenCallback iProvisionTokenCallback) {
            Bundle bundle;
            i i2;
            c.i("PaymentFrameworkService", "Entered method = provisionToken");
            if (!PaymentFrameworkService.this.o("provisionToken")) {
                c.e("PaymentFrameworkService", "provisionToken:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "provisionToken:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(3, string, provisionTokenInfo, iProvisionTokenCallback);
            if (provisionTokenInfo != null && provisionTokenInfo.getActivationParamsBundle() != null && (i2 = PaymentFrameworkService.this.a(bundle = provisionTokenInfo.getActivationParamsBundle())) != null) {
                c.d("PaymentFrameworkService", "send to restore handler");
                i2.a(message);
                return 0;
            }
            c.d("PaymentFrameworkService", "send to handler ");
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int refreshIdv(String string, IRefreshIdvCallback iRefreshIdvCallback) {
            c.i("PaymentFrameworkService", "Entered method = refreshIdv");
            if (!PaymentFrameworkService.this.o("refreshIdv")) {
                c.e("PaymentFrameworkService", "refreshIdv:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "refreshIdv:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(38, string, iRefreshIdvCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int reset(String string, ICommonCallback iCommonCallback) {
            c.d("PaymentFrameworkService", "Entered method = reset : " + string);
            if (!PaymentFrameworkService.this.o("reset")) {
                c.e("PaymentFrameworkService", "reset:permission denied");
                return -40;
            }
            Message message = j.a(13, string, iCommonCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int retryPay(PayConfig payConfig) {
            c.d("PaymentFrameworkService", "Entered method =  retryPay()");
            if (!PaymentFrameworkService.this.o("retryPay")) {
                c.e("PaymentFrameworkService", "retryPay:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "retryPay:TAException");
                return PaymentFrameworkApp.ay();
            }
            return n.q(PaymentFrameworkService.this.mContext).retryPay(payConfig);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public int selectCard(String string, ISelectCardCallback iSelectCardCallback) {
            c.d("PaymentFrameworkService", "Entered method = selectCard");
            if (!PaymentFrameworkService.this.o("selectCard")) {
                c.e("PaymentFrameworkService", "selectCard:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "selectCard:TAException");
                return PaymentFrameworkApp.ay();
            }
            try {
                f.j(PaymentFrameworkService.this.mContext).A(string);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            Message message = j.a(6, string, iSelectCardCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int selectIdv(String string, IdvMethod idvMethod, ISelectIdvCallback iSelectIdvCallback) {
            c.i("PaymentFrameworkService", "Entered method = selectIdv");
            if (!PaymentFrameworkService.this.o("selectIdv")) {
                c.e("PaymentFrameworkService", "selectIdv:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "selectIdv:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(4, string, idvMethod, iSelectIdvCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int setConfig(String string, String string2) {
            c.d("PaymentFrameworkService", "Entered method =  setConfig()");
            if (!PaymentFrameworkService.this.o("setConfig")) {
                return -40;
            }
            return e.h(PaymentFrameworkService.this.mContext).setConfig(string, string2);
        }

        @Override
        public int setJwtToken(String string) {
            c.i("PaymentFrameworkService", "Entered method = setJwtToken");
            if (!PaymentFrameworkService.this.o("setJwtToken")) {
                c.e("PaymentFrameworkService", "not enough permission.throwing security exception");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "setJwtToken:TAException");
                return PaymentFrameworkApp.ay();
            }
            return e.h(PaymentFrameworkService.this.mContext).setConfig("CONFIG_JWT_TOKEN", string);
        }

        @Override
        public int setPin(String string, char[] arrc, ICommonCallback iCommonCallback) {
            c.d("PaymentFrameworkService", "Entered method =  setPin()");
            if (!PaymentFrameworkService.this.o("setPin")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "setPin:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(39, string, arrc, iCommonCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int startGiftCardPay(byte[] arrby, byte[] arrby2, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
            c.d("PaymentFrameworkService", "Entered method =  startGiftCardPay()");
            if (!PaymentFrameworkService.this.o("startGiftCardPay")) {
                c.e("PaymentFrameworkService", "startGiftCardPay:permission denied");
                return -40;
            }
            Message message = j.a(29, arrby, arrby2, securedObject, payConfig, iPayCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int startGlobalMembershipCardPay(String string, byte[] arrby, SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
            c.d("PaymentFrameworkService", "Entered method =  startGlobalMembershipCardPay()");
            if (!PaymentFrameworkService.this.o("startGlobalMembershipCardPay")) {
                c.e("PaymentFrameworkService", "startGlobalMembershipCardPay:permission denied");
                return -40;
            }
            Message message = j.a(42, string, arrby, securedObject, payConfig, iPayCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, IInAppPayCallback iInAppPayCallback) {
            c.d("PaymentFrameworkService", "Entered method =  startInAppPay()");
            if (!PaymentFrameworkService.this.o("startInAppPay")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "startInAppPay:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(24, securedObject, inAppTransactionInfo, iInAppPayCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int startLoyaltyCardPay(LoyaltyCardShowRequest loyaltyCardShowRequest, IPayCallback iPayCallback) {
            c.d("PaymentFrameworkService", "Entered method =  storeUserSignature()");
            if (!PaymentFrameworkService.this.o("storeUserSignature")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "storeUserSignature:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(36, loyaltyCardShowRequest, iPayCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int startPay(SecuredObject securedObject, PayConfig payConfig, IPayCallback iPayCallback) {
            c.d("PaymentFrameworkService", "Entered method =  startPay()");
            if (!PaymentFrameworkService.this.o("startPay")) {
                c.e("PaymentFrameworkService", "startPay:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "startPay:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(7, securedObject, payConfig, iPayCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int stopPay(ICommonCallback iCommonCallback) {
            c.d("PaymentFrameworkService", "Entered method = stopPay()");
            if (!PaymentFrameworkService.this.o("stopPay")) {
                c.e("PaymentFrameworkService", "stopPay:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "stopPay:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(8, null, iCommonCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int storeUserSignature(String string, byte[] arrby, ICommonCallback iCommonCallback) {
            c.d("PaymentFrameworkService", "Entered method =  storeUserSignature()");
            if (!PaymentFrameworkService.this.o("storeUserSignature")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "storeUserSignature:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(32, string, arrby, iCommonCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int synchronizeCards(String string, ISynchronizeCardsCallback iSynchronizeCardsCallback) {
            return 0;
        }

        @Override
        public int updateBinAttribute(String string, String string2, ICommonCallback iCommonCallback) {
            c.d("PaymentFrameworkService", "Entered method =  updateBinAttribute()");
            if (!PaymentFrameworkService.this.o("updateBinAttribute")) {
                c.e("PaymentFrameworkService", "updateBinAttribute:permission denied");
                return -40;
            }
            if (TextUtils.equals((CharSequence)BinAttribute.getServerBinVersion(), (CharSequence)string)) {
                c.d("PaymentFrameworkService", "updateBinAttribute. requested version is same. skip update BIN");
                return -3;
            }
            Message message = j.a(44, string, string2, iCommonCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int updateLoyaltyCard(UpdateLoyaltyCardInfo updateLoyaltyCardInfo, IUpdateLoyaltyCardCallback iUpdateLoyaltyCardCallback) {
            c.d("PaymentFrameworkService", "Entered method =  updateLoyaltyCard()");
            if (!PaymentFrameworkService.this.o("updateLoyaltyCard")) {
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "updateLoyaltyCard:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(34, updateLoyaltyCardInfo, iUpdateLoyaltyCardCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

        @Override
        public int verifyIdv(String string, VerifyIdvInfo verifyIdvInfo, IVerifyIdvCallback iVerifyIdvCallback) {
            c.d("PaymentFrameworkService", "Entered method = verifyIdv");
            if (!PaymentFrameworkService.this.o("verifyIdv")) {
                c.e("PaymentFrameworkService", "verifyIdv:permission denied");
                return -40;
            }
            if (PaymentFrameworkApp.ay() != 0) {
                c.e("PaymentFrameworkService", "verifyIdv:TAException");
                return PaymentFrameworkApp.ay();
            }
            Message message = j.a(5, string, verifyIdvInfo, iVerifyIdvCallback);
            PaymentFrameworkService.this.jB.a(message);
            return 0;
        }

    };
    private Context mContext;

    /*
     * Enabled aggressive block sorting
     */
    private i a(Bundle bundle) {
        String string = bundle != null ? bundle.getString("operation", null) : null;
        if (string == null) {
            c.d("PaymentFrameworkService", "no operation type for handler");
            return null;
        }
        if (string.equals((Object)"restore")) {
            i i2 = PaymentFrameworkApp.aA();
            c.d("PaymentFrameworkService", "getRestoreHandler");
            return i2;
        }
        c.e("PaymentFrameworkService", "no registered handler for " + string);
        return null;
    }

    private boolean o(String string) {
        SEAMS sEAMS = SEAMS.getInstance((Context)this.getApplicationContext());
        if (sEAMS == null) {
            c.e("PaymentFrameworkService", "PaymentFrameworkService.checkCallerPermission(): SEAMS is null");
            return false;
        }
        if (sEAMS.isAuthorized(Binder.getCallingPid(), Binder.getCallingUid(), "PaymentFramework", string) == 0) {
            c.d("PaymentFrameworkService", "PaymentFrameworkService.checkCallerPermission(): Access Granted");
            return true;
        }
        c.e("PaymentFrameworkService", "PaymentFrameworkService.checkCallerPermission(): Access Denied");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public IBinder onBind(Intent intent) {
        Bundle bundle;
        IBinder iBinder;
        int n2 = b.getCallingUserId();
        if (n2 != b.USER_OWNER) {
            c.e("PaymentFrameworkService", "onBind: only app which runs on USER_OWNER has permission to bind: " + n2);
            return null;
        }
        if (!PaymentFrameworkApp.aB().isReady()) {
            c.i("PaymentFrameworkService", "PF not ready. requesting init ");
            if (!PaymentFrameworkApp.aB().init()) {
                c.e("PaymentFrameworkService", "PF init failed. Not return binder to caller ");
                return null;
            }
            c.d("PaymentFrameworkService", "PF init success ");
        }
        if ((bundle = intent.getExtras()) != null && (iBinder = bundle.getBinder("deathDetectorBinder")) != null) {
            c.d("PaymentFrameworkService", "onBind: registering deathBinder : " + (Object)iBinder);
            try {
                this.jT.a(iBinder);
                iBinder.linkToDeath((IBinder.DeathRecipient)this.jT, 0);
            }
            catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
        }
        PackageStateReceiver.enable();
        FraudReceiver.enable();
        PFGenericReceiver.enable();
        UpdateReceiver.enable();
        if (!h.fR()) {
            SPayHCEService.enable();
            SPayHCEReceiver.enable();
            AnalyticFrameworkService.enable();
            TokenReplenishReceiver.enable();
        }
        return this.jU;
    }

    public void onCreate() {
        super.onCreate();
        this.jT = new a();
        this.jB = PaymentFrameworkApp.az();
        this.mContext = this.getApplicationContext();
    }

    private class a
    implements IBinder.DeathRecipient {
        private IBinder iG;

        private a() {
        }

        public void a(IBinder iBinder) {
            this.iG = iBinder;
        }

        public void binderDied() {
            c.e("PaymentFrameworkService", "DeathRecipient: Error: Wallet App died, handle clean up");
            if (this.iG != null) {
                this.iG.unlinkToDeath((IBinder.DeathRecipient)this, 0);
            }
            PaymentFrameworkApp.az().aL();
            PaymentFrameworkApp.aA().aL();
        }
    }

}
