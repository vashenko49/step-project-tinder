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
import Container from "@material-ui/core/Container";
import NewReleasesOutlinedIcon from '@material-ui/icons/NewReleasesOutlined';
import Typography from "@material-ui/core/Typography";
import StyledLink from "../StyledLink/StyledLink";
import Badge from "@material-ui/core/Badge";

const useStyles = makeStyles((theme) => ({
    root: {
        height: "98vh"
    },
    blockWithMessengerAndListMatches: {
        [theme.breakpoints.down('sm')]: {
            display: 'none',
        },
    },
    bottomNavigation: {
        height: "5vh"
    },
    rootIsNotAuthorization: {
        height: "98vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center"
    },
    mainIcon: {
        fontSize: "18rem"
    }
}));

const HomePage = ({User: {isAuthorization}, Messenger: {unReadMessage}}) => {
    const classes = useStyles();
    const [value, setValue] = React.useState('messages');
    const handleChange = (event, newValue) => {
        setValue(newValue);
    };
    return (
        <>
            {
                isAuthorization ? (<Grid container className={classes.root}>
                    <Grid className={classes.blockWithMessengerAndListMatches} item xl={8} lg={8} md={8} sm={12} xs={12}>
                        <BottomNavigation value={value} onChange={handleChange}>
                            <BottomNavigationAction label="Messages" value="messages" icon={
                                <Badge badgeContent={unReadMessage} color="secondary">
                                    <MessageIcon/>
                                </Badge>
                            }/>
                            <BottomNavigationAction label="Matches" value="matches" icon={<FavoriteIcon/>}/>
                        </BottomNavigation>
                        {
                            value === 'messages' ? <Messenger/> : <ListMatches/>
                        }
                    </Grid>
                    <Grid item xl={4} lg={4} md={4} sm={12} xs={12}>
                        <SlidePartner/>
                    </Grid>
                </Grid>) : (
                    <Container className={classes.rootIsNotAuthorization}>
                        <NewReleasesOutlinedIcon className={classes.mainIcon}/>
                        <Typography variant="h6">
                            Welcome to my step project tinder =). To continue working,
                            <StyledLink to={"/sing-in"}> sign in </StyledLink>
                            to your account or go
                            through the <StyledLink to={"/sing-up"}> sign up </StyledLink> process.
                        </Typography>
                    </Container>
                )
            }

        </>
    );
};


const mapStateToProps = (state) => {
    return {
        User: state.User,
        Messenger: state.Messenger
    };
}

const mapDispatchToProps = (dispatch) => {
    return {
        startLoad: bindActionCreators(SystemAction.startLoad, dispatch)
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(HomePage);
