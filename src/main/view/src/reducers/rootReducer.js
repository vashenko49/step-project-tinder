import {combineReducers} from 'redux';
import User from './User/User';
import Notistack from './Notistack/Notistack';
import System from './System/System';
import Slide from './Slide/Slide';

export default combineReducers({
    User,
    Notistack,
    System,
    Slide
});