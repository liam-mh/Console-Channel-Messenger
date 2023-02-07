package Server.Responses;

import Server.Channel;
import Server.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageListResponse extends Response {

    private static final String _class = MessageListResponse.class.getSimpleName();
    private List<Message> messages;

    public MessageListResponse(List<Message> messages) {
        // check for nulls
        if (messages == null || messages.contains(null))
            throw new NullPointerException();
        this.messages = messages;
    }

    public List<Message> getMessages() { return messages; }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Get messages
        JSONArray arr = new JSONArray();
        for (Message msg : messages)
            arr.add(msg.toJSON());

        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("messages",  arr);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static MessageListResponse fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get messages from JSONArray
            JSONArray arr = (JSONArray)obj.get("messages");
            List<Message> messages = new ArrayList<>();
            for (Object msg_obj : arr)
                messages.add(Message.fromJSON(msg_obj));

            // Get values and create object
            return new MessageListResponse(messages);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
