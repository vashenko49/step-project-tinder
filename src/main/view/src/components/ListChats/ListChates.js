import React from 'react';
import {ChatItem} from 'react-chat-elements'
import {connect} from "react-redux";
import cloudinary from 'cloudinary-core';
import Card from "@material-ui/core/Card";
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
    root: {
        overflow: "auto",
        boxSizing: "border-box",
        width:"40%"
    }
}));
const ListChats = ({Messenger: {chats}, User: {userId}}) => {
    const classes = useStyles();

    const onClick = e => {
        console.log(e);
    }

    return (
        <Card className={classes.root}>

            {
                chats.map(c => {
                    const {from_id, number_unread, time_send, message_text, chat_id} = c;
                    let field = from_id === userId ? "from" : "tou";

                    const img = new cloudinary.Cloudinary({
                        cloud_name: "dxge5r7h2"
                    }).url(c[`${field}_img_url`])

                    let name = c[`${field}_name`]

                    return <ChatItem
                        key={message_text}
                        avatar={img}
                        alt={name}
                        title={name}
                        subtitle={message_text}
                        date={new Date(time_send)}
                        unread={number_unread}
                        chat_id={chat_id}
                        onClick={onClick}
                    />
                })
            }
        </Card>
    );
};


const mapStateToProps = (state) => {
    return {
        Messenger: state.Messenger,
        User: state.User
    };
}


export default connect(mapStateToProps, null)(ListChats);
