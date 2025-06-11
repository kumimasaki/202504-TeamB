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
                messageBox.innerText = '';
                document.getElementById('messageBox').innerText = '';
            }, 3000);
        })
        .catch(error => {
            console.error('通信エラー:', error);
        });
}