


function toggleAll(source) {
    const checkboxes = document.querySelectorAll('input[name="bundleIds"]');
    checkboxes.forEach(cb => cb.checked = source.checked);
}

function copySelectedBundles() {
    const checkboxes = document.querySelectorAll('input[name="bundleIds"]:checked');
    const radios = document.querySelectorAll('input[name="appIds"]:checked');
    const containerBundles = document.getElementById("selectedBundles");
    const containerAppids = document.getElementById("selectedAppid");
	
	
    containerBundles.innerHTML = ""; // 기존 제거
    containerAppids.innerHTML = ""; // 기존 제거

    checkboxes.forEach(cb => {
        const hidden = document.createElement("input");
        hidden.type = "hidden";
        hidden.name = "bundleIds";
        hidden.value = cb.value;
        containerBundles.appendChild(hidden);
    });
	
	radios.forEach(rd => {	
        const hidden = document.createElement("input");
        hidden.type = "hidden";
        hidden.name = "appIds";
        hidden.value = rd.value;
        containerAppids.appendChild(hidden);
    });
	
}


function confirmKill() {
    const selectedRooms = getSelectedServerRooms();
    if (selectedRooms.length === 0) {
        alert("최소 하나의 방을 선택해주세요.");
        return false;
    }
    return confirm("선택한 서버들을 종료하시겠습니까?");
}


const eventSource = new EventSource("/api/assetbundles/sse/refresh");

eventSource.onmessage = function(event) {
  if(event.data === "refresh") {
    console.log("리프레시 신호 수신, 페이지 리로드!");
    window.location.reload();  // 페이지 리프레시
  }
};

eventSource.onerror = function(err) {
  console.error("SSE 에러", err);
};
