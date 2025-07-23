package com.bu.startup.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//import org.hibernate.mapping.Collection;
import com.bu.startup.entity.Post;
import com.bu.startup.type.CategoryType;
import com.bu.startup.type.ItemStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bu.startup.ServerInstance;
import com.bu.startup.VirtualStartUpApplication;
import com.bu.startup.entity.AssetBundleEntity;
import com.bu.startup.repo.AssetBundleRepository;
import com.bu.startup.service.AssetBundleService;
import com.bu.startup.service.GameServerService;
import com.bu.startup.service.RoomService;
import com.bu.startup.service.ServerService;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Sinks;
import reactor.core.publisher.Flux;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.bu.startup.entity.User;
import com.bu.startup.service.LikeService;
import com.bu.startup.service.PostService;
import com.bu.startup.service.UserService;

@RestController
@RequestMapping("/api/assetbundles")
@RequiredArgsConstructor
public class AssetBundleController {

	 public static class AppInfo {
         private String num;
         private String appid; // 여기서는 AppID 자체가 value가 됩니다.

         public AppInfo(String num, String appid) {
             this.num = num;
             this.appid = appid;
         }

         // Getter methods
         public String getAppid() { return appid; }
         public String getNum() { return num; }
     }
	 
	 
	 private final AssetBundleRepository assetBundleRepository;
	 private final AssetBundleService assetBundleService;
	 private final ServerService serverService;
	 private final LikeService likeService;
	 private final PostService postService;
	 private final UserService userService;
	 
	 private final RoomService roomService;
	 private final GameServerService gameServerService;
	 
	 private final Sinks.Many<String> sink = Sinks.many().multicast().directBestEffort();
	 
	 
	@Value("${upload.path}")
	private String uploadPath;

	/**
	 * 단일 파일 + 설명 등록
	 * @param file
	 * @param bundleName
	 * @param bundleTitle
	 * @param bundleDescription
	 * @return
	 */
	    @PostMapping("/bundleUpload")
	    public ResponseEntity<String> uploadBundleDesc(@RequestParam("file") MultipartFile file,
	                                               @RequestParam("bundleName") String bundleName,
	                                               @RequestParam("bundleTitle") String bundleTitle,
	                                               @RequestParam("bundleDescription") String bundleDescription) 
	    {	    	
	    	try {
				AssetBundleEntity abe = assetBundleService.saveAssetBundle(file, bundleName, bundleTitle, bundleDescription);
				sink.tryEmitNext("refresh");  // "refresh" 메시지를 클라이언트에 보냄
					
				return ResponseEntity.ok("Upload Description successful");
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
				
			}
	    }
	    
	    @PostMapping("/bundleUploadPlatform")
	    public ResponseEntity<String> uploadBundlePlatform(@RequestParam("win") MultipartFile winFile,
	    													@RequestParam("and") MultipartFile andFile,
	                                               @RequestParam("bundleName") String bundleName,
	                                               @RequestParam("bundleTitle") String bundleTitle,
	                                               @RequestParam("bundleDescription") String bundleDescription) 
	    {	    	
	    	try {
				AssetBundleEntity abe = assetBundleService.saveAssetBundle(winFile, andFile, bundleName, bundleTitle, bundleDescription);
				sink.tryEmitNext("refresh");  // "refresh" 메시지를 클라이언트에 보냄
					
				return ResponseEntity.ok("Upload Description successful");
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
				
			}
	    }
	    
	    @PostMapping("/upload")
	    public ResponseEntity<String> uploadBundle(@RequestParam("file") MultipartFile file,
	                                               @RequestParam("bundleName") String bundleName) {
	            
	    	try {
				AssetBundleEntity abe = assetBundleService.saveAssetBundle(file, bundleName);
				sink.tryEmitNext("refresh");  // "refresh" 메시지를 클라이언트에 보냄
				return ResponseEntity.ok("Upload successful");
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
				
			}
	    	
//	    	try {
//	            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//	            Path path = Paths.get(uploadPath + File.separator + fileName);
//	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//	            AssetBundleEntity bundle = AssetBundleEntity.builder()
//	                    .bundleName(bundleName)
//	                    .filePath(path.toString())
//	                    .uploadedAt(LocalDateTime.now())
//	                    .build();
//
//	            
//	            assetBundleRepository.save(bundle);
//
//	            return ResponseEntity.ok("Upload successful");
//	        } catch (IOException e) {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
//	        }
	    }

//	    @GetMapping
//	    public List<AssetBundleEntity> getAllBundles() {
//	        return assetBundleRepository.findAll();
//	    }
	    
	    @GetMapping(value = "/sse/refresh", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	    public Flux<String> refreshStream() {
	    	return sink.asFlux();
	    }
	    
	    @GetMapping("/serverNotFound/{appid}")
	    public String serverNotFound(@PathVariable String appid) {
	    	
	    	serverService.start("-room=Lobby -mode=admin -scenename=Lobby -username=admin1 -appid="+appid);
	    	
	    	return appid + " Lobby OK";
	    }
	    
	    @GetMapping("/serverMaxCCU/{appid}")
	    public String serverMaxCCU(@PathVariable String appid) {
	    	
	    	int idx = VirtualStartUpApplication.PhotonAppID.indexOf(appid) + 1;	    	
	    	idx = idx % VirtualStartUpApplication.PhotonAppID.size();	    	
	    	
	    	appid = VirtualStartUpApplication.PhotonAppID.get(idx);
	    	serverService.start("-room=Lobby -mode=admin -scenename=Lobby -username=admin1 -appid=" + appid);
	    	
	    	return appid + " Lobby Chage OK";
	    }
	    
	    
	    
	    
	    @GetMapping("/downloadBundleByName/{bundleName}")
	    public ResponseEntity<byte[]> downloadBundle(@PathVariable String bundleName) {
	    	System.out.println(bundleName);
	    	
	    	Optional<AssetBundleEntity> optional = assetBundleRepository.findByBundleName(bundleName);
	    	 if (optional.isEmpty()) return ResponseEntity.notFound().build();

		        AssetBundleEntity bundle = optional.get();
		        Path path = Paths.get(bundle.getFilePath().get(0));

		        try {
		            byte[] data = Files.readAllBytes(path);
		            return ResponseEntity.ok()
		                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName())
		                    .body(data);
		        } catch (IOException e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		        }
	    }
	    
	    @GetMapping("/downloadBundleByName/{bundleName}/{platform}")
	    public ResponseEntity<byte[]> downloadBundle(@PathVariable String bundleName, @PathVariable String platform) {
	    	System.out.println(bundleName);
	    	
	    	Optional<AssetBundleEntity> optional = assetBundleRepository.findByBundleName(bundleName);
	    	 if (optional.isEmpty()) return ResponseEntity.notFound().build();

		        AssetBundleEntity bundle = optional.get();
		        Path path = Paths.get(bundle.getFilePath().get(0));
		        
		        if(platform.equals("and"))
		        	path = Paths.get(bundle.getFilePath().get(1));

		        try {
		            byte[] data = Files.readAllBytes(path);
		            return ResponseEntity.ok()
		                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName())
		                    .body(data);
		        } catch (IOException e) {
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		        }
	    }
	    
	    
	    @PostMapping("/serverConnectionResult")
	    public String serverConnection(
	    		@RequestParam("appid") String appid ,
	    								@RequestParam("roomName") String roomName ,
	                                   @RequestParam("result") Boolean result) {
	    	
	    	ServerInstance si = serverService.getRunningServers(appid+"/"+roomName);
	    	if(si == null)
	    		return "fail";
	    	
	    	si.setConnection(result);
	    	sink.tryEmitNext("refresh");  // "refresh" 메시지를 클라이언트에 보냄
	    	
	    	return appid+"/"+roomName + "/" + result;	    	
	    }
	    
	    @PostMapping("/getServerBundleIdsList")
	    public List<Long> getServerBundleIdsList(
	    		@RequestParam("appid") String appid ,
	    		@RequestParam("roomName") String roomName) {
	        return serverService.getServerBundleIds(appid+"/"+roomName);
	    }
	    
	    @PostMapping("/getServerBundleDescriptionList")
	    public List<String> getServerBundleDescriptionList(
	    		@RequestParam("appid") String appid ,
	    		@RequestParam("roomName") String roomName) {
	        return serverService.getServerBundleDescriptions(appid+"/"+roomName);
	    }
	    @PostMapping("/getServerBundleTitleList")
	    public List<String> getServerBundleTitleList(
	    		@RequestParam("appid") String appid ,
	    		@RequestParam("roomName") String roomName) {
	    	System.out.println(serverService.getServerBundleTitles(appid+"/"+roomName));
	    	
	    	return serverService.getServerBundleTitles(appid+"/"+roomName);
	    }
	    
	    @GetMapping("/getBundleList")
	    public List<AssetBundleEntity> getBundleList() {
	        return assetBundleService.getAllBundles();
	    }
	    
	    @GetMapping("/serverRoomList")
	    public String[] getRoomNames()
	    {
	    	return serverService.getAliveRoomNames();
	    }


	/**
	 * 관리자용 - 번들 목록 및 서버 관리
	 * @param authentication
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping("/admin/bundleList")
	public ModelAndView listBundles(Authentication authentication) throws JsonProcessingException {

		if(authentication == null) return new ModelAndView("login");

		String userId = authentication.getName();

		ModelAndView mav = new ModelAndView("admin/assetBundle");
		mav.addObject("assetBundles", assetBundleService.getAllBundles());

		String appid = VirtualStartUpApplication.PhotonAppID.get(0);
		String args = String.format(
				"-room=%s -mode=%s -scenename=%s -username=%s",
				"Samsung", "admin", "Room", userId
			);

		mav.addObject("params", args);



		List<AppInfo> appInfoList = IntStream.range(0, VirtualStartUpApplication.PhotonAppID.size())
					 .mapToObj(index -> new AppInfo(String.valueOf(index), VirtualStartUpApplication.PhotonAppID.get(index)))
				 .collect(Collectors.toList());

		mav.addObject("appidList", appInfoList );


		serverService.cleanUpDeadProcesses();
		Collection<ServerInstance> runningServers = serverService.getConnectedServers();

		System.out.println(runningServers);

//			ObjectMapper mapper = new ObjectMapper();
//			    // LocalDateTime 포맷 지정 (선택)
//			mapper.findAndRegisterModules();
//			
//			String serversJson = mapper.writeValueAsString(runningServers);
//			
//			System.out.println(serversJson);
		mav.addObject("serversJson", runningServers);

//	    	mav.addObject("rooms", serverService.getAliveRoomNames());
////	    	mav.addObject("serverStatus", serverService.getStatus());      
////	    	mav.addObject("username", authentication.getName());
//	    	mav.addObject("servers", serverService.getRunningServers()); // NEW
//	    	mav.addObject("rooms", serverService.getAliveRoomNames());

		return mav;
	}

	/**
	 * 번들 목록(창업 아이템 부스) 조회
	 * @param authentication
	 * @return
	 */
	@GetMapping("/bundleList")
	public ModelAndView listBundlesOnly(@RequestParam(required = false) CategoryType category,
										Authentication authentication) {
		if(authentication == null) return new ModelAndView("login");

		List<AssetBundleEntity> bundleList;
		if (category != null) {
			bundleList = assetBundleService.getBundlesByCategory(category);
		} else {
			bundleList = assetBundleService.getAllBundles();
		}

		ModelAndView mav = new ModelAndView("/bundleList");
		mav.addObject("bundleList", bundleList);
		mav.addObject("categories", CategoryType.values());
		mav.addObject("statuses", ItemStatus.values());
		return mav;
	}
	    
	@GetMapping("/{id}")
	public ModelAndView getBundleDetail(@PathVariable Long id, Authentication authentication) {
		ModelAndView mav = new ModelAndView("bundleDetail");
		AssetBundleEntity bundle = assetBundleService.getBundleById(id)
				.orElseThrow(() -> new IllegalArgumentException("AssetBundle not found"));
		mav.addObject("bundle", bundle);

		if (authentication != null && authentication.isAuthenticated()) {
			User currentUser = userService.getUserByUsername(authentication.getName());
			if (currentUser != null) {
				mav.addObject("isLiked", likeService.isLiked(id, currentUser));
			}
		}
		mav.addObject("likeCount", likeService.getLikeCount(id));
		List<Post> postList = postService.getPostsByAssetBundleId(id);
		mav.addObject("postList", postList);
		return mav;
	}

	@PostMapping("/{id}/like")
	public ResponseEntity<Void> toggleLike(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		User currentUser = userService.getUserByUsername(userDetails.getUsername());
		if (currentUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		likeService.toggleLike(id, currentUser);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/killServer")
	public ResponseEntity<Void> killServer(@RequestParam(value = "serverIds", required = false) List<String> roomNames)
	{
		if(roomNames == null || roomNames.isEmpty())
			return getRedirect("/api/assetbundles/bundleList"); // 302 Redirect


		for (String room : roomNames) {

			serverService.stop(room);

			System.out.println("종료 요청된 방: " + room);
		// 해당 room 이름을 기준으로 서버 종료 처리
		}

		sink.tryEmitNext("refresh");  // "refresh" 메시지를 클라이언트에 보냄

		return getRedirect("/api/assetbundles/bundleList"); // 302 Redirect
	}

	@PostMapping("/deleteSelected")
	public  ResponseEntity<Void> deleteSelected(@RequestParam(value = "bundleIds", required = false) List<Long> bundleIds) {

		if(bundleIds == null || bundleIds.isEmpty())
			return getRedirect("/api/assetbundles/bundleList"); // 302 Redirect

		assetBundleService.deleteByIds(bundleIds);

		sink.tryEmitNext("refresh");  // "refresh" 메시지를 클라이언트에 보냄

		return getRedirect("/api/assetbundles/bundleList"); // 302 Redirect
	}

	@PostMapping("/findSelectedWithServer")
	public  ResponseEntity<Void> findSelected(
			@RequestParam(value = "bundleIds", required = false) List<Long> bundleIds,
			@RequestParam(value = "appIds", required = false) List<Long> appIds,
			@RequestParam(value = "serverParam", required = false) String serverParam)
	{
//	        List<AssetBundleEntity> list = assetBundleService.getBundleByIds(bundleIds);

		if(appIds != null )
			serverParam += " -appid=" + VirtualStartUpApplication.PhotonAppID.get((int)(long)appIds.get(0));

		if(bundleIds == null)
			serverService.start(serverParam);

//	    	System.out.println("appIds : " + appIds.toString()); 

		if(bundleIds != null && bundleIds.size()>0)
			serverService.start(bundleIds, serverParam);


		return getRedirect("/api/assetbundles/bundleList"); // 302 Redirect
	}


	ResponseEntity<Void> getRedirect(String url)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(url));
		return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redire
	}

	@GetMapping("/downloadBundleById/{id}")
	public ResponseEntity<byte[]> downloadBundleById(@PathVariable Long id) {
		Optional<AssetBundleEntity> optional = assetBundleRepository.findById(id);
		if (optional.isEmpty()) return ResponseEntity.notFound().build();

		AssetBundleEntity bundle = optional.get();
		Path path = Paths.get(bundle.getFilePath().get(0));

		try {
			byte[] data = Files.readAllBytes(path);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName())
					.body(data);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/downloadBundleById/{id}/{platform}")
	public ResponseEntity<byte[]> downloadBundleById(@PathVariable Long id, @PathVariable String platform) {
		Optional<AssetBundleEntity> optional = assetBundleRepository.findById(id);
		if (optional.isEmpty()) return ResponseEntity.notFound().build();

		AssetBundleEntity bundle = optional.get();
		Path path = Paths.get(bundle.getFilePath().get(0));

		if(platform.equals("and"))
			path = Paths.get(bundle.getFilePath().get(1));

		try {
			byte[] data = Files.readAllBytes(path);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName())
					.body(data);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
