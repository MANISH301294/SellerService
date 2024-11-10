package reusable;

public class payload {

    public static String body()
    {
        return  "{\n" +
                "      \"resourceType\": \"COUPON\",\n" +
                "     \"approvalStatus\": [\"APPROVED\"],\n" +
                "    \"limit\": 50,\n" +
                "    \"offset\":0\n" +
                "}";
    }
}
