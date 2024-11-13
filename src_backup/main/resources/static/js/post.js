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