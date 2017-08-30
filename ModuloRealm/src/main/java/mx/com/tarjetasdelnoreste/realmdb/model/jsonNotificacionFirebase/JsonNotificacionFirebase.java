
package mx.com.tarjetasdelnoreste.realmdb.model.jsonNotificacionFirebase;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JsonNotificacionFirebase {

    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("priority")
    private Long mPriority;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("topic")
    private String mTopic;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getPriority() {
        return mPriority;
    }

    public void setPriority(Long priority) {
        mPriority = priority;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String topic) {
        mTopic = topic;
    }

}
