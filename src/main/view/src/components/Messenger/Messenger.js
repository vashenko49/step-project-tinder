import React from 'react';
import {makeStyles} from "@material-ui/core/styles";
import Chat from "../Chat/Chat";
import ListChats from "../ListChats/ListChates";

const useStyles = makeStyles((theme) => ({
    root: {
        height: "85vh",
        [theme.breakpoints.down('sm')]: {
            height: '92vh',
        },
        display: "flex",
        flexDirection: "row",
        flexWrap: "nowrap"
    }
}));
const Messenger = () => {
    const classes = useStyles();
    return (
        <div className={classes.root}>
            <ListChats/>
            <Chat/>
        </div>
    );
};

export default Messenger;