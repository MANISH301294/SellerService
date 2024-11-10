package testClass;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import reusable.Reusable;
import reusable.payload;

import java.util.Random;
import static io.restassured.RestAssured.given;

public class Upsert {

    static String applicationId = "42142dd0-1c33-4e82-9e48-985760cafd2e";
    static String jwtToken;
    static String clientId = "7c1f5df8b07e62deb315f410d8788d56a";
  static   Random random = new Random();
    static   StringBuilder stringBuilder = new StringBuilder("002");
    static  String resourceId = "236749df-3120-4a18-8b7a-2378ec4e4fcf";
   static String[] resourceType = {"COUPON","CAMPAIGN_YEARLY","CAMPAIGN_DAILY","RECEIVABLE","SEGMENT"};


    // Authorization

    public static void authorization()
    {
        RestAssured.baseURI = "https://qa-auth-backend.zepto.co.in/";

        String response =  given().queryParam("applicationId",applicationId).header("Content-Type","application/json").body("{\n" +
                "  \"email\": \"manish.rastogi@zeptonow.com\",\n" +
                "  \"password\": \"Manish123\"\n" +
                "}").when().post("api/v1/auth/sign-in").then().assertThat().statusCode(200).extract().response().asString();

        JsonPath json = Reusable.rowToJson(response);
        jwtToken =  json.getString("jwtToken");
        System.out.println("Token = "+jwtToken);
    }

    // Create Resource

    public static void createResource()
    {
        String resourcetype = "";

        for(int i=0;i<3;i++) {
            stringBuilder.append(random.nextInt(10));
        }

        for(int i=0;i<resourceType.length;i++)
        {
            if(resourceType[i].equalsIgnoreCase("CAMPAIGN_DAILY"));
            {
                 resourcetype = resourceType[i];
            }
        }


        String requestId_addon = stringBuilder.toString();

        RestAssured.baseURI = "http://seller-service.zeptonow.dev/";

        String req_body = String.format("{\n" +
                "  \"requestId\": \"550e8400-e29b-41d4-a716-44666644%s\",\n" +
                "  \"operationType\": \"CREATE\",\n" +
                "  \"resourceType\": \"%s\",\n" +
                "  \"resourceId\": \"%s\",\n" +
                "  \"resourceSegments\": {\n" +
                "    \"storeSegmentId\": \"263e320b-28a5-40e6-b505-6023011c37f4\",\n" +
                "    \"userSegmentId\": \"fe40924b-ae4a-4296-85c3-eab426f7396a\",\n" +
                "    \"secondarySubcatIds\": [\"550e8400-ccc1-41d4-a716-446655440028\"]\n" +
                "  },\n" +
                "  \"resourceExperiments\": {\n" +
                "    \"experimentNames\": [\"db9fa811-98bb-4d7d-aaa6-3baedac0765d\", \"73fb1c2a-a26c-4537-b2a0-140bfd01af08\"]\n" +
                "  },\n" +
                "   \"resourceDetails\": {\n" +
                "    \"startTime\": \"2024-11-02T00:00:00Z\",\n" +
                "      \"bucketType\": \"Promotional\",\n" +
                "      \"couponCode\": \"C-coupon2\",\n" +
                "      \"couponName\": \"C coupon2\",\n" +
                "      \"couponType\": \"Seasonal\",\n" +
                "    //  \"pvId\":\"Clothing\",\n" +
                "     \"expiryTime\": \"2024-11-10T23:59:59Z\",\n" +
                "    //  \"campaignName\":\"Campaign daily\",\n" +
                "     \"details\": \" Campaign test details\",\n" +
                "     \"discount\": \"100.00\",\n" +
                "    //  \"campaignDetails\":\"Campaign detals test fine\",\n" +
                "     \"quantity\": 10,\n" +
                "     \"eligibility\":\"seller user\",\n" +
                "    //  \"maximumQuantity\" : 20,\n" +
                "    //  \"maximumQuantityPerUser\": 2,\n" +
                "      \"discountType\": \"AMOUNT\"\n" +
                "    //  \"discountAmountOff\": 100.00\n" +
                "   }\n" +
                "}",requestId_addon,resourcetype,resourceId);

       RequestSpecification requestSpecification =    given().header("X-Client-ID",clientId).body(req_body);

       String res = requestSpecification.when().post("api/v1/resource/upsert").then().extract().response().asString();


        System.out.println(res);
    }

    // List Resource

    public static void listResource()
    {
        RestAssured.baseURI = "https://fcc.zeptonow.dev/seller/";
        String req_body = String.format(payload.body());



        RequestSpecification requestSpecification = given()
                .header("Authorization",jwtToken)
                .header("Content-Type","application/json")
                .body(req_body);

        String resp = requestSpecification.when().post("api/v1/resource/list").then().extract().response().asString();
        JsonPath json = Reusable.rowToJson(resp);
          String[] a  = new String[]{json.getString("data.resources.requestId")};
          String[] b = new String[]{json.getString("data.resources.resourceId")};

          for(String reques:a)
          {
              System.out.println("requestId = "+ reques);
          }

          for(String resour:b)
          {
              System.out.println("resourceId = "+ resour);
          }
    }



  public static void main(String[] args)
  {
      authorization();
      listResource();
  }





}
