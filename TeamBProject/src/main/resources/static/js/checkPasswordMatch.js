function checkPasswordMatch() {
        const password = document.querySelector('input[id="password1"]').value;
        const confirmPassword = document.querySelector('input[id="confirmPassword"]').value;

        if (password !== confirmPassword) {
            alert("パスワードが一致しません。");
            return false; // フォーム送信を中止
        }
        return true;
    }