package Client.Requests;

import org.json.simple.JSONObject;

public class LoginRequest extends Request {

    private static final String _class = LoginRequest.class.getSimpleName();
    private String name;

    public LoginRequest(String name) {
        // check for null
        if (name == null)
            throw new NullPointerException();
        this.name = name;
    }

    public String getName() { return name; }

    //////////////////////////////////////////////////////////////////////////
    // JSON representation

    // Serializes into a JSONObject
    @SuppressWarnings("unchecked")
    public Object toJSON() {
        // Create object
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("name", name);
        return obj;
    }

    // Deserialize from a JSONObject.
    public static LoginRequest fromJSON(Object objectType) {
        try {
            // Return null if incorrect object is being requested
            JSONObject obj = (JSONObject)objectType;
            if (!_class.equals(obj.get("_class")))
                return null;

            // Get values and create object
            String name = (String)obj.get("name");
            return new LoginRequest(name);

        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
