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




document.addEventListener("DOMContentLoaded", function() {
    // 수정 버튼 클릭 시 모달에 댓글 정보 채우기
    const editButtons = document.querySelectorAll('.btn-warning');
    editButtons.forEach(button => {
        button.addEventListener('click', function() {
            const commentId = this.getAttribute('data-comment-id');
            const commentContent = this.getAttribute('data-comment-content');
            const postId = this.getAttribute('data-post-id'); // postId를 가져옴
            
            // 모달에 댓글 정보 채우기
            document.getElementById('commentId').value = commentId;
            document.getElementById('editCommentContent').value = commentContent;
            document.getElementById('postId').value = postId; // postId도 채우기
        });
    });

    // 수정 폼 제출
    const form = document.getElementById('editCommentForm');
    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const commentId = document.getElementById('commentId').value;
        const postId = document.getElementById('postId').value;
        const newContent = document.getElementById('editCommentContent').value;

        // 서버로 수정된 댓글 내용 전송 (AJAX 사용)
        fetch(`/post/posts/${postId}/comments/${commentId}/edit`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ content: newContent })  // JSON 형식으로 데이터 전송
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('댓글이 수정되었습니다.');
                location.reload();  // 새로고침으로 댓글 내용 반영
            } else {
                alert('수정에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });
});


function confirmDelete() {
    // 확인 창 띄우기
    const isConfirmed = confirm("정말로 삭제하시겠습니까?");
    
    if (isConfirmed) {
        // 사용자가 '네'를 클릭하면 form 제출
        document.getElementById('deleteForm').submit();
    }
}

