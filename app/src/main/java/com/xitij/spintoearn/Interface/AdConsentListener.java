package com.xitij.spintoearn.Interface;

import com.google.ads.consent.ConsentStatus;

public interface AdConsentListener {
    void onConsentUpdate(ConsentStatus consentStatus);
}
