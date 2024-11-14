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

// 파일 업로드 시 파일 이름 가져오기
function changeFile(target) {
	var file = target.files[0];
	document.getElementById("fileName").innerText = file.name;
};

function openFileDialog() {
	document.getElementById("file").click();
};


// 게시글 내용 textarea 자동으로 크기 조절
function autoResizing(textarea) {
	const scrollPosition = window.scrollY;
	textarea.style.height = 'auto';
	textarea.style.height = (textarea.scrollHeight) + 'px';
	window.scrollTo(0, scrollPosition);
};

// 좋아요 버튼 클릭 함수 호출 
function toggleLike() {
    const form = document.getElementById('likeForm');
    const postId = form.querySelector('input[name="postId"]').value;
    const userId = form.querySelector('input[name="userId"]').value;

    fetch(`/post/posts/like`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ postId: postId, userId: userId })
		    })
		    .then(response => response.json())
		    .then(data => {
		        const likeButton = document.getElementById('likeButton');
		        const likeCount = document.getElementById('likeCount');
		        
		        if (data.liked) {
		            likeButton.innerHTML = `<img class="like-btn" src="/icons/heart-fill.png">`;
		        } else {
		            likeButton.innerHTML = `<img class="like-btn" src="/icons/heart.png">`;
		        }
		        
		        likeCount.textContent = `좋아요: ${data.likeCount}`;
		    })
		}