// navbar.js
document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const loginSuccess = urlParams.get('loginSuccess');

    if (loginSuccess === 'true') {
        alert("로그인 성공!");
        urlParams.delete('loginSuccess');
        const newUrl = window.location.pathname + '?' + urlParams.toString();
        window.history.replaceState({}, document.title, newUrl);
    }
});
