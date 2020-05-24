import React from 'react';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as SlideAction from "../../actions/Slide/Slide";
import WatchLaterIcon from '@material-ui/icons/WatchLater';
import _ from 'lodash';
import {makeStyles} from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import {Image} from "cloudinary-react";
import Card from "@material-ui/core/Card";
import CardMedia from "@material-ui/core/CardMedia";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import ThumbDownIcon from '@material-ui/icons/ThumbDown';
import ThumbUpIcon from '@material-ui/icons/ThumbUp';
import MessageIcon from '@material-ui/icons/Message';
import IconButton from "@material-ui/core/IconButton";

const useStyles = makeStyles((theme) => ({
    root: {
        height: "100%"
    },
    notFound: {
        height: "100%",
        display: "flex",
        justifyContent: "center",
        flexDirection: "column",
        alignItems: "center"
    },
    notFoundIcon: {
        fontSize: "18rem"
    },
    avatarPartner: {
        width: "100%",
        minHeight: "150px",
        height: "55vh",
        objectFit: "cover"
    },
    card: {
        height: "100%",
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between"
    },
    cardContent: {
        marginTop: "6x",
        height: "22vh",
        overflow: "auto",
        wordWrap:"break-word",
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
    buttons: {
        height: "5vh%",
        display: "flex",
        justifyContent: 'space-around'
    }
}));
const SlidePartner = ({Slide: {users, currentSlide}, nextSlide}) => {
    const classes = useStyles();

    const onSlide = result => {
        nextSlide(currentSlide, users, result);
    }
    return (
        <div className={classes.root}>
            {
                users.length === 0 || currentSlide + 1 > users.length ? (<div className={classes.notFound}>
                    <WatchLaterIcon className={classes.notFoundIcon}/>
                    <Typography variant="h6">
                        No one found. Check back later
                    </Typography>
                </div>) : (
                    <Card className={classes.card}>
                        <div>
                            <CardMedia
                                className={classes.avatarPartner}
                                component={Image}
                                publicId={users[currentSlide].img_url}
                            />
                            <CardContent className={classes.cardContent}>
                                <Typography gutterBottom variant="h5" component="h2">
                                    {users[currentSlide].first_name + ", " + users[currentSlide].age}
                                </Typography>
                                {
                                    _.isString(users[currentSlide].interests) && users[currentSlide].interests.length > 0 && _.isString(users[currentSlide].aboutMe) && users[currentSlide].aboutMe.length > 0 ? (<>
                                        {
                                            _.isString(users[currentSlide].interests) && users[currentSlide].interests.length > 0 && (
                                                <>
                                                    <Typography variant="caption" color="textSecondary"
                                                                component="p">
                                                        Interests
                                                    </Typography>
                                                    <Typography variant="body2" color="textPrimary" component="p">
                                                        {users[currentSlide].interests}
                                                    </Typography>
                                                </>
                                            )

                                        }
                                        {
                                            _.isString(users[currentSlide].aboutMe) && users[currentSlide].aboutMe.length > 0 && (
                                                <>
                                                    <Typography variant="caption" color="textSecondary"
                                                                component="p">
                                                        About Me
                                                    </Typography>
                                                    <Typography variant="body2" color="textPrimary" component="p">
                                                        {users[currentSlide].aboutMe}
                                                    </Typography>
                                                </>
                                            )
                                        }
                                    </>) : (<>
                                        <Typography variant="body2" color="textSecondary" component="p">
                                            No data
                                        </Typography>
                                    </>)
                                }
                            </CardContent>
                        </div>
                        <CardActions className={classes.buttons}>
                            <IconButton onClick={() => {
                                onSlide(false)
                            }} aria-label={"Dislike"}>
                                <ThumbDownIcon fontSize={"large"}/>
                            </IconButton>
                            <IconButton aria-label={"Message"}>
                                <MessageIcon fontSize={"large"}/>
                            </IconButton>
                            <IconButton onClick={() => {
                                onSlide(true)
                            }} label={"Like"}>
                                <ThumbUpIcon fontSize={"large"}/>
                            </IconButton>
                        </CardActions>
                    </Card>)
            }

        </div>
    );
};


const mapStateToProps = (state) => {
    return {Slide: state.Slide};
}

function mapDispatchToProps(dispatch) {
    return {
        nextSlide: bindActionCreators(SlideAction.nextSlide, dispatch),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(SlidePartner);