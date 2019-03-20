package com.chicsol.marrymax.utils;

import android.util.Log;

import com.chicsol.marrymax.modal.Members;
import com.chicsol.marrymax.urls.Urls;

import java.util.Calendar;

/**
 * Created by Android on 2/6/2017.
 */

public class functions {
    //Activity activity;

    public functions() {
        //  this.activity = activity;

    }

    public static boolean checkProfileCompleteStatus(Members member) {

        if (member.getMember_status() == 3 || member.getMember_status() == 4) {
            return true;
        } else {
            return false;
        }

    }

    public int getTimeDifference(Long ticks) {

        Calendar cTime = Calendar.getInstance();

        Calendar xTime = Calendar.getInstance();
        xTime.setTimeInMillis(ticks);


        Long mVal = ticks - 621355968000000000L;
        Long mSecValue = mVal / 10000;

        return (int) (((cTime.getTimeInMillis() - mSecValue) / (1000 * 60)));


    }

    public String getAccessToken() {


        Calendar cl2 = Calendar.getInstance();

        Long ticks = 621355968000000000L + cl2.getTimeInMillis() * 10000;
        // Log.e("ticks ", ticks + "");
        Cryptography_Android crypt = new Cryptography_Android();
        String encryptedValue = null;
        try {

            Log.e("ARDTOKEN",""+Urls.ARDTOKEN +"***" + ticks);
            encryptedValue = crypt.Encrypt(Urls.ARDTOKEN +"***" + ticks, Urls.PassPhraseArdAp);
            //120 minutes check
            //    Log.e("encrypted value ", encryptedValue + "");
            String accesstoken = "ARD" + encryptedValue + "-345";

            String acctoken = accesstoken.replaceAll("\\s+", "");
            Log.e("new access token ", Constants.accessToken + "");
            return acctoken;
            //addd  start    ARD  end
        } catch (Exception e) {

            e.printStackTrace();
            return  null;

        }

    }

    public String getToken() {
        if (Constants.accessToken != null) {
            int min = decryptAccessToken();
            if (min > 120) {
                return getAccessToken();


            } else {
                return Constants.accessToken;
            }
        } else {

            return getAccessToken();

        }


    }

    public int decryptAccessToken() {
        String encrypted = Constants.accessToken;
        if (encrypted != null) {
            Log.e("strin after encryption", encrypted);

            encrypted = encrypted.replace("ARD", " ");
            encrypted = encrypted.replace("-345", " ");
            // Log.e("strin after encryption", encrypted);

            String decryptedValue;
            Cryptography_Android crypt = new Cryptography_Android();

            try {
                decryptedValue = crypt.Decrypt(encrypted, Urls.PassPhraseArdAp);

                decryptedValue = decryptedValue.replace(Urls.ARDTOKEN +"***", "");
                //Log.e("Decrypted value ", decryptedValue + "");

                int min = getTimeDifference(Long.parseLong(decryptedValue));
                Log.e("minutes ", min + "");

                return min;
            } catch (Exception e) {

                e.printStackTrace();

                return 0;
            }
        }
        return 0;
    }

}
