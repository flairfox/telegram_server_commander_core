package ru.blodge.bserver.commander.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.blodge.bserver.commander.model.system.SystemInfo;

public interface SystemInfoApi {

    @GET("/sysinfo")
    Call<SystemInfo> getSystemInfo();

}
