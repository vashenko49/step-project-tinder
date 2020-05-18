import React from 'react';
import {Route} from "react-router-dom";
import HomePage from "../HomePage/HomePage";
import ErrorPage from "../ErrorPage/ErrorPage";

const Routing = () => {
    return (
        <>
            <Route exact path={"/"} component={HomePage}/>
            <Route exact path={"/error"} component={ErrorPage}/>
        </>
    );
};

export default Routing;