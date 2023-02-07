package Client.Requests;

import org.json.simple.JSONObject;

public class QuitRequest extends Request {

    private static final String _class = QuitRequest.class.getSimpleName();

    public QuitRequest() {}

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class",   _class);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static QuitRequest fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get values and create object
            return new QuitRequest();

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
