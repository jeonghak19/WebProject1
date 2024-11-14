let isNameAvailable = false;

document.getElementById('checkNameBtn').addEventListener('click', function() {
    const name = document.getElementById('name').value;
    const nameCheckResult = document.getElementById('nameCheckResult');

    if (name.trim().length < 2) {
        nameCheckResult.textContent = "이름을 입력해주세요.";
        nameCheckResult.style.color = "red";
        isNameAvailable = false;
        toggleRegisterButton();
        return;
    }

    fetch('/user/check-name?name=' + name)
        .then(response => response.json())
        .then(data => {
            if (data.isAvailable) {
                nameCheckResult.textContent = "사용 가능한 이름입니다.";
                nameCheckResult.style.color = "blue";
                isNameAvailable = true;
            } else {
                nameCheckResult.textContent = "이미 존재하는 이름입니다.";
                nameCheckResult.style.color = "red";
                isNameAvailable = false;
            }
            toggleRegisterButton();
        })
        .catch(error => {
            nameCheckResult.textContent = "오류가 발생했습니다. 다시 시도해주세요.";
            nameCheckResult.style.color = "red";
            isNameAvailable = false;
            toggleRegisterButton();
        });
});

document.querySelector('form').addEventListener('submit', function(event) {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm_password').value;

    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        event.preventDefault();
    }
});

function toggleRegisterButton() {
    document.getElementById('registerBtn').disabled = !isNameAvailable;
}