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
import {SnackbarProvider} from 'notistack';
import Notifier from "./components/Notifier/Notifier";
import Loader from "./components/Loader/Loader";
import {CloudinaryContext} from "cloudinary-react";
import 'react-chat-elements/dist/main.css';

const outerTheme = createMuiTheme({
    palette: {
        secondary: pink,
        primary: pink
    },
});

ReactDOM.render(
    <ThemeProvider theme={outerTheme}>
        <Provider store={configureStore()}>
            <CloudinaryContext className={"CloudinaryContext"} cloudName={"dxge5r7h2"}>
                <SnackbarProvider>
                    <BrowserRouter>
                        <Header/>
                        <Switch>
                            <Routing/>
                        </Switch>
                        <Notifier/>
                        <Loader/>
                    </BrowserRouter>
                </SnackbarProvider>
            </CloudinaryContext>
        </Provider>
    </ThemeProvider>
    ,
    document.getElementById('root')
);
