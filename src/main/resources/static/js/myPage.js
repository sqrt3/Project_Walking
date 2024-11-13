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
            document.getElementById("userLevel").innerText = data.userLevel;
            document.getElementById("userExp").innerText = data.userExp;
        });
}

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

                div.addEventListener("click", () => {
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
    const profileImageFile = document.getElementById("profileImage").files[0];

    const formData = new FormData();
    const updateData = JSON.stringify({ password, phone, nickname });
    formData.append("update", new Blob([updateData], { type: "application/json" }));

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
                div.classList.add("item");

                const itemInfo = document.createElement("div");
                itemInfo.textContent = `${item.name} x ${item.amount}`;
                div.appendChild(itemInfo);

                const useButton = document.createElement("button");
                useButton.textContent = "사용";
                useButton.classList.add("use-button", "btn", "btn-success");

                useButton.onclick = () => {
                    const confirmUse = confirm(`정말로 ${item.name}을(를) 사용하시겠습니까?`);
                    if (confirmUse) {
                        useItem(item.goodsId);
                    }
                };

                div.appendChild(useButton);
                itemList.appendChild(div);
            });
        });
}

function useItem(goodsId) {
    fetch(`/api/${userId}/items/${goodsId}/use`, {
        method: "POST"
    })
        .then(response => {
            if (response.ok) {
                alert("아이템을 사용했습니다!");
                loadUserItems();
            } else {
                alert("아이템 사용에 실패했습니다.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("아이템 사용 중 에러가 발생했습니다.");
        });
}

function loadPointLogs() {
    const pointLogs = document.getElementById("pointLogs");
    pointLogs.innerHTML = "";
    fetch(`/api/users/${userId}/points`)
        .then(response => response.json())
        .then(data => {
            data.forEach(log => {
                const row = document.createElement("tr");

                const descriptionTd = document.createElement("td");
                descriptionTd.textContent = log.description;
                row.appendChild(descriptionTd);

                const amountTd = document.createElement("td");
                amountTd.textContent = log.amount;
                row.appendChild(amountTd);

                pointLogs.appendChild(row);
            });
        });
}

function loadRecentPosts() {
    fetch(`/api/users/${userId}/recent-post`)
        .then(response => response.json())
        .then(data => {
            const recentPostsList = document.getElementById("recentPostsList");
            recentPostsList.innerHTML = "";

            const div = document.createElement("div");

            if (data.message) {
                div.textContent = data.message;
            } else if (data.postId && data.title) {
                const postLink = document.createElement("a");
                postLink.href = `/posts/${data.postId}`;
                postLink.textContent = data.title;
                div.appendChild(postLink);
            } else {
                div.textContent = "최근 본 게시물이 없습니다.";
            }

            recentPostsList.appendChild(div);
        })
        .catch(error => {
            console.error("게시물을 불러오는 데 실패했습니다.:", error);
        });
}


function loadUserPosts() {
    fetch(`/api/posts?userId=${userId}`)
        .then(response => response.json())
        .then(data => {
            const userPostsList = document.getElementById("userPostsList");
            userPostsList.innerHTML = "";

            data.forEach(post => {
                const postDiv = document.createElement("div");
                const postLink = document.createElement("a");
                postLink.href = `/posts/${post.postId}`;
                postLink.textContent = post.title;
                postDiv.appendChild(postLink);
                userPostsList.appendChild(postDiv);
            });
        })
        .catch(error => {
            console.error("게시글 불러오기 실패 :", error);
        });
}

function deleteAccount() {
    fetch(`/api/users/${userId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                alert("회원 탈퇴가 완료되었습니다.");
                window.location.href = "/auth/logout";
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
    loadRecentPosts();
    loadUserPosts();

    document.getElementById("followerLink").onclick = () => loadFollowerList();
    document.getElementById("followingLink").onclick = () => loadFollowingList();

    const myItemsModal = document.getElementById("myItemsModal");
    const myPointLogsModal = document.getElementById("myPointLogsModal");

    if (myItemsModal) {
        myItemsModal.addEventListener("shown.bs.modal", loadUserItems);
    }

    if (myPointLogsModal) {
        myPointLogsModal.addEventListener("shown.bs.modal", loadPointLogs);
    }
});
