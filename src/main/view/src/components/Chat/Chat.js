import React from 'react';
import {connect} from "react-redux";
import {MessageList, Input, Button} from 'react-chat-elements'
import {makeStyles} from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
    root: {
        width: "60%",
        padding: "0 10px 0 10px",
        boxSizing: "border-box"
    },
    messageList: {
        overflow: "auto"
    }
}));

const Chat = ({Messenger: {messages}, User: {userId}}) => {
    const classes = useStyles();

    return (
        <div className={classes.root}>
            <MessageList
                className={classes.messageList}
                lockable={true}
                dataSource={messages.map(c => {
                    const {message_id, read, time_send, from_id, tou_id, message_text} = c;
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
                    placeholder="Type here..."
                    multiline={false}
                    rightButtons={
                        <>
                            <Button
                                color="white"
                                backgroundColor="black"
                                text="SEND"
                                onClick={() => console.log("onClick")}
                            />
                        </>
                    }
                    onKeyPress={e => {
                        if (e.key === "Enter") {
                            console.log("onKeyPress")
                        }
                    }}
                    onChange={e => console.log("onChange")}
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


export default connect(mapStateToProps, null)(Chat);
