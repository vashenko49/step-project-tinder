import React from 'react';
import {Route} from "react-router-dom";
import HomePage from "../HomePage/HomePage";
import ErrorPage from "../ErrorPage/ErrorPage";
import SingIn from "../SingIn/SingIn";
import SingUp from "../SingUp/SingUp";
import PrivateRoute from "./PrivateRoute/PrivateRoute";
import PersonalCabinet from "../PersonalCabinet/PersonalCabinet";

const Routing = () => {
    return (
        <>
            <Route exact path={"/"} component={HomePage}/>
            <Route exact path={"/error"} component={ErrorPage}/>
            <Route exact path={"/sing-in"} component={SingIn}/>
            <Route exact path={"/sing-up"} component={SingUp}/>
            <PrivateRoute exact path={"/personal-cabinet"} component={PersonalCabinet}/>
        </>
    );
};

export default Routing;