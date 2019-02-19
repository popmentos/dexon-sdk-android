package dekusan;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Base64;
import org.dexon.dekusan.core.model.Message;

public class BaseSignMessageRequest<V> implements Request, Parcelable {

    private final Message<V> message;
    private final Uri callbackUri;
    private final Uri uri;

    protected BaseSignMessageRequest(Message<V> message, Uri callbackUri) {
        this.message = message;
        this.callbackUri = callbackUri;
        this.uri = toUri(message, callbackUri);
    }

    protected BaseSignMessageRequest(Parcel in) {
        message = in.readParcelable(Message.class.getClassLoader());
        callbackUri = in.readParcelable(Uri.class.getClassLoader());
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    private Uri toUri(Message message, Uri callbackUri) {
        byte[] value = getData();
        value = Base64.encode(value, Base64.DEFAULT);
        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme("dekusan")
                .authority(getAuthority())
                .appendQueryParameter(DekuSan.ExtraKey.MESSAGE, new String(value))
                .appendQueryParameter(DekuSan.ExtraKey.URL, message.url);
        if (callbackUri != null) {
            uriBuilder.appendQueryParameter("callback", callbackUri.toString());
        }
        return uriBuilder.build();
    }

    @Override
    public <T> T body() {
        return (T) message;
    }

    @Override
    public Uri key() {
        return uri;
    }

    @Nullable
    @Override
    public Uri getCallbackUri() {
        return callbackUri;
    }

    byte[] getData() {
        return new byte[0];
    }

    String getAuthority() {
        return "message";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(message, flags);
        dest.writeParcelable(callbackUri, flags);
        dest.writeParcelable(uri, flags);
    }

    public static final Creator<BaseSignMessageRequest> CREATOR = new Creator<BaseSignMessageRequest>() {
        @Override
        public BaseSignMessageRequest createFromParcel(Parcel in) {
            return new BaseSignMessageRequest(in);
        }

        @Override
        public BaseSignMessageRequest[] newArray(int size) {
            return new BaseSignMessageRequest[size];
        }
    };
}
