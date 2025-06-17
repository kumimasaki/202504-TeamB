function addToCart(lessonId) {
    fetch('/lesson/cart/all', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ lessonId: lessonId })
    })
        .then(response => response.text())
        .then(message => {
            if (message === "refuse") {
                window.location.href = "/user/login";
                return;
            }
            const messageBox = document.getElementById('messageBox-' + lessonId);
            if (messageBox) {
                messageBox.innerText = message;
            }
            if (document.getElementById('messageBox')) {
                document.getElementById('messageBox').innerText = message;
            }
            // 3秒後に自動消去
            setTimeout(() => {
                if (messageBox) {
                    messageBox.innerText = '';
                }
                if (document.getElementById('messageBox')) {
                    document.getElementById('messageBox').innerText = '';
                }
            }, 3000);
        })
        .catch(error => {
            console.error('通信エラー:', error);
        });
}

function addToCartInPlan(lessonId) {
    fetch('/lesson/cart/all', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ lessonId: lessonId })
    })
        .then(response => response.text())
        .then(message => {
            if (message === "refuse") {
                window.location.href = "/user/login";
                return;
            }
            const messageBox = document.getElementById('messageBox-' + lessonId);
            if (messageBox) {
                messageBox.innerText = message;
            }
            if (document.getElementById('messageBox')) {
                document.getElementById('messageBox').innerText = message;
            }
            // 3秒後に自動消去
            setTimeout(() => {
                if (messageBox) {
                    messageBox.innerText = '';
                }
                if (document.getElementById('messageBox')) {
                    document.getElementById('messageBox').innerText = '';
                }
            }, 3000);

            if (message == "✅ レッスンをカートに追加しました！") {
                setTimeout(() => {
                    window.location.href = "/lesson/show/cart";
                }, 500);
            }
        })
        .catch(error => {
            console.error('通信エラー:', error);
        });
}


function addToLike(lessonId) {
    fetch('/lesson/like/all', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ lessonId: lessonId })
    })
        .then(response => response.text())
        .then(message => {
            if (message === "refuse") {
                window.location.href = "/user/login";
                return;
            }
            const btn = document.getElementById('like_btn');
            if (btn) {
                if (message == 'liked') {
                    btn.innerText = "❤️ お気に入り済み";
                    btn.classList.remove("not-liked");
                    btn.classList.add("liked");
                } else {
                    btn.textContent = "お気に入り";
                    btn.classList.remove("liked");
                    btn.classList.add("not-liked");
                }
            }
        })
        .catch(error => {
            console.error('通信エラー:', error);
        });
}

function buy(button) {
    const lessonIdsStr = button.getAttribute("data-lesson-ids");
    const lessonIds = lessonIdsStr.split(',').map(id => parseInt(id));
    console.log(lessonIds)
    console.log(typeof lessonIds)
    fetch('/lesson/buy', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({ lessonIds: lessonIds })
    })
        .then(response => response.json())
        .then(data => {
            if (data.message === "refuse") {
                window.location.href = "/user/login";
                return;
            }

            // data.lessonIdListとmessageが必要
            if (Array.isArray(data.lessonIdList) && data.lessonIdList.length !== 0) {
                for (const lessonId of data.lessonIdList) {
                    const messageBox = document.getElementById('messageBox2-' + lessonId);
                    if (messageBox) {
                        messageBox.innerText = data.message;
                    }
                }
				
				alert("購入人数は上限に達しています。")
                setTimeout(() => {
                    if (Array.isArray(data.lessonIdList)) {
                        for (const lessonId of data.lessonIdList) {
                            const messageBox = document.getElementById('messageBox2-' + lessonId);
                            if (messageBox) {
                                messageBox.innerText = '';
                            }
                        }
                    }
                }, 3000);
            } else {
                window.location.href = "/lesson/request";
            }
        })
        .catch(error => {
            console.error('通信エラー:', error);
        });
}
