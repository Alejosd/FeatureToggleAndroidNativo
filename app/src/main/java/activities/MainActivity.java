package activities;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.common.util.concurrent.ListenableFuture;
import com.launchdarkly.android.*;
import com.tumblr.alejosd5.featuretoggleandroidnativo.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import models.User;

public class MainActivity extends AppCompatActivity {

    private List<User> users;
    private User user;
    private boolean flagValue;

    private final static String desactiveFeatureToggle = "Java Spark Desactivado Feature Toggle Agile.   ";

    private final static String activeFeatureToggle = "Java Spark Activado Feature Toggle Agile.   ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        user = new User("","");
        users = new ArrayList<>();

        User user1 = new User("alejosd5","1");
        User user2 = new User("Alvaro","2");
        User user3 = new User("Sergio","3");
        User user4 = new User("Marcelo","4");
        User user5 = new User("Pedro","5");
        User user6 = new User("1010101010","6");
        User user7 = new User("Jorge","7");

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);

        setListUsers(users);
        updateUser(users);
    }

    public ListView setListUsers(List<User> users) {

        ArrayList<String> list = new ArrayList<>();

        for (User user : users) {
            list.add(user.getUsername()+","+user.getNumberPhone());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);

        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setAdapter(adapter);

        return listView;
    }

    public void updateUser(List<User> users){

        ListView listView = setListUsers(users);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = String.valueOf(parent.getItemAtPosition(position));
                Log.e("ACTUALIZADO", username);
                TextView text = (TextView) findViewById(R.id.optionText);
                text.setText(username);
                String[] list = username.split(",");
                user.setUsername(list[0]);
                user.setNumberPhone(list[1]);

            }
        });
    }


    public void sendMessage(View view) throws IOException, LaunchDarklyException {
        getMessages();
    }


    public void getMessages() throws IOException, LaunchDarklyException {

        String message = getMessageFeatureToggle(user.getUsername(),user.getNumberPhone());
        messageView(message);
    }


    public String getMessageFeatureToggle(String email,String numberPhone) throws IOException, LaunchDarklyException {
        // TODO Auto-generated method stub

        boolean showFeature = configFeatureToggle(email, numberPhone);
        String message = "";

        if (showFeature) {

            System.out.println("Showing your feature");

            message = activeFeatureToggle+"usuario:  "+email+",telefono:"+numberPhone;

        } else {

            System.out.println("Not showing your feature");

            message = desactiveFeatureToggle+"usuario:  "+email+",telefono:"+numberPhone;

        }


        return message;
    }

    public boolean configFeatureToggle(String user, String numberPhone) throws IOException, LaunchDarklyException {

        final String nameFlag = "sd";

        LDConfig ldConfig = new LDConfig.Builder()
                .setMobileKey("mob-32e034c2-1e1b-43bb-9673-bf24bf40ec1b")
                .build();

        LDUser userFeatureToggle = new LDUser.Builder(user)
                .build();


        ListenableFuture<LDClient> initFuture = LDClient.init(this.getApplication(), ldConfig, userFeatureToggle);
        initFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    flagValue = LDClient.get().boolVariation(nameFlag, false);
                    Log.w(flagValue+"", "FEATURE TOGGLE");
                    Log.w(flagValue+"", "USER TOGGLE");
                } catch (LaunchDarklyException e) {
                    e.printStackTrace();
                }
            }
        }, Executors.newSingleThreadExecutor());




        return flagValue;
    }

    private void messageView(String message) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, message, duration).show();
    }

}
