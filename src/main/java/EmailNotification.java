import EmailUtils.DynamoDBManage;
import EmailUtils.SendMessage;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

import java.io.IOException;
import java.util.Map;

public class EmailNotification implements RequestHandler<SNSEvent, Object>{

    final String sender = System.getenv("SENDER");

    public Object handleRequest(SNSEvent request, Context context) {

        SNSEvent.SNS sns = request.getRecords().get(0).getSNS();
        context.getLogger().log(">>>>>>>>  got sns; ");

        Map<String, SNSEvent.MessageAttribute> attributeMap = sns.getMessageAttributes();
        context.getLogger().log(">>>>>>>>  attributeMap : " + attributeMap.toString());
        String user = attributeMap.get("User").getValue();
        String due_in = attributeMap.get("Due_in").getValue();

        context.getLogger().log(">>>>>>>>  user : " + user);
        context.getLogger().log(">>>>>>>>  due_in : " + due_in);


        if(new DynamoDBManage(context).checkToken(user)){
            String msg = sns.getMessage();
            context.getLogger().log(">>>>>>>>  body message : " + msg);

            try {
                SendMessage.sendMessage(sender, user, due_in, msg, context);
                context.getLogger().log(">>>>>>>>  call sending function : " );

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

