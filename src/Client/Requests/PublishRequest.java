package Client.Requests;

import Server.Message;
import org.json.simple.JSONObject;

public class PublishRequest extends Request {

    private static final String _class = PublishRequest.class.getSimpleName();
    private String channel;
    private Message message;

    public PublishRequest(String channel, Message message) {
        // check for null
        if (channel == null || message == null)
            throw new NullPointerException();
        this.channel = channel;
        this.message = message;
    }
    public PublishRequest(String channel, String message, String user, long timeStamp) {
        // check for null
        if (channel == null || message == null || user == null)
            throw new NullPointerException();
        this.channel = channel;
        this.message = new Message(message, user, timeStamp);
    }

    public String getChannel() { return channel; }
    public Message getMessage() { return message; }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Create object
        JSONObject msg_obj = (JSONObject) message.toJSON();
        JSONObject obj = new JSONObject();
        obj.put("_class",   _class);
        obj.put("channel", channel);
        obj.put("message", msg_obj);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static PublishRequest fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get values and create object
            String channel  = (String)obj.get("channel");
            JSONObject msg_obj = (JSONObject)obj.get("message");
            Message message = Message.fromJSON(msg_obj);
            return new PublishRequest(channel, message);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
