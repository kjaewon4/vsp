document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const modal = document.getElementById('resultModal');
    const modalMsg = document.getElementById('modalMsg');
    const modalBtn = document.getElementById('modalBtn');

    form.addEventListener('submit', async function(e) {
        e.preventDefault();

        const formData = new FormData(form);

        const response = await fetch(form.action, {
            method: 'POST',
            body: formData
        });

        const data = await response.json();

        if (data.success) {
            // 모달 메시지 분기
            modalMsg.textContent = data.isAdmin ? "번들 등록이 완료되었습니다." : "번들 등록 요청이 완료되었습니다.";
            modal.style.display = "block";  // 모달 표시
        } else {
            alert(data.message || "업로드 실패");
        }
    });

    // 모달 확인 버튼 클릭시 폼 초기화 및 리다이렉트
    modalBtn.addEventListener('click', function() {
        modal.style.display = "none";
        form.reset();
        window.location.href = "/api/assetbundles/bundleList";
    });
});
