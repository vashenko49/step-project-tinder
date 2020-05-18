import React from 'react';
import {Route} from "react-router-dom";
import HomePage from "../HomePage/HomePage";
import ErrorPage from "../ErrorPage/ErrorPage";
import SingIn from "../SingIn/SingIn";
import SingUp from "../SingUp/SingUp";

const Routing = () => {
    return (
        <>
            <Route exact path={"/"} component={HomePage}/>
            <Route exact path={"/error"} component={ErrorPage}/>
            <Route exact path={"/sing-in"} component={SingIn}/>
            <Route exact path={"/sing-up"} component={SingUp}/>
        </>
    );
};

export default Routing;