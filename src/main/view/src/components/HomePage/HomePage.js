import React from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as SystemAction from '../../actions/System/System';
import Grid from "@material-ui/core/Grid";
import SlidePartner from "../SlidePartner/SlidePartner";
import {makeStyles} from "@material-ui/core/styles";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import MessageIcon from '@material-ui/icons/Message';
import FavoriteIcon from '@material-ui/icons/Favorite';
import Messenger from "../Messenger/Messenger";
import ListMatches from "../ListMatches/ListMatches";

const useStyles = makeStyles((theme) => ({
    root: {
        height: "100%"
    },
    blockWithMessengerAndListMatches: {
        [theme.breakpoints.down('sm')]: {
            display: 'none',
        },
    },
    bottomNavigation: {
        height: "5vh"
    }
}));

const HomePage = () => {
    const classes = useStyles();
    const [value, setValue] = React.useState('matches');
    const handleChange = (event, newValue) => {
        setValue(newValue);
    };
    return (
        <Grid container className={classes.root}>
            <Grid className={classes.blockWithMessengerAndListMatches} item xl={8} lg={8} md={8} sm={12} xs={12}>
                <BottomNavigation value={value} onChange={handleChange}>
                    <BottomNavigationAction label="Messages" value="messages" icon={<MessageIcon/>}/>
                    <BottomNavigationAction label="Matches" value="matches" icon={<FavoriteIcon/>}/>
                </BottomNavigation>
                {
                    value === 'messages' ? <Messenger/> : <ListMatches/>
                }
            </Grid>
            <Grid item xl={4} lg={4} md={4} sm={12} xs={12}>
                <SlidePartner/>
            </Grid>
        </Grid>
    );
};


const mapStateToProps = (state) => {
    return {User: state.User};
}

const mapDispatchToProps = (dispatch) => {
    return {
        startLoad: bindActionCreators(SystemAction.startLoad, dispatch)
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(HomePage);
