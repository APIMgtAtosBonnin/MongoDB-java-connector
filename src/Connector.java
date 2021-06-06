import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class Connector {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017")); //can also connect to Atlas
        MongoDatabase database = mongoClient.getDatabase("projects_v1");
        MongoCollection<Document> clients = database.getCollection("clients");
        MongoCollection<Document> service_providers = database.getCollection("service_providers");
        MongoCollection<Document> contracts = database.getCollection("contracts");

        //Retrieve documents
        Document find_filter = (Document) clients.find(new Document("organization", "SNCF")).first();
        String sncf = gson.toJson(find_filter); //prettify JSON
        System.out.println(sncf); //get first document of the cursor
        System.out.println((String)find_filter.get("city")); //can access document fields

        //Create documents
        List<String> contract_projects = Arrays.asList("API Management", "ta mere");
        Document contract = new Document("_id", new ObjectId());
        contract.append("name", "New Contract")
                .append("start date", "2021-01-01")
                .append("end date", "2022-01-01")
                .append("consumer", "SNCF")
                .append("provider", "Atos")
                .append("contract projects", contract_projects);
        //contracts.insertOne(contract);

        //Update documents
        Bson up_filter = eq("name", "New Contract");
        Bson updateOperation = set("comment", "You should learn MongoDB!");
        UpdateResult updateResult = contracts.updateOne(up_filter, updateOperation);
        System.out.println(updateResult);

        //Delete documents
        Bson del_filter = eq("name", "New Contract");
        DeleteResult result = contracts.deleteOne(del_filter);
        System.out.println(result);
    }
}
