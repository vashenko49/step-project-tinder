import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux'
import {BrowserRouter, Switch} from 'react-router-dom';
import configureStore from './store/index';
import './index.css';
import Routing from "./components/Routing/Routing";

ReactDOM.render(
    <React.StrictMode>
        <Provider store={configureStore()}>
            <BrowserRouter basename={"/step_project_tinder/"}>
                <Switch>
                    <Routing/>
                </Switch>
            </BrowserRouter>
        </Provider>
    </React.StrictMode>,
    document.getElementById('root')
);
