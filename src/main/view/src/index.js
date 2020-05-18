import React from 'react';
import ReactDOM from 'react-dom';
import {createMuiTheme, ThemeProvider} from '@material-ui/core/styles';
import {pink} from '@material-ui/core/colors';
import {Provider} from 'react-redux'
import {BrowserRouter, Switch} from 'react-router-dom';
import configureStore from './store/index';
import './index.css';
import Routing from "./components/Routing/Routing";
import Header from "./components/Header/Header";

const outerTheme = createMuiTheme({
    palette: {
        secondary: pink,
        primary: pink
    },
});

ReactDOM.render(
    <ThemeProvider theme={outerTheme}>
        <Provider store={configureStore()}>
            <BrowserRouter basename={"/step_project_tinder/"}>
                <Header/>
                <Switch>
                    <Routing/>
                </Switch>
            </BrowserRouter>
        </Provider>
    </ThemeProvider>
    ,
    document.getElementById('root')
);
