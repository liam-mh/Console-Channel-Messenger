package Server.Responses;

import org.json.simple.JSONObject;

public class SuccessResponse extends Response {
    private static final String _class = SuccessResponse.class.getSimpleName();
    private String success;

    public SuccessResponse(String success) {
        this.success = success;
    }

    public String getSuccess() { return success; }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class",   _class);
        obj.put("success", success);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static SuccessResponse fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get values and create object
            String success = (String)obj.get("success");
            return new SuccessResponse(success);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
