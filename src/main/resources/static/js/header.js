document.addEventListener('DOMContentLoaded', function () {
    // 게시판 목록을 서버에서 받아와서 드롭다운에 추가하는 함수
    fetch('/api/boards')
        .then(response => response.json())
        .then(boardList => {
            if (boardList.length > 0) {
                const boardDropdownMenu = document.getElementById('boardDropdownMenu');
                boardList.forEach(board => {
                    const boardItem = document.createElement('li');
                    const boardLink = document.createElement('a');
                    boardLink.classList.add('dropdown-item');
                    boardLink.href = `/boardList?boardId=${board.boardId}`;
                    boardLink.textContent = board.name;
                    boardItem.appendChild(boardLink);
                    boardDropdownMenu.appendChild(boardItem);
                });
            } else {
                // 게시판 목록이 없으면 "게시판 없음" 메시지 추가
                const boardDropdownMenu = document.getElementById('boardDropdownMenu');
                const noBoardItem = document.createElement('li');
                noBoardItem.classList.add('dropdown-item');
                noBoardItem.textContent = '게시판 없음';
                boardDropdownMenu.appendChild(noBoardItem);
            }
        })
        .catch(error => console.error('게시판 목록을 가져오는 데 실패했습니다:', error));
});