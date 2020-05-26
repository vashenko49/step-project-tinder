import React, {useState, useRef} from 'react';
import {connect} from "react-redux";
import {MessageList, Input, Button} from 'react-chat-elements'
import {makeStyles} from "@material-ui/core/styles";
import * as MESSAGE from '../../config/Messenger';
import {bindActionCreators} from "redux";
import {send} from "@giantmachines/redux-websocket/dist";
import * as MessageAction from "../../actions/Messenger/Messenger";

const useStyles = makeStyles((theme) => ({
    root: {
        width: "60%",
        padding: "0 10px 0 10px",
        boxSizing: "border-box",
        display: "flex",
        flexDirection: "column",
        justifyContent: "flex-end"
    },
    messageList: {
        overflow: "auto",
    }
}));

const Chat = ({Messenger: {messages, receiver, page, activeChat}, User: {userId}, send}) => {
    const classes = useStyles();

    const inputMes = useRef("");

    const [message, setMessage] = useState("");
    const sendMessage = () => {

        if (messages.length > 0) {
            const {from_id, chat_id, tou_id} = messages[0];
            send({
                type: MESSAGE.SEND_MESSAGE,
                receiver: from_id === userId ? tou_id : from_id,
                chatId: chat_id,
                text: message
            })
        } else {
            send({
                type: MESSAGE.SEND_MESSAGE,
                receiver: receiver,
                text: message
            })
        }

        setMessage("");
        inputMes.current.clear();
    };

    const scrollChat = e => {
        if (e.target.scrollTop === 0) {
            send({
                type: MESSAGE.GET_MESSAGES_FOR_ACTIVE_CHAT,
                activeChat: activeChat,
                page: page + 1
            })
        }
    }

    return (
        <div className={classes.root}>
            <MessageList
                className={classes.messageList}
                lockable={true}
                onScroll={scrollChat}
                dataSource={messages.map(c => {
                    const {message_id, time_send, from_id, message_text} = c;
                    return {
                        message_id: message_id,
                        position: from_id === userId ? "right" : "left",
                        type: "text",
                        text: message_text,
                        date: new Date(time_send),
                    }

                })}
            />
            <div className="send-messages-input">
                <Input
                    ref={inputMes}
                    placeholder="Type here..."
                    multiline={false}
                    value={message}
                    rightButtons={
                        <>
                            <Button
                                color="white"
                                backgroundColor="black"
                                text="SEND"
                                onClick={sendMessage}
                            />
                        </>
                    }
                    onKeyPress={e => {
                        if (e.key === "Enter") {
                            sendMessage();
                        }
                    }}
                    onChange={e => setMessage(e.target.value)}
                    inputStyle={{
                        border: "2px solid #dedede",
                        backgroundColor: "#f1f1f1",
                        borderRadius: "5px",
                        padding: "10px",
                        margin: "10px 0"
                    }}
                />
            </div>
        </div>
    );
};

const mapStateToProps = (state) => {
    return {
        Messenger: state.Messenger,
        User: state.User,
    };
}
const mapDispatchToProps = (dispatch) => {
    return {
        send: bindActionCreators(send, dispatch),
        setReceiver: bindActionCreators(MessageAction.setReceiver, dispatch),
    };
}


export default connect(mapStateToProps, mapDispatchToProps)(Chat);
