package com.bu.startup.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bu.startup.dto.GameServerDTO;
import com.bu.startup.entity.GameServer;
import com.bu.startup.repo.GameServerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameServerService {

	 private final GameServerRepository repository;
	 
	 private final Map<Long, Process> processMap = new ConcurrentHashMap<>();	 
	 
	 
	    public List<GameServer> getAllServers() {
	        return repository.findAll();
	    }

	    public void addServer(GameServerDTO dto) {
	        GameServer server = new GameServer();
	        server.setName(dto.getServerName());
	        server.setExecutablePath(dto.getExecutablePath());
	        server.setDefaultParams(dto.getDefaultParams());
	        repository.save(server);
	    }

	    public void deleteServer(Long id) {
	        repository.deleteById(id);
	    }

	    
	    boolean processCheck(Long id, GameServer server)
	    {
	    	Process gameServerProcess = processMap.get(id);	        
	    	
	    	boolean ret, enable;
	    	ret = enable = false;
	    	
	        if(gameServerProcess != null)
	        {
	        	enable = !gameServerProcess.isAlive();
	        	
	        	if( gameServerProcess.isAlive())    	
		        	ret = true;
	        	else        		
			        processMap.remove(id);
	        }
	        
	        server.setEnabled(enable);
	        repository.save(server);
	        
	        return ret;
	    }
	    
//	    @Scheduled(fixedDelay = 10000)
	    public void checkServerProcesses() {
	    	
	    	System.out.println("checkServerProcesses");
	        processMap.forEach((serverId, process) -> {
	        	
	        
	            if (!process.isAlive()) {
	            	
	            	GameServer server = repository.findById(serverId).orElseThrow();
	            	processCheck(serverId, server);
	            	
	                // 필요하다면 DB 상태 업데이트나 알림 처리도 여기에 추가
	            }
	        });
	    }
	    
	    
	    public void runServer(Long id) {
	        GameServer server = repository.findById(id).orElseThrow();
	        
	        if(processCheck(id, server)) return;
	        
	        List<String> command = new ArrayList<>();
	        command.add(server.getExecutablePath());
	        
	        
//	        List<String> command = Arrays.asList(
//	        	    "C:\\Users\\a\\Documents\\Unity\\VirtualStartUP\\bin\\VirtualStartUP.exe",
//	        	    "-username=admin",
//	        	    "-server=192.168.0.100",
//	        	    "-room=1010",
//	        	    "-mode=admin"
//	        	);
	        
	        
	        if (server.getDefaultParams() != null && !server.getDefaultParams().isEmpty()) {
	            command.addAll(List.of(server.getDefaultParams().split(" ")));
	            
	            try {
					InetAddress inetAddress = InetAddress.getLocalHost();
					
					if(!command.contains("-server"))command.add("-server=" + inetAddress.getHostAddress());
		            
		            
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            if(!command.contains("-room"))command.add("-room=" + "1010");
	        }

	        try {
	        	processMap.put(id, new ProcessBuilder(command).start());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
