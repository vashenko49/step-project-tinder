import React from 'react';
import {connect} from "react-redux";
import Backdrop from "@material-ui/core/Backdrop";
import CircularProgress from "@material-ui/core/CircularProgress";
import {makeStyles} from "@material-ui/core/styles";
const useStyles = makeStyles({
    "backDrop":{
        "zIndex":"100"
    }
});


const Loader = ({System: {load}}) => {
    const classes = useStyles();
    return (
        <Backdrop className={classes.backDrop} open={load}>
            <CircularProgress color={"inherit"}/>
        </Backdrop>
    );
};

const mapStateToProps = (state) => {
    return {System: state.System};
}


export default connect(mapStateToProps, null)(Loader);