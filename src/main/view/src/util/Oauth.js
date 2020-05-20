export default class Oauth {
    static validateEmail = emailVal => {
        // eslint-disable-next-line
        let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(String(emailVal).toLowerCase());
    }
    static removeCookies = () => {
        let res = document.cookie;
        let multiple = res.split(";");
        for (let i = 0; i < multiple.length; i++) {
            let key = multiple[i].split("=");
            document.cookie = key[0] + " =; expires = Thu, 01 Jan 1970 00:00:00 UTC";
        }
    }
}

