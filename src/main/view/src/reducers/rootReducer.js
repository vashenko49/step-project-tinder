import {combineReducers} from 'redux';
import User from './User/User';
import Notistack from './Notistack/Notistack';
import System from './System/System'
export default combineReducers({
    User,
    Notistack,
    System
});