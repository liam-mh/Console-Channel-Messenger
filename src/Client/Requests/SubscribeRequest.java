package Client.Requests;

import org.json.simple.JSONObject;

public class SubscribeRequest extends Request {

    private static final String _class = SubscribeRequest.class.getSimpleName();
    private String channelName;
    private boolean subOrUnsub;

    public SubscribeRequest(String channelName, boolean subOrUnsub) {
        // check for null
        if (channelName == null)
            throw new NullPointerException();
        this.channelName = channelName;
        this.subOrUnsub = subOrUnsub;
    }

    public String getChannelName() { return channelName; }
    public boolean getSubOrUnsub() { return subOrUnsub; }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class",           _class);
        obj.put("channelName", channelName);
        obj.put("bool",         subOrUnsub);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static SubscribeRequest fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get values and create object
            String channelName = (String)obj.get("channelName");
            Boolean subOrUnsub = (Boolean)obj.get("bool");
            return new SubscribeRequest(channelName, subOrUnsub);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
