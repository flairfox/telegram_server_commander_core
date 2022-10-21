package ru.blodge.bserver.commander.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.blodge.bserver.commander.model.system.DriveInfo;
import ru.blodge.bserver.commander.model.system.ResourceUtilizationInfo;
import ru.blodge.bserver.commander.model.system.SystemInfo;

import java.util.List;

public interface SystemInfoApi {

    String SYSTEM_MODULE_API_ADDRESS = "/api/v1/system/";

    @GET(SYSTEM_MODULE_API_ADDRESS + "sysinfo")
    Call<SystemInfo> getSystemInfo();

    @GET(SYSTEM_MODULE_API_ADDRESS + "utilization")
    Call<ResourceUtilizationInfo> getResourceUtilizationInfo();

    @GET(SYSTEM_MODULE_API_ADDRESS + "drives")
    Call<List<DriveInfo>> getDrivesInfo();

    @GET(SYSTEM_MODULE_API_ADDRESS + "reboot")
    Call<Void> reboot();

    @GET(SYSTEM_MODULE_API_ADDRESS + "shutdown")
    Call<Void> shutdown();

    @GET(SYSTEM_MODULE_API_ADDRESS + "ping")
    Call<Void> ping();

}
