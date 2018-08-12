package pl.vrajani;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;

import java.io.BufferedReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class PutObject
{
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        String bucket_name = System.getenv("BUCKET_NAME");
        String file_path = System.getenv("OBJECT_NAME");

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        System.out.format("Uploading %s to S3 bucket %s...\n", file_path, bucket_name);
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
            JSONParser parser = new JSONParser();

            JSONObject event = (JSONObject)parser.parse(reader);

            if (event.get("body") != null) {
                JSONObject body = (JSONObject)parser.parse((String)event.get("body"));
                s3.putObject(bucket_name, file_path, body.toJSONString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Done!");
    }
}
