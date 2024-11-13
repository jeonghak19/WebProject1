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
            likeButton.innerHTML = `<img class="like-btn" src="/icons/heart-fill.svg">`;
        } else {
            likeButton.innerHTML = `<img class="like-btn" src="/icons/heart.svg">`;
        }
        
        likeCount.textContent = `좋아요: ${data.likeCount}`;
    })
}