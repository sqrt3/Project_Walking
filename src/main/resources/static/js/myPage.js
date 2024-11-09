const userId = window.location.pathname.split("/")[2];

function loadUserInfo() {
    fetch(`/api/users/${userId}/myPage`)
        .then(response => response.json())
        .then(data => {
            document.getElementById("userName").innerText = `${data.name}님의 마이페이지`;
            document.getElementById("nickname").innerText = data.nickname;
            document.getElementById("followerCount").innerText = data.followers;
            document.getElementById("followingCount").innerText = data.following;
            document.getElementById("profileImg").src = data.profileImage;
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

                // 클릭 시 해당 사용자의 마이페이지로 이동
                div.addEventListener("click", () => {
                    console.log(follower.userId);
                    window.location.href = `/myPage/info/${follower.userId}`;
                });

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

                // 클릭 시 해당 사용자의 마이페이지로 이동
                div.addEventListener("click", () => {
                    console.log(following.userId);
                    window.location.href = `/myPage/info/${following.userId}`;
                });

                div.appendChild(img);
                div.appendChild(name);
                followingList.appendChild(div);
            });
        });
}

function updateUserInfo() {
    const password = document.getElementById("password").value;
    const phone = document.getElementById("phone").value;
    const nickname = document.getElementById("nicknameInput").value;
    const profileImageFile = document.getElementById("profileImage").files[0]; // 파일 선택

    // JSON 데이터를 문자열로 직렬화하여 FormData에 추가
    const formData = new FormData();
    const updateData = JSON.stringify({ password, phone, nickname });
    formData.append("update", new Blob([updateData], { type: "application/json" }));

    // 이미지 파일을 FormData에 추가
    if (profileImageFile) {
        formData.append("profileImage", profileImageFile);
    }

    fetch(`/api/users/${userId}`, {
        method: 'PUT',
        body: formData
    })
        .then(response => response.text())
        .then(() => {
            alert("정보가 업데이트되었습니다.");
            window.location.href = `/myPage/${userId}`;
        })
        .catch(error => console.error("오류 발생:", error));
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
        .then(response => response.text())
        .then(title => {
            console.log(title);
            const recentPostsList = document.getElementById("recentPostsList");
            recentPostsList.innerHTML = "";

            // 제목이 있으면 출력, 없으면 "최근 본 게시글이 없습니다" 출력
            const div = document.createElement("div");
            div.textContent = title || "최근 본 게시글이 없습니다.";
            recentPostsList.appendChild(div);
        });
}

function deleteAccount() {
    fetch(`/api/users/${userId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                alert("회원 탈퇴가 완료되었습니다.");
                window.location.href = "/"; // 홈 페이지로 리다이렉트
            } else {
                alert("탈퇴 처리 중 오류가 발생했습니다.");
            }
        })
        .catch(error => {
            console.error("오류 발생:", error);
            alert("탈퇴 처리 중 오류가 발생했습니다.");
        });
}


document.addEventListener("DOMContentLoaded", () => {
    loadUserInfo();
    document.getElementById("followerLink").onclick = () => loadFollowerList();
    document.getElementById("followingLink").onclick = () => loadFollowingList();
    $('#myItemsModal').on('shown.bs.modal', () => loadUserItems(userId));
    $('#myPointLogsModal').on('shown.bs.modal', () => loadPointLogs(userId));
    loadRecentPosts(userId);
});
