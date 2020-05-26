import React from 'react';
import {makeStyles} from "@material-ui/core/styles";
import Masonry from "react-masonry-css";
import Card from "@material-ui/core/Card";
import AssignmentIcon from '@material-ui/icons/Assignment';
import Typography from "@material-ui/core/Typography";
import {connect} from "react-redux";
import CardMedia from "@material-ui/core/CardMedia";
import {Image} from "cloudinary-react";
import CardContent from "@material-ui/core/CardContent";
import CardActions from "@material-ui/core/CardActions";
import IconButton from "@material-ui/core/IconButton";
import MessageIcon from "@material-ui/icons/Message";
import {bindActionCreators} from "redux";
import * as MessengerAction from "../../actions/Messenger/Messenger";

const useStyles = makeStyles((theme) => ({
    root: {
        height: "85vh",
        overflow: "auto",
        padding: "12px",
        boxSizing: "border-box",
        [theme.breakpoints.down('sm')]: {
            height: '100vh',
        },
        display: "flex",
        width: "auto",
        '&::-webkit-scrollbar': {
            width: '0.4em'
        },
        '&::-webkit-scrollbar-track': {
            '-webkit-box-shadow': 'inset 0 0 6px rgba(0,0,0,0.00)'
        },
        '&::-webkit-scrollbar-thumb': {
            backgroundColor: 'rgba(0,0,0,.1)',
            outline: '1px solid slategrey'
        }
    },
    columnClassName: {
        paddingLeft: "12px",
        boxSizing: "border-box"
    },
    emptyList: {
        height: "85vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center"
    },
    notFoundIcon: {
        fontSize: "18rem"
    },
    partnerAvatar: {
        width: "100%",
    },
    partnerCard: {
        margin: "12px 0"
    },
    buttons: {
        display: "flex",
        justifyContent: 'space-around'
    }
}));

const breakpointColumnsObj = {
    default: 3,
    1100: 2,
    700: 2,
    500: 1
};

const ListMatches = ({Slide: {matches}, goToMessage, startNewChat}) => {
    const classes = useStyles();
    return (
        <>
            {
                matches.length === 0 ? (
                    <div className={classes.emptyList}>
                        <AssignmentIcon className={classes.notFoundIcon}/>
                        <Typography variant="h6">
                            No matches
                        </Typography>
                    </div>
                ) : (
                    <Masonry
                        className={classes.root}
                        breakpointCols={breakpointColumnsObj}
                        columnClassName={classes.columnClassName}
                    >
                        {
                            matches.map(u => {
                                console.log(u);
                                const {age, first_name, img_url, userId} = u;
                                return (
                                    <Card key={userId} className={classes.partnerCard}>
                                        <CardMedia
                                            className={classes.partnerAvatar}
                                            component={Image}
                                            publicId={img_url}
                                        />
                                        <CardContent>
                                            <Typography gutterBottom variant="body1" component="h2">
                                                {first_name + ", " + age}
                                            </Typography>
                                        </CardContent>
                                        <CardActions className={classes.buttons}>
                                            <IconButton onClick={() => {
                                                goToMessage();
                                                startNewChat(userId);
                                            }} aria-label={"Message"}>
                                                <MessageIcon fontSize={"large"}/>
                                            </IconButton>
                                        </CardActions>
                                    </Card>
                                )
                            })
                        }
                    </Masonry>
                )
            }
        </>
    );
};


const mapStateToProps = (state) => {
    return {Slide: state.Slide};
}

const mapDispatchToProps = (dispatch) => {
    return {
        startNewChat: bindActionCreators(MessengerAction.startNewChat, dispatch),
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(ListMatches);