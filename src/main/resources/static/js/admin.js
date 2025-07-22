document.addEventListener('DOMContentLoaded', 

function () {
	
	
    console.log("Admin page loaded.");
    
    const fileInput = document.getElementById('fileInput');
    const dropZone = document.getElementById('dropZone');
    const textInput = document.getElementById('executablePath');

    // Check if elements exist before adding event listeners
    if (dropZone && textInput) {
        // 기본 동작 방지
        ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
            dropZone.addEventListener(eventName, e => e.preventDefault(), false);
            document.body.addEventListener(eventName, e => e.preventDefault(), false);
        });

        dropZone.addEventListener('dragover', () => dropZone.classList.add('drag-over'));
        dropZone.addEventListener('dragleave', () => dropZone.classList.remove('drag-over'));

        dropZone.addEventListener('drop', (e) => {		
		
            dropZone.classList.remove('drag-over');
            const files = e.dataTransfer.files;
            if (files.length > 0) {
			
                const file = files[0];           
                // Windows 기반 Electron 앱이라면 file.path, 웹은 file.name만 제공
                textInput.value = file.path || file.name;
            }
        });

        dropZone.addEventListener('click', () => {
		    if (textInput.files && textInput.files.length > 0) { // Check if textInput.files exists
                    const file = textInput.files[0];
                    alert("선택된 파일: " + file.name);
                    // 또는 file.name을 다른 곳에 표시 가능
                }
                
            alert("텍스트 입력창에 직접 경로를 붙여넣기 하세요.\n(브라우저는 시스템 경로 접근을 제한합니다)");
        });
    }
});