import React, {useEffect} from 'react';
import {makeStyles} from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import Badge from '@material-ui/core/Badge';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';
import AccountCircle from '@material-ui/icons/AccountCircle';
import MailIcon from '@material-ui/icons/Mail';
import MoreIcon from '@material-ui/icons/MoreVert';
import StyledLink from "../StyledLink/StyledLink";
import {connect} from "react-redux";
import * as UserAction from '../../actions/User/User'
import {bindActionCreators} from "redux";
import _ from 'lodash';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import Button from "@material-ui/core/Button";

const urlParams = new URLSearchParams(window.location.search);
const useStyles = makeStyles((theme) => ({
    grow: {
        flexGrow: 1,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        display: 'block',
    },
    sectionDesktop: {
        display: 'none',
        [theme.breakpoints.up('sm')]: {
            display: 'flex',
        },
    },
    sectionMobile: {
        display: 'flex',
        [theme.breakpoints.up('sm')]: {
            display: 'none',
        },
    },
    mainLink: {
        color: "white"
    },
    signOut:{
        color: "white",
        marginLeft:"10px"
    }
}));

const Header = ({User: {isAuthorization, unReadMessage}, getUserDataByJWT, SignOut}) => {
    useEffect(() => {
        if (_.isString(urlParams.get("oauth"))) {
            document.cookie = "oauth=" + urlParams.get("oauth");
        }
        getUserDataByJWT()
        // eslint-disable-next-line
    }, [])
    const classes = useStyles();
    const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = React.useState(null);
    const isMobileMenuOpen = Boolean(mobileMoreAnchorEl);
    const handleMobileMenuClose = () => {
        setMobileMoreAnchorEl(null);
    };
    const handleMobileMenuOpen = (event) => {
        setMobileMoreAnchorEl(event.currentTarget);
    };
    const mobileMenuId = 'primary-search-account-menu-mobile';
    const renderMobileMenu = (
        <Menu
            anchorEl={mobileMoreAnchorEl}
            anchorOrigin={{vertical: 'top', horizontal: 'right'}}
            id={mobileMenuId}
            keepMounted
            transformOrigin={{vertical: 'top', horizontal: 'right'}}
            open={isMobileMenuOpen}
            onClose={handleMobileMenuClose}
        >
            <MenuItem>
                <IconButton aria-label="show new mails" color="inherit">
                    <Badge badgeContent={unReadMessage} color="secondary">
                        <MailIcon/>
                    </Badge>
                </IconButton>
                <p>Messages</p>
            </MenuItem>
            <MenuItem>
                <IconButton
                    aria-label="account of current user"
                    aria-controls="primary-search-account-menu"
                    aria-haspopup="true"
                    color="inherit"
                >
                    <AccountCircle/>
                </IconButton>
                <p>Profile</p>
            </MenuItem>
            <MenuItem >
                <IconButton
                    onClick={SignOut}
                    aria-label="account of current user"
                    aria-controls="primary-search-account-menu"
                    aria-haspopup="true"
                    color="inherit"
                >
                    <ExitToAppIcon/>
                </IconButton>
                <p>Sign out</p>
            </MenuItem>
        </Menu>
    );

    return (
        <div className={classes.grow}>
            <AppBar position="static">
                <Toolbar>
                    <Typography className={classes.title} variant="h6" noWrap>
                        <StyledLink className={classes.mainLink} to={"/"}>
                            Step project tinder
                        </StyledLink>
                    </Typography>
                    <div className={classes.grow}/>
                    <div className={classes.sectionDesktop}>
                        {
                            isAuthorization ? (
                                <>
                                    <IconButton
                                        edge="end"
                                        aria-label="account of current user"
                                        aria-haspopup="true"
                                        color="inherit"
                                    >
                                        <AccountCircle/>
                                    </IconButton>
                                    <Button onClick={SignOut} className={classes.signOut}>
                                        <Typography variant="button">
                                            Sign out
                                        </Typography>
                                    </Button>
                                </>
                            ) : (
                                <>
                                    <Typography variant="button">
                                        <StyledLink className={classes.mainLink} to={"/sing-in"}>Sign In</StyledLink>
                                    </Typography>
                                    <Typography style={{marginLeft: "12px"}} variant="button">
                                        <StyledLink className={classes.mainLink} to={"/sing-up"}>Sign Up</StyledLink>
                                    </Typography>
                                </>
                            )
                        }

                    </div>
                    <div className={classes.sectionMobile}>
                        <IconButton
                            aria-label="show more"
                            aria-controls={mobileMenuId}
                            aria-haspopup="true"
                            onClick={handleMobileMenuOpen}
                            color="inherit"
                        >
                            <MoreIcon/>
                        </IconButton>
                    </div>
                </Toolbar>
            </AppBar>
            {renderMobileMenu}
        </div>
    );
};


const mapStateToProps = (state) => {
    return {User: state.User};
}
const mapDispatchToProps = (dispatch) => {
    return {
        getUserDataByJWT: bindActionCreators(UserAction.getUserDataByJWT, dispatch),
        SignOut: bindActionCreators(UserAction.SignOut, dispatch),
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(Header);
