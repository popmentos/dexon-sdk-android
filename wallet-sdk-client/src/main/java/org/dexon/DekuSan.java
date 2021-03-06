package org.dexon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.fragment.app.Fragment;

public abstract class DekuSan {

    public static final String ACTION_SEND_TRANSACTION = "send-transaction";
    public static final String ACTION_SIGN_MESSAGE = "sign-message";
    public static final String ACTION_SIGN_PERSONAL_MESSAGE = "sign-personal-message";
    public static final String ACTION_SIGN_TYPED_MESSAGE = "sign-typed-message";

    public static final int RESULT_ERROR = 1;

    public static final int REQUEST_CODE_SIGN = 8001;

    private static String packageName = "org.dexon.wallet";

    protected static String appName = "dapp";

    private DekuSan() {}

    public static void setWalletAppPackageName(String packageName) {
        DekuSan.packageName = packageName;
    }

    public static void setAppName(String appName) {
        DekuSan.appName = appName;
    }

    public static String getAppName() {
        return appName;
    }

    public static SendTransactionRequest.Builder sendTransaction() {
        return SendTransactionRequest.builder();
    }

    public static SignMessageRequest.Builder signMessage() {
        return SignMessageRequest.Companion.builder();
    }

    public static SignPersonalMessageRequest.Builder signPersonalMessage() {
        return SignPersonalMessageRequest.Companion.builder();
    }

    public static SignTypedMessageRequest.Builder signTypedMessage() {
        return SignTypedMessageRequest.Companion.builder();
    }

    public static <T extends Request> Call<T> execute(final Fragment fragment, T request) {
        final Intent intent = new Intent();
        intent.setData(request.key());
        if (fragment.getContext() == null) return null;
        if (canStartActivity(fragment.getContext(), intent)) {
            fragment.startActivityForResult(intent, REQUEST_CODE_SIGN);
            return new Call<>(request);
        } else {
            try {
                fragment.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                fragment.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }
            return null;
        }
    }

    public static <T extends Request> Call<T> execute(final Activity activity, T request) {
        final Intent intent = new Intent();
        intent.setData(request.key());
        if (canStartActivity(activity, intent)) {
            activity.startActivityForResult(intent, REQUEST_CODE_SIGN);
            return new Call<>(request);
        } else {
            try {
                activity.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                activity.startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }
            return null;
        }
    }

    static boolean canStartActivity(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        return pm.queryIntentActivities(intent, 0).size() > 0;
    }

    interface ExtraKey {
        String SIGN = "sign";
        String ERROR = "error";
        String FROM = "from";
        String RECIPIENT = "to";
        String VALUE = "amount";
        String GAS_PRICE = "gasPrice";
        String GAS_LIMIT = "gasLimit";
        String CONTRACT = "contract";
        String PAYLOAD = "data";
        String NONCE = "nonce";
        String LEAF_POSITION = "leaf_position";
        String MESSAGE = "message";
        String URL = "url";
        String CALLBACK_URI = "callback";
        String BLOCKCHAIN = "blockchain";
        String NAME = "name";
        String ID = "id";
    }

    public interface ErrorCode {
        int UNKNOWN = -1;
        int NONE = 0;
        int CANCELED = 1;
        int INVALID_REQUEST = 2;
        int WATCH_ONLY = 3;
    }
}
