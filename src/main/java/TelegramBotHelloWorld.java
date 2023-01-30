import java.io.IOException;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class TelegramBotHelloWorld {
    private static final String BOT_TOKEN = "5871928800:AAEdOkuBkuYiLZNe-YxgKV1Pi2Mp96cn2wo";
    private static final String SEND_MESSAGE_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final String GET_UPDATES_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/getUpdates";

    public static void main(String[] args) throws IOException, InterruptedException {
        Long chatId = null;
        while (Objects.isNull(chatId)) {
            chatId = getId();
            Thread.sleep(1000);
        }

        System.out.println("Id grabed! " + chatId);
        OkHttpClient client = new OkHttpClient();

        String json = "{\"chat_id\":\"" + chatId + "\",\"text\":\"Hello, World!\"}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(SEND_MESSAGE_URL)
                .post(body)
                .build();
        while (true) {
            Response response = client.newCall(request).execute();
            Thread.sleep(1000);
        }
//        System.out.println(response.body());
    }

    private static Long getId() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(GET_UPDATES_URL)
                .build();
        Response response = client.newCall(request).execute();

        JSONObject jsonResponse = new JSONObject(response.body().string());
        JSONArray updates = jsonResponse.getJSONArray("result");
        JSONObject update = updates.getJSONObject(updates.length() - 1);
        try {
            return update.getJSONObject("message").getJSONObject("chat").getLong("id");
        } catch (Exception e) {
            return null;
        }
    }
}



