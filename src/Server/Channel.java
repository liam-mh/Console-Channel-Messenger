package Server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Channel {

    private static final String _class = Channel.class.getSimpleName();
    private String channelName = null;
    private List<Message> channelMessages = new ArrayList<Message>();

    public Channel(String channelName) {
        if (channelName == null)
            throw new NullPointerException();
        this.channelName = channelName;
    }
    public Channel(String channelName, List<Message> messages) {
        if (channelName == null)
            throw new NullPointerException();
        this.channelName = channelName;
        channelMessages = messages;
    }

    public String getChannelName()
    { return channelName; }

    public void addMessage(Message message) {
        channelMessages.add(message);
    }

    public List<Message> getChannelMessages() {
        return channelMessages;
    }

    public List<Message> getChannelMessages(long timeStamp) {
        List<Message> timeFilterMessages = new ArrayList<Message>();
        for (Message m : channelMessages) {
            if (m.getTimestamp() > timeStamp)
                timeFilterMessages.add(m);
        }
        return timeFilterMessages;
    }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Get messages
        JSONArray arr = new JSONArray();
        for (Message msg : channelMessages)
            arr.add(msg.toJSON());

        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class",           _class);
        obj.put("channelName", channelName);
        obj.put("channelMessages",     arr);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static Channel fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get messages from JSONArray
            JSONArray arr = (JSONArray)obj.get("channelMessages");
            List<Message> messages = new ArrayList<>();
            for (Object msg_obj : arr)
                messages.add(Message.fromJSON(msg_obj));

            // Get values and create object
            String channelName = (String)obj.get("channelName");
            return new Channel(channelName, messages);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
