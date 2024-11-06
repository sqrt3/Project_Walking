// URL에서 userId 추출
const userId = window.location.pathname.split("/")[2];  // '/mypage/{userId}'에서 userId 가져오기


function loadUserInfo() {
    fetch(`/users/${userId}/mypage`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("userName").innerText = `${data.name}님의 마이페이지`;
            document.getElementById("followerCount").innerText = data.followers;
            document.getElementById("followingCount").innerText = data.following;
            document.getElementById("profileImage").src = data.profileImage;
            document.getElementById("userEmail").innerText = data.email;
            document.getElementById("userPhone").innerText = data.phone;
        });
}

function loadFollowerList() {
    fetch(`/users/${userId}/follower`)
        .then(response => response.json())
        .then(data => {
            const followerList = document.getElementById("followerList");
            followerList.innerHTML = ""; // 기존 리스트 초기화
            data.forEach(follower => {
                console.log(data);
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
    fetch(`/users/${userId}/following`)
        .then(response => response.json())
        .then(data => {
            const followingList = document.getElementById("followingList");
            followingList.innerHTML = "";
            data.forEach(following => {
                console.log(data);
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

    fetch(`/users/${userId}`, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({phone, nickname})
    })
        .then(response => response.text())
        .then(() => alert("정보가 업데이트되었습니다."));
}

function loadUserItems() {
    const itemList = document.getElementById("itemList");
    itemList.innerHTML = "";
    fetch(`/users/${userId}/items`)
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
    fetch(`/users/${userId}/points`)
        .then(response => response.json())
        .then(data => {
            data.forEach(log => {
                const div = document.createElement("div");
                div.textContent = log.description;
                pointLogs.appendChild(div);
            });
        });
}

document.addEventListener("DOMContentLoaded", () => {
    loadUserInfo();
    document.getElementById("followerLink").onclick = loadFollowerList;
    document.getElementById("followingLink").onclick = loadFollowingList;
    $('#myItemsModal').on('shown.bs.modal', loadUserItems);
    $('#myPointLogsModal').on('shown.bs.modal', loadPointLogs);
});
