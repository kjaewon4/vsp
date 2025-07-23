// bundleDetail.js

// Thymeleaf로 렌더링될 때 아래 구문이 그대로 변환됨
const bundleId = document.querySelector('.detail-container').dataset.bundleId;
const likeButton = document.getElementById('likeButton');
const likeCountSpan = document.getElementById('likeCount');
const postForm = document.getElementById('postForm');

document.querySelector('.btn-add-post').addEventListener('click', function () {
    // Bootstrap 5 모달 인스턴스 가져오기
    const modalEl = document.getElementById('postModal');
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
});

// 좋아요 버튼 클릭 이벤트
if (likeButton) {
    likeButton.addEventListener('click', async () => {
        const response = await fetch(`/api/assetbundles/${bundleId}/like`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });
        if (response.ok) {
            const isLiked = likeButton.classList.toggle('liked');
            let currentLikes = parseInt(likeCountSpan.textContent);
            if (isLiked) {
                likeCountSpan.textContent = currentLikes + 1;
                likeButton.querySelector('span').textContent = '좋아요 취소';
            } else {
                likeCountSpan.textContent = currentLikes - 1;
                likeButton.querySelector('span').textContent = '좋아요';
            }
        } else if (response.status === 401) {
            alert('로그인이 필요합니다.');
            window.location.href = '/login';
        } else {
            alert('좋아요 처리 중 오류가 발생했습니다.');
        }
    });
}

document.querySelectorAll('.tab-btn').forEach((btn, i) => {
    btn.addEventListener('click', () => {
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.tab-panel').forEach((p, pi) => p.classList.toggle('active', pi === i));
        btn.classList.add('active');
    });
});

// 게시글 폼 제출 이벤트 리스너
if (postForm) {
    postForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const formData = new FormData(postForm);
        const assetBundleId = formData.get('assetBundleId');
        const title = formData.get('title');
        const content = formData.get('content');

        const response = await fetch('/posts', {
            method: 'POST',
            body: new URLSearchParams({ assetBundleId, title, content }).toString(),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        });

        if (response.ok) {
            alert('게시글이 작성되었습니다.');
            const postModal = bootstrap.Modal.getInstance(document.getElementById('postModal'));
            if (postModal) {
                postModal.hide();
            }
            window.location.reload();
        } else if (response.status === 401) {
            alert('로그인이 필요합니다.');
            window.location.href = '/login';
        } else {
            alert('게시글 작성 중 오류가 발생했습니다.');
        }
    });
}
