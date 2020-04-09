package EmailUtils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;

public class DynamoDBManage {

    private AmazonDynamoDB dynamoDBClient;
    private String TABLE_NAME = System.getenv("DYNAMODB_NAME");
    private String PR_KEY = System.getenv("DYNAMODB_PR_KEY");
    private String username;
    private Context context;
    private final long GAP = 3600000;

    public DynamoDBManage( Context context){
        this.dynamoDBClient = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .build();
        this.context = context;
    }

    public void createToken(){
        Table table = new DynamoDB(dynamoDBClient).getTable(TABLE_NAME);

        try{
            Item userToken = new Item().withPrimaryKey(PR_KEY, username)
                    .withNumber("ExpireTTL",(System.currentTimeMillis() + GAP)/ 1000L );
            table.putItem(userToken);
        }catch (Exception e){
            context.getLogger().log("Create items failed.");
            context.getLogger().log(e.getMessage());
        }
    }

    //return true if over 60 minutes
    public boolean checkToken(String username){

        context.getLogger().log("Start checking token.");
        context.getLogger().log("Talbe Name " + TABLE_NAME);

        context.getLogger().log("PR_KEY Name " + PR_KEY);

        Table table = new DynamoDB(dynamoDBClient).getTable(TABLE_NAME);

        context.getLogger().log("table " + table);

        this.username = username;

        try {
            Item item = table.getItem(PR_KEY, username);
            if(item == null){
                createToken();
                return true;
            }else{
                context.getLogger().log("Printing item after retrieving it....");
                context.getLogger().log(item.toJSONPretty());
                return false;
            }

        } catch (Exception e) {
            context.getLogger().log("GetItem failed.");
            context.getLogger().log(e.getMessage());
        }
        return false;
    }
}