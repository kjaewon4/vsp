package com.bu.startup.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//import org.hibernate.mapping.Map;
import org.springframework.stereotype.Service;

import com.bu.startup.ServerInstance;
import com.bu.startup.entity.AssetBundleEntity;

import lombok.RequiredArgsConstructor;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ServerService {

	final String exeServerPath = "C:\\sts\\sts-4.31.0.RELEASE\\Workspace\\VirtualStartUP\\uploads\\Server\\VirtualPlatform.exe";
	
	 // roomName → ServerInstance
    private final Map<String, ServerInstance> runningServers = new ConcurrentHashMap<>();
    private final AssetBundleService assetBundleService;
    
    private boolean serverRunning = false;

    
    public void start(String param) 
    {
        // 파라미터 파싱
        String room = null;
        String username = null;
        String mode = null;
        String scenename = null;
        String[] bundles = null;
        String appid = null;
        
        String[] splitParam = param.split(" ");
        
        for (String arg : splitParam) 
        {
            if (arg.startsWith("-room=")) room = arg.substring(6);
            if (arg.startsWith("-username=")) username = arg.substring(10);
            if (arg.startsWith("-mode=")) mode = arg.substring(6);
            if (arg.startsWith("-scenename=")) scenename = arg.substring(11);
            if (arg.startsWith("-bundles=")) bundles = arg.substring(9).split("\\.");
            if (arg.startsWith("-appid=")) appid = arg.substring(7);
        }

        if (room == null) {
            System.err.println("방 번호(-room=)가 누락되었습니다.");
            return;
        }
        if(runningServers.containsKey(room))
        {        	
        	System.err.println("이미 같은 방 번호로 서버가 존재한다");
        	return;

        }

        List<String> command = new ArrayList<>();
        command.add(exeServerPath);
        command.addAll(Arrays.asList(splitParam));

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try 
        {   
            Process process = processBuilder.start();
            
            ServerInstance instance = new ServerInstance(process);
            instance.setRoom(room);
            instance.setUsername(username);
            instance.setMode(mode);
            instance.setScenename(scenename);
            instance.setAppid(appid);
            
            if(bundles != null && bundles.length >0)
            {            	
	            List<Long> longList = Arrays.stream(bundles)
	                    .map(Long::parseLong)
	                    .collect(Collectors.toList());	       
	            
	            instance.setBundles(assetBundleService.getBundleByIds(longList));            
            }
            else
            	instance.setBundles(new ArrayList<>());
            
            instance.setStartedAt(LocalDateTime.now());

            runningServers.put(appid+"/"+room, instance);

            System.out.println("Game server started for room: " + room);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(String room) {
        ServerInstance instance = runningServers.get(room);
        if (instance != null) {
            instance.getProcess().destroy();
            runningServers.remove(room);
            System.out.println("Game server stopped for room: " + room);
        }
    }

    public List<ServerInstance> getRunningServers() {
        return new ArrayList<>(runningServers.values());
    }
    
    public List<ServerInstance> getConnectedServers() {
//        return new ArrayList<>(runningServers.values());
        
        return runningServers.values().stream()
        	    .filter(server -> Boolean.TRUE.equals(server.getConnection()))
        	    .collect(Collectors.toList());
    }
    

    public List<Long> getServerBundleIds(String room)
    {
		ServerInstance instance = runningServers.get(room);
    	
    	if (instance == null || instance.getBundles() == null) {
            return Collections.emptyList(); // null 대신 빈 리스트 반환
        }
    	
    	return instance.getBundles().stream()
    			.map(AssetBundleEntity::getId)
                .collect(Collectors.toList());    	
    }
    
    public List<String> getServerBundleDescriptions(String room)
    {
		ServerInstance instance = runningServers.get(room);
    	
    	if (instance == null || instance.getBundles() == null) {
            return Collections.emptyList(); // null 대신 빈 리스트 반환
        }
    	
    	return instance.getBundles().stream()
    			.map(AssetBundleEntity::getBundleDescription)
                .collect(Collectors.toList());    	
    }
    
    public List<String> getServerBundleTitles(String room)
    {
    	ServerInstance instance = runningServers.get(room);
    	System.out.println(instance.getBundles());
    	
    	if (instance == null || instance.getBundles() == null) {
            return Collections.emptyList(); // null 대신 빈 리스트 반환
        }
    	
    	return instance.getBundles().stream()
    			.map(AssetBundleEntity::getBundleTitle)
                .collect(Collectors.toList());
    	
    }
    
    public ServerInstance getRunningServers(String room)
    {
    	return runningServers.get(room);
    }
    public boolean isAlive(String room) {
        ServerInstance instance = runningServers.get(room);
        return instance != null && instance.getProcess().isAlive();
    }

 // 살아있는 서버들의 방 이름 배열을 반환
    public String[] getAliveRoomNames() 
    {
        return runningServers.entrySet().stream()
                .filter(entry -> entry.getValue().isAlive()) // 살아있는 서버만 필터링
                .map(Map.Entry::getKey) // key는 방 이름
                .toArray(String[]::new);
    }
    
    // 주기적 검사용
    public void cleanUpDeadProcesses() {
        runningServers.entrySet().removeIf(entry -> !entry.getValue().getProcess().isAlive());
    }
    
//	public void start( String param) {
//    	
//    	System.out.println("Game server started.");
//    	
////    	List<String> command = Arrays.asList(
////        	    "C:\\sts\\sts-4.20.0.RELEASE\\Workspace\\VirtualStartUP\\uploads\\Server\\VirtualPlatform.exe",
////        	    param  	    
////        	);
//    	
//    	List<String> command = new ArrayList<>();
//    	command.add("C:\\sts\\sts-4.20.0.RELEASE\\Workspace\\VirtualStartUP\\uploads\\Server\\VirtualPlatform.exe");
//    	command.addAll(Arrays.asList(param.split(" ")));
//    	
//    	
//     
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//    	try {
//			Process process = processBuilder.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

    public void start(List<Long> bundleIds, String param) {
    	
//    	System.out.println("Game server started.");
//    	
////    	List<String> command = Arrays.asList(
////        	    "C:\\sts\\sts-4.20.0.RELEASE\\Workspace\\VirtualStartUP\\uploads\\Server\\VirtualPlatform.exe",
////        	    param, 
////        	    "-bundles="+ bundleIds.stream().map(String::valueOf).collect(Collectors.joining("."))        	    
////        	);
//     
//    	List<String> command = new ArrayList<>();
//    	command.add("C:\\sts\\sts-4.20.0.RELEASE\\Workspace\\VirtualStartUP\\uploads\\Server\\VirtualPlatform.exe");
//    	command.addAll(Arrays.asList(param.split(" ")));
//    	command.add("-bundles="+ bundleIds.stream().map(String::valueOf).collect(Collectors.joining(".")));    	
//    	
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//    	try {
//			Process process = processBuilder.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	
    	param += " -bundles="+ bundleIds.stream().
    			map(String::valueOf).
    			collect(Collectors.joining("."));
    	start(param);
    }
//    public void start() {
//        // 서버 시작 로직 (예: shell script 실행)
//        serverRunning = true;
//        System.out.println("Game server started.");
//        
////        Path path = Paths.get("C:", "Users", "a", "Documents", "Unity", "VirtualStartUP", "bin", "MyUnityGame.exe");
//        
//        List<String> command = Arrays.asList(
//        	    "C:\\Users\\a\\Documents\\Unity\\VirtualStartUP\\bin\\VirtualStartUP.exe",
//        	    "-username=admin",
//        	    "-server=192.168.0.100",
//        	    "-room=1010",
//        	    "-mode=admin"
//        	);
//
//    	ProcessBuilder processBuilder = new ProcessBuilder(command);
//    	try {
//			Process process = processBuilder.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

//    public void restart() {
//        // 서버 재시작 로직
//        serverRunning = false;
//        System.out.println("Game server stopping...");
//        serverRunning = true;
//        System.out.println("Game server restarted.");
//    }
//
//    public String getStatus() {
//        return serverRunning ? "Running" : "Stopped";
//    }
}
