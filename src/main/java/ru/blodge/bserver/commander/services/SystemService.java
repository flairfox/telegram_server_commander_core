package ru.blodge.bserver.commander.services;

import ru.blodge.bserver.commander.model.system.SystemInfo;

public class SystemService {

    SystemService instance = new SystemService();

    private SystemService() {}

    public SystemService instance() {
        return this.instance;
    }

    public SystemInfo getSystemInfo() {
        return new SystemInfo();
    }

}
