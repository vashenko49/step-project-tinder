import React from 'react';
import {ChatItem} from 'react-chat-elements'
import {connect} from "react-redux";
import cloudinary from 'cloudinary-core';
import Card from "@material-ui/core/Card";
import {makeStyles} from "@material-ui/core/styles";
import {bindActionCreators} from "redux";
import {send} from '@giantmachines/redux-websocket';
import * as MESSAGE from '../../config/Messenger';

const useStyles = makeStyles((theme) => ({
    root: {
        overflow: "auto",
        boxSizing: "border-box",
        width: "40%",
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
    activeChat: {
        backgroundColor: "black",

    }
}));
const ListChats = ({Messenger: {chats, activeChat}, User: {userId}, send}) => {
    const classes = useStyles();

    const onClick = (chat_id, receiver, number_unread) => {
        send({
            type: MESSAGE.GET_MESSAGES_FOR_ACTIVE_CHAT,
            activeChat: chat_id,
            page: 0
        })
        if (number_unread > 0) {
            send({
                type: MESSAGE.MAKE_READ_CHAT,
                chatID: chat_id,
                receiver: receiver
            })
        }
    }

    return (
        <Card className={classes.root}>

            {
                chats.map(c => {
                    const {from_id, tou_id, number_unread, time_send, message_text, chat_id} = c;
                    let field = from_id === userId ? "from" : "tou";

                    const img = new cloudinary.Cloudinary({
                        cloud_name: "dxge5r7h2"
                    }).url(c[`${field}_img_url`])

                    let name = c[`${field}_name`]

                    let id = from_id === userId ? tou_id : false;

                    return <ChatItem
                        statusColor={activeChat === chat_id ? "#eb3434" : ""}
                        key={chat_id}
                        avatar={img}
                        alt={name}
                        title={name}
                        subtitle={message_text}
                        date={new Date(time_send)}
                        unread={number_unread}
                        onClick={() => {
                            onClick(chat_id, id, number_unread)
                        }}
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

const mapDispatchToProps = (dispatch) => {
    return {
        send: bindActionCreators(send, dispatch),
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(ListChats);
