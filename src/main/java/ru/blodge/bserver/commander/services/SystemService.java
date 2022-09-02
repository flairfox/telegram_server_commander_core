package ru.blodge.bserver.commander.services;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.blodge.bserver.commander.model.system.SystemInfo;
import ru.blodge.bserver.commander.retrofit.SystemInfoApi;

import java.io.IOException;

public class SystemService {

    private final SystemInfoApi systemInfoApi;

    private static final SystemService instance = new SystemService();

    private SystemService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:5000")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        this.systemInfoApi = retrofit.create(SystemInfoApi.class);
    }

    public static SystemService instance() {
        return instance;
    }

    public SystemInfo getSystemInfo() throws IOException {
        return systemInfoApi.getSystemInfo()
                .execute()
                .body();
    }

}
