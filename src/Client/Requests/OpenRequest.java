package Client.Requests;

import org.json.simple.JSONObject;

public class OpenRequest extends Request {

    private static final String _class = OpenRequest.class.getSimpleName();
    private String channelName;

    public OpenRequest(String channelName) {
        // check for null
        if (channelName == null)
            throw new NullPointerException();
        this.channelName = channelName;
    }

    public String getChannelName() { return channelName; }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class",           _class);
        obj.put("channelName", channelName);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static OpenRequest fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get values and create object
            String channelName = (String)obj.get("channelName");
            return new OpenRequest(channelName);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
