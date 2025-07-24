// home.js
document.addEventListener('DOMContentLoaded', () => {
    // 앵커 클릭 시 부드러운 스크롤
    document.querySelectorAll('a.nav-link[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({ behavior: 'smooth' });
            }
            // 모바일 메뉴 닫기
            const navCollapse = document.querySelector('.navbar-collapse');
            if (navCollapse.classList.contains('show')) {
                new bootstrap.Collapse(navCollapse).hide();
            }
        });
    });
});
