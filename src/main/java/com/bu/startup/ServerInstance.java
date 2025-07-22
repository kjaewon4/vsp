package com.bu.startup;

import java.time.LocalDateTime;

import com.bu.startup.entity.AssetBundleEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;


import lombok.Data;

@Data
public class ServerInstance {

	private String room;
    private String username;
    private String mode;
    private String scenename;
    private String appid;
    
    private List<AssetBundleEntity> bundles;
    
    @JsonIgnore
    private Process process;
    private Boolean connection;
    
    private LocalDateTime startedAt;    
    
    public ServerInstance(Process process) {
        this.process = process;
    }

    public boolean isAlive() {
        return process != null && process.isAlive();
    }

    public Process getProcess() {
        return process;
    }
    
    
    
}
