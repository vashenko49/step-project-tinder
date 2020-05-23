import axios from 'axios';

export default class SlideAPI {
    static getPackAccountForUser = () => axios.get("/api/v0/liked", {withCredentials: true}).then(res => res.data);
    static slidePartner = data => axios.post("/api/v0/liked", data, {withCredentials: true}).then(res => res.data);
    static getUserMatch = () => axios.get("/api/v0/match", {withCredentials: true}).then(res => res.data);
}