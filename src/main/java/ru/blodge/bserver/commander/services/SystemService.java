package ru.blodge.bserver.commander.services;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.blodge.bserver.commander.model.system.DriveInfo;
import ru.blodge.bserver.commander.model.system.ResourceUtilizationInfo;
import ru.blodge.bserver.commander.model.system.SystemInfo;
import ru.blodge.bserver.commander.retrofit.SystemInfoApi;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.blodge.bserver.commander.configuration.TelegramBotConfig.SYSTEM_INFO_HOST;

public class SystemService {

    private final SystemInfoApi systemInfoApi;

    private static final SystemService instance = new SystemService();

    private SystemService() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SYSTEM_INFO_HOST)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient)
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

    public ResourceUtilizationInfo getResourceUtilizationInfo() throws IOException {
        return systemInfoApi.getResourceUtilizationInfo()
                .execute()
                .body();
    }

    public List<DriveInfo> getDrivesInfo() throws IOException {
        return systemInfoApi.getDrivesInfo()
                .execute()
                .body();
    }

    public void reboot() throws IOException {
        systemInfoApi.reboot()
                .execute();
    }

    public void shutdown() throws IOException {
        systemInfoApi.shutdown()
                .execute();
    }

    public void ping() throws IOException {
        systemInfoApi.ping()
                .execute();
    }

}
