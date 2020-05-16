import React from 'react';
import {Route} from "react-router-dom";
import HomePage from "../HomePage/HomePage";

const Routing = () => {
    return (
        <>
            <Route exact path={"/"} component={HomePage}/>
        </>
    );
};

export default Routing;