import axios from 'axios';

export default class ServiceUserAPI {
    static getUserDataBYJWT = () => axios.get("/api/v0/users", {withCredentials: true}).then(res => res.data)
    static signUpUser = data => axios.post("/api/v0/login", data).then(res => res.data);
    static signInUser = data => axios.put("/api/v0/login", data).then(res => res.data);
}