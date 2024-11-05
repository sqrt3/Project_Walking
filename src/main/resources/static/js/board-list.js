let currentPage = 1; // 현재 페이지 추적
let pageCount = 0; // 총 페이지 수
const pagesToShow = 10; // 한 번에 표시할 페이지 수

document.addEventListener('DOMContentLoaded', function () {
    const boardIdInput = document.getElementById('boardId');

    // 게시판 목록 가져오기
    fetch('/api/boards')
        .then(handleResponse)
        .then(data => {
            const boardList = document.getElementById('board-list');
            data.forEach(board => {
                const li = document.createElement('li');
                const a = document.createElement('a');
                a.href = `#`;
                a.textContent = board.name;
                a.addEventListener('click', function (event) {
                    event.preventDefault();
                    boardIdInput.value = board.boardId; // 현재 게시판 업데이트
                    updateBoardContent(board.boardId);
                });
                li.appendChild(a);
                boardList.appendChild(li);
            });
        })
        .catch(error => handleError(error, '게시판 목록을 가져오는 중에 문제가 발생했습니다.'));

    // 초기 로딩 시 데이터 가져오기
    updateBoardContent(boardIdInput.value);

    // 검색 기능
    document.getElementById('search-form').addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(this); // 현재 form의 데이터를 가져옴 (검색 옵션, 내용)
        const boardId = boardIdInput.value;
        const searchCategory = formData.get('searchCategory');
        const searchKeyword = formData.get('searchKeyword');
        let searchParams = `boardId=${boardId}&page=1`;

        if (searchCategory === 'title') {
            searchParams += `&title=${searchKeyword}`;
        } else if (searchCategory === 'content') {
            searchParams += `&content=${searchKeyword}`;
        } else if (searchCategory === 'title-content') {
            searchParams += `&title=${searchKeyword}&content=${searchKeyword}`;
        } else if (searchCategory === 'nickname') {
            searchParams += `&nickname=${searchKeyword}`;
        }

        fetch(`/api/posts/search?${searchParams}`)
            .then(handleResponse)
            .then(data => {
                if (!data || data.length === 0) {
                    displayNoPostsMessage('검색 결과가 없습니다.');
                } else {
                    displayPosts(data);
                }
            })
            .catch(error => handleError(error, '게시물을 검색하는 중에 문제가 발생했습니다.'));
    });
});

function updateBoardContent(boardId) {
    fetchPopularPosts(boardId);
    fetchRecentPosts(boardId); // 초기 게시물 가져오기
    updatePagination(boardId);
    fetchNotices();
}

function fetchNotices() {
    fetch('/api/boards/notices')
        .then(handleResponse)
        .then(data => {
            const noticeList = document.getElementById('notice-list');
            noticeList.innerHTML = '';
            data.forEach(notice => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <h4>${notice.title}</h4>
                    <p>${notice.content}</p>
                    <span>작성일: ${notice.createdAt}</span>`;
                noticeList.appendChild(li);
            });
        })
        .catch(error => handleError(error, '공지사항을 가져오는 중에 문제가 발생했습니다.'));
}

function fetchPopularPosts(boardId) {
    fetch(`/api/posts/hot/${boardId}`)
        .then(response => {
            if (response.status === 204) {
                displayNoPopularPostsMessage('주간 인기글이 없습니다.');
                return null;
            }
            return handleResponse(response);
        })
        .then(data => {
            if (data) {
                const popularPosts = document.getElementById('popular-posts');
                popularPosts.innerHTML = `
                    <h4>${data.title}</h4>
                    <p>${data.content}</p>
                    <img src="https://placehold.co/30x30" alt="Placeholder Image">
                    <p>작성일: ${data.createdAt}</p>
                    <p>조회수: ${data.viewCount}</p>
                    <p>좋아요: ${data.likes}</p>
                    <p>댓글 수: ${data.commentsCount}</p>
                    <p>작성자: ${data.nickname}</p>`;
            }
        })
        .catch(error => handleError(error, '인기 게시물을 가져오는 중에 문제가 발생했습니다.'));
}

function fetchRecentPosts(boardId, page = 1) {
    fetch(`/api/boards/posts?boardId=${boardId}&page=${page}`)
        .then(response => {
            if (response.status === 204) {
                displayNoPostsMessage('게시판이 비어 있습니다.');
                return null;
            }
            return handleResponse(response);
        })
        .then(data => {
            if (data) {
                displayPosts(data);
            }
        })
        .catch(error => handleError(error, '게시물 목록을 가져오는 중에 문제가 발생했습니다.'));
}

function updatePagination(boardId) {
    fetch(`/api/boards/${boardId}/pagescount`)
        .then(handleResponse)
        .then(count => {
            pageCount = count; // 총 페이지 수 업데이트
            displayPagination(); // 페이지네이션 표시
        })
        .catch(error => handleError(error, '페이지 수를 가져오는 중에 문제가 발생했습니다.'));
}

function displayPagination() {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = ''; // 이전 페이지네이션 내용 지우기

    // 이전 버튼 추가
    if (currentPage > 1) {
        const prevButton = document.createElement('a');
        prevButton.href = `#`;
        prevButton.textContent = '이전';
        prevButton.addEventListener('click', function (event) {
            event.preventDefault();
            currentPage -= pagesToShow; // 이전 세트로 이동
            if (currentPage < 1) {
                currentPage = 1; // 1 페이지 이상으로 내려가지 않도록 보장
            }
            fetchRecentPosts(document.getElementById('boardId').value, currentPage);
            displayPagination(); // 페이지네이션 표시 갱신
        });
        pagination.appendChild(prevButton); // 이전 버튼을 페이지네이션에 추가
    }

    const startPage = Math.floor((currentPage - 1) / pagesToShow) * pagesToShow + 1;
    const endPage = Math.min(startPage + pagesToShow - 1, pageCount);

    // 페이지 번호 표시
    for (let i = startPage; i <= endPage; i++) {
        const a = document.createElement('a');
        a.href = `#`;
        a.textContent = i;
        a.className = i === currentPage ? 'active' : ''; // 현재 페이지 강조
        a.addEventListener('click', function (event) {
            event.preventDefault();
            currentPage = i; // 현재 페이지 업데이트
            fetchRecentPosts(document.getElementById('boardId').value, currentPage);
            displayPagination(); // 페이지네이션 표시 갱신
        });
        pagination.appendChild(a);
    }

    // 다음 버튼 표시
    if (endPage < pageCount) {
        const nextButton = document.createElement('a');
        nextButton.href = `#`;
        nextButton.textContent = '다음';
        nextButton.addEventListener('click', function (event) {
            event.preventDefault();
            currentPage += pagesToShow; // 다음 세트로 이동
            if (currentPage > pageCount) {
                currentPage = pageCount; // 총 페이지를 초과하지 않도록 보장
            }
            fetchRecentPosts(document.getElementById('boardId').value, currentPage);
            displayPagination(); // 페이지네이션 표시 갱신
        });
        pagination.appendChild(nextButton); // 다음 버튼을 페이지네이션에 추가
    }
}

function displayPosts(posts) {
    const postList = document.getElementById('post-list');
    postList.innerHTML = ''; // 이전 게시물 지우기

    posts.forEach(post => {
        const li = document.createElement('li');
        li.innerHTML = `
            <h4>${post.title}</h4>
            <p>${post.content}</p>
            <img src="https://placehold.co/30x30" alt="Placeholder Image">
            <p>작성일: ${post.createdAt}</p>
            <p>조회수: ${post.viewCount}</p>
            <p>좋아요: ${post.likes}</p>
            <p>댓글 수: ${post.commentsCount}</p>
            <p>작성자: ${post.nickname}</p>`;
        postList.appendChild(li);
    });
}

function handleResponse(response) {
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
}

function handleError(error, contextMessage) {
    console.error('Error:', error);
    displayNoPostsMessage(`${contextMessage} - ${error.message}`);
}

function displayNoPostsMessage(message) {
    const postList = document.getElementById('post-list');
    postList.innerHTML = `<p>${message}</p>`;
}

function displayNoPopularPostsMessage(message) {
    const popularPosts = document.getElementById('popular-posts');
    popularPosts.innerHTML = `<p>${message}</p>`;
}
