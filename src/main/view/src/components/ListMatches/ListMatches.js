import React, {useState, useEffect} from 'react';
import {makeStyles} from "@material-ui/core/styles";
import SlideAPI from "../../service/Slide";


const useStyles = makeStyles((theme) => ({
    root: {
        height: "85vh",
        overflow: "auto",
        padding: "12px",
        boxSizing: "border-box"
    }
}));
const ListMatches = () => {
    const classes = useStyles();
    const [matches, setMatches] = useState([]);
    useEffect(() => {
        SlideAPI.getUserMatch().then(res=>{
            console.log(res);
        })
    }, []);
    return (
        <div className={classes.root}>
            ListMatches
        </div>
    );
};

export default ListMatches;