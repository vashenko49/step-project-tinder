import React from 'react';
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
    root: {
        height: "85vh"
    }
}));
const Messenger = () => {
    const classes = useStyles();
    return (
        <div className={classes.root}>
            Messenger
        </div>
    );
};

export default Messenger;