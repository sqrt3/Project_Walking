// URL에서 userId 추출
const userId = window.location.pathname.split("/")[2];  // '/mypage/{userId}'에서 userId 가져오기

function loadUserInfo() {
    fetch(`/api/users/${userId}/mypage`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("userName").innerText = `${data.name}님의 마이페이지`;
            document.getElementById("nickname").innerText = data.nickname;
            document.getElementById("followerCount").innerText = data.followers;
            document.getElementById("followingCount").innerText = data.following;
            document.getElementById("profileImage").src = data.profileImage;
            document.getElementById("userEmail").innerText = data.email;
            document.getElementById("userPhone").innerText = data.phone;
        });
}

function loadFollowerList() {
    fetch(`/api/users/${userId}/follower`)
        .then(response => response.json())
        .then(data => {
            const followerList = document.getElementById("followerList");
            followerList.innerHTML = ""; // 기존 리스트 초기화
            data.forEach(follower => {
                const div = document.createElement("div");
                div.classList.add("follower-item");

                const img = document.createElement("img");
                img.src = follower.profileImage;
                img.classList.add("profile-img");

                const name = document.createElement("span");
                name.textContent = follower.nickname;
                name.classList.add("nickname");

                div.appendChild(img);
                div.appendChild(name);
                followerList.appendChild(div);
            });
        });
}

function loadFollowingList() {
    fetch(`/api/users/${userId}/following`)
        .then(response => response.json())
        .then(data => {
            const followingList = document.getElementById("followingList");
            followingList.innerHTML = "";
            data.forEach(following => {
                const div = document.createElement("div");
                div.classList.add("following-item");

                const img = document.createElement("img");
                img.src = following.profileImage;
                img.classList.add("profile-img");

                const name = document.createElement("span");
                name.textContent = following.nickname;
                name.classList.add("nickname");

                div.appendChild(img);
                div.appendChild(name);
                followingList.appendChild(div);
            });
        });
}

function updateUserInfo() {
    const phone = document.getElementById("phone").value;
    const nickname = document.getElementById("nickname").value;

    fetch(`/api/users/${userId}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({phone, nickname})
    })
        .then(response => response.text())
        .then(() => alert("정보가 업데이트되었습니다."));
        window.location.href = `/mypage/${userId}`;
}

function loadUserItems() {
    const itemList = document.getElementById("itemList");
    itemList.innerHTML = "";
    fetch(`/api/users/${userId}/items`)
        .then(response => response.json())
        .then(data => {
            data.forEach(item => {
                const div = document.createElement("div");
                div.textContent = `${item.name} x ${item.amount}`;
                itemList.appendChild(div);
            });
        });
}

function loadPointLogs() {
    const pointLogs = document.getElementById("pointLogs");
    pointLogs.innerHTML = "";
    fetch(`/api/users/${userId}/points`)
        .then(response => response.json())
        .then(data => {
            data.forEach(log => {
                const div = document.createElement("div");
                div.textContent = log.description;
                pointLogs.appendChild(div);
            });
        });
}

function loadRecentPosts() {
    fetch(`/api/users/${userId}/recent-post`)
        .then(response => response.json())
        .then(postId => {
            const recentPostsList = document.getElementById("recentPostsList");
            recentPostsList.innerHTML = "";

            if (postId) {
                const div = document.createElement("div");
                div.textContent = `최근 게시물 ID: ${postId}`;
                recentPostsList.appendChild(div);
            } else {
                const div = document.createElement("div");
                div.textContent = "최근 게시물이 없습니다.";
                recentPostsList.appendChild(div);
            }
        });
}

document.addEventListener("DOMContentLoaded", () => {
    loadUserInfo();
    document.getElementById("followerLink").onclick = loadFollowerList;
    document.getElementById("followingLink").onclick = loadFollowingList;
    $('#myItemsModal').on('shown.bs.modal', loadUserItems);
    $('#myPointLogsModal').on('shown.bs.modal', loadPointLogs);
    loadRecentPosts();
});
