// 페이지 로드 시 API 호출
window.onload = function () {
    const userId = 1;
    // 유저 상세 정보 API 호출
    fetch(`/api/users/${userId}/info`)
        .then(response => response.json())
        .then(data => {
            const userDetailContainer = document.getElementById("userDetailContainer");
            userDetailContainer.innerHTML = `
                        <h2>${data.nickname}</h2>
                        <img src="${data.profileImage}" alt="Profile Image" width="100">
                        <p>Follower Count: ${data.followerCount}</p>
                        <p>Following Count: ${data.followingCount}</p>
                    `;
        })
        .catch(error => {
            console.error('Error fetching user details:', error);
            document.getElementById("userDetailContainer").innerHTML = "<p>Error loading user details.</p>";
        });
};