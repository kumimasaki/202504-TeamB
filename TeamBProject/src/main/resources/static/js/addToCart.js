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
			// error
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
				if(message=='liked'){
					btn.innerText = "❤️ お気に入り済み";
					btn.classList.remove("not-liked");
					btn.classList.add("liked");
				}else{
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