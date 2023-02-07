package Client.Requests;

import org.json.simple.JSONObject;

public class GetRequest extends Request {

    private static final String _class = GetRequest.class.getSimpleName();
    private static long afterTime;

    public GetRequest() {
        afterTime = -1;
    }
    public GetRequest(long afterTime) {
        this.afterTime = afterTime;
    }

    public long getAfterTime() {
        return afterTime;
    }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class",  _class);
        obj.put("time", afterTime);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static GetRequest fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get values and create object
            long afterTime = (long)obj.get("time");
            return new GetRequest(afterTime);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
