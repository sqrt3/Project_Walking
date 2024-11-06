const userId = window.location.pathname.split("/")[3]; // URL에서 userId 추출

window.onload = function () {
    // 유저 상세 정보 API 호출
    fetch(`/api/users/${userId}/info`)
        .then(response => response.json())
        .then(data => {
            const userDetailContainer = document.getElementById("userDetailContainer");
            userDetailContainer.innerHTML = `
                        <h2>${data.nickname}</h2>
                        <img src="${data.profileImage}" alt="Profile Image" width="100">
                        <p>Follower Count: <span id="followerCount">${data.followerCount}</span>명</p>
                        <p>Following Count: <span id="followingCount">${data.followingCount}</span>명</p>
                    `;
        })
        .catch(error => {
            console.error('Error fetching user details:', error);
            document.getElementById("userDetailContainer").innerHTML = "<p>Error loading user details.</p>";
        });

    // 팔로워 목록
    document.getElementById("followerLink").addEventListener("click", function() {
        loadFollowerList();
    });

    // 팔로잉 목록
    document.getElementById("followingLink").addEventListener("click", function() {
        loadFollowingList(); //
    });
};

// 팔로워 목록
function loadFollowerList() {
    fetch(`/api/users/${userId}/follower`)
        .then(response => response.json())
        .then(data => {
            const followerList = document.getElementById("followerList");
            followerList.innerHTML = "";
            data.forEach(follower => {
                const div = document.createElement("div");
                div.classList.add("follower-item");

                const img = document.createElement("img");
                img.src = follower.profileImage;
                img.classList.add("profile-img");

                const name = document.createElement("span");
                name.textContent = follower.nickname;
                name.classList.add("nickname");

                div.addEventListener("click", () => {
                    window.location.href = `/myPage/info/${follower.userId}`;
                });

                div.appendChild(img);
                div.appendChild(name);
                followerList.appendChild(div);
            });
        })
        .catch(error => {
            console.error('Error loading followers:', error);
        });
}

// 팔로잉 목록
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

                div.addEventListener("click", () => {
                    window.location.href = `/myPage/info/${following.userId}`;
                });

                div.appendChild(img);
                div.appendChild(name);
                followingList.appendChild(div);
            });
        })
        .catch(error => {
            console.error('Error loading following:', error);
        });
}
