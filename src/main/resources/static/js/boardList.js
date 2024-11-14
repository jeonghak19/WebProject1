//boardList.js
	let currentIdx = 0;
	const slides = document.querySelectorAll('.kind_wrap .slider li');
	const totalSlides = slides.length;
	const slider = document.querySelector('.kind_wrap .slider');
	const dots = document.querySelectorAll('.pagination .dot');
	let autoSlideInterval;

	//슬라이드 이동 함수
	function moveSlide(index) {
		currentIdx = index;
		// 템플릿 리터럴을 사용하여 올바르게 문자열을 작성합니다.
		slider.style.transform = `translateX(-${currentIdx * 100}%)`;
		updatePagination();
	}


	// 페이지네이션 상태 업데이트 함수
	function updatePagination() {
		dots.forEach(dot => dot.classList.remove('active'));
		dots[currentIdx].classList.add('active');
	}

	// 자동 슬라이드 시작
	function startAutoSlide() {
		autoSlideInterval = setInterval(() => {
			currentIdx = (currentIdx + 1) % totalSlides;
			moveSlide(currentIdx);
		}, 3000);
	}

	// 자동 슬라이드 초기화
	function resetAutoSlide() {
		clearInterval(autoSlideInterval);
		startAutoSlide();
	}

	// 페이지네이션 클릭 이벤트
	dots.forEach(dot => {
		dot.addEventListener('click', (event) => {
			const index = parseInt(event.target.getAttribute('data-index'));
			moveSlide(index);
			resetAutoSlide();
		});
	});

	// 페이지 로드 시 자동 슬라이드 시작
	startAutoSlide();
